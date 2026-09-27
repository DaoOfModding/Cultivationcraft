package DaoOfModding.Cultivationcraft.Client.Renderers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Blocks.Plants.ProceduralPlantBlock;
import DaoOfModding.Cultivationcraft.Common.Blocks.Plants.entity.ProceduralPlantBlockEntity;
import DaoOfModding.Cultivationcraft.Common.Blocks.Plants.world.ClientPlantCatalog;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.DivineSenseTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.DivineSenseTechnique.SenseProfile;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Tag-driven, client-only detection; never requests chunks or changes block rendering. */
@Mod.EventBusSubscriber(modid = Cultivationcraft.MODID, value = Dist.CLIENT)
public final class DivineSenseBlockRenderer {
    public static final TagKey<Block> VISIBLE_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY,
            new ResourceLocation(Cultivationcraft.MODID, "divine_sense_visible"));
    private record ScanSection(BlockPos origin, double distanceSquared) {}
    private record Target(BlockPos pos, double distanceSquared) {}

    private static final int SECTIONS_PER_TICK = 8;
    private static final List<ScanSection> pendingSections = new ArrayList<>();
    private static final PriorityQueue<Target> candidates = new PriorityQueue<>(
            Comparator.comparingDouble(Target::distanceSquared).reversed());
    private static SenseProfile scanProfile;
    private static Vec3 scanOrigin;
    private static int sectionIndex;
    private static final int SCAN_INTERVAL = 10;
    private static final List<BlockPos> nearby = new ArrayList<>();
    private static final BufferBuilder BUFFER = new BufferBuilder(32768);
    private static final BufferBuilder PLANT_BUFFER = new BufferBuilder(16384);
    private static ClientLevel scannedLevel;
    private static int scanTicks;

    private static boolean isActive(Minecraft mc) {
        if (mc.player == null || mc.level == null || !mc.player.isAlive() || mc.player.isSpectator()) return false;
        var techniques = CultivatorTechniques.getCultivatorTechniques(mc.player);
        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++) {
            var technique = techniques.getTechnique(i);
            if (technique instanceof DivineSenseTechnique && technique.isActive() && technique.isValid(mc.player)) return true;
        }
        return false;
    }

    private static void clearScan() {
        nearby.clear();
        pendingSections.clear();
        candidates.clear();
        scannedLevel = null;
        scanProfile = null;
        scanOrigin = null;
        sectionIndex = 0;
        scanTicks = 0;
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (!isActive(mc)) {
            clearScan();
            return;
        }
        if (mc.isPaused()) return;
        SenseProfile current = DivineSenseTechnique.getSenseProfile(mc.player);
        Vec3 origin = mc.player.getEyePosition();
        // Refresh immediately on a breakthrough, realm reset, teleport, or world change.
        if (scannedLevel != mc.level || !current.equals(scanProfile)
                || (scanOrigin != null && origin.distanceToSqr(scanOrigin) > 64)) {
            if (scannedLevel != mc.level) clearScan();
            pendingSections.clear();
            candidates.clear();
            scanTicks = 0;
            scannedLevel = mc.level;
            scanProfile = current;
        }
        if (pendingSections.isEmpty()) {
            if (scanTicks-- > 0) return;
            beginScan(mc, current, origin);
        }
        scanNextSections(mc);
    }

    private static void beginScan(Minecraft mc, SenseProfile current, Vec3 origin) {
        scanOrigin = origin;
        scanProfile = current;
        sectionIndex = 0;
        candidates.clear();
        int radius = current.radius();
        int minX = Mth.floor(origin.x - radius) >> 4, maxX = Mth.floor(origin.x + radius) >> 4;
        int minZ = Mth.floor(origin.z - radius) >> 4, maxZ = Mth.floor(origin.z + radius) >> 4;
        for (int chunkX = minX; chunkX <= maxX; chunkX++) {
            for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
                var chunk = mc.level.getChunkSource().getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
                if (chunk == null) continue;
                var sections = chunk.getSections();
                for (int index = 0; index < sections.length; index++) {
                    int y = chunk.getSectionYFromSectionIndex(index) * 16;
                    BlockPos start = new BlockPos(chunkX * 16, y, chunkZ * 16);
                    double distance = distanceSquaredToBox(origin, new AABB(start, start.offset(16, 16, 16)));
                    if (distance > radius * radius || sections[index].hasOnlyAir()
                            || !sections[index].maybeHas(state -> state.is(VISIBLE_BLOCKS))) continue;
                    pendingSections.add(new ScanSection(start, distance));
                }
            }
        }
        pendingSections.sort(Comparator.comparingDouble(ScanSection::distanceSquared));
    }

    private static void scanNextSections(Minecraft mc) {
        int processed = 0;
        while (sectionIndex < pendingSections.size() && processed++ < SECTIONS_PER_TICK) {
            ScanSection next = pendingSections.get(sectionIndex++);
            // Once enough nearby targets exist, farther sections cannot improve the selection.
            if (candidates.size() == scanProfile.maxHighlights()
                    && next.distanceSquared() > candidates.peek().distanceSquared()) {
                sectionIndex = pendingSections.size();
                break;
            }
            BlockPos start = next.origin();
            var chunk = mc.level.getChunkSource().getChunk(start.getX() >> 4, start.getZ() >> 4, ChunkStatus.FULL, false);
            if (chunk == null) continue;
            var section = chunk.getSection(chunk.getSectionIndex(start.getY()));
            if (!section.maybeHas(state -> state.is(VISIBLE_BLOCKS))) continue;
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    for (int x = 0; x < 16; x++) {
                        if (!section.getBlockState(x, y, z).is(VISIBLE_BLOCKS)) continue;
                        BlockPos pos = start.offset(x, y, z);
                        double distance = Vec3.atCenterOf(pos).distanceToSqr(scanOrigin);
                        if (distance >= scanProfile.radius() * scanProfile.radius()) continue;
                        if (candidates.size() < scanProfile.maxHighlights()) candidates.add(new Target(pos, distance));
                        else if (distance < candidates.peek().distanceSquared()) {
                            candidates.poll();
                            candidates.add(new Target(pos, distance));
                        }
                    }
                }
            }
        }
        // Keep the last complete snapshot while scanning; partial replacements blink
        // distant targets off at the start of every refresh.
        if (sectionIndex >= pendingSections.size()) {
            nearby.clear();
            candidates.stream().sorted(Comparator.comparingDouble(Target::distanceSquared))
                    .forEach(target -> nearby.add(target.pos()));
            pendingSections.clear();
            scanTicks = SCAN_INTERVAL - 1;
        }
    }

    private static double distanceSquaredToBox(Vec3 point, AABB box) {
        double x = point.x - Mth.clamp(point.x, box.minX, box.maxX);
        double y = point.y - Mth.clamp(point.y, box.minY, box.maxY);
        double z = point.z - Mth.clamp(point.z, box.minZ, box.maxZ);
        return x * x + y * y + z * z;
    }

    private static double smoothstep(double start, double end, double value) {
        double t = Mth.clamp((value - start) / (end - start), 0, 1);
        return t * t * (3 - 2 * t);
    }

    @SubscribeEvent
    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return;
        Minecraft mc = Minecraft.getInstance();
        if (!isActive(mc) || mc.options.hideGui || scannedLevel != mc.level || nearby.isEmpty()) return;
        SenseProfile current = DivineSenseTechnique.getSenseProfile(mc.player);
        var pose = event.getPoseStack();
        Vec3 camera = event.getCamera().getPosition();
        Vec3 origin = mc.player.getEyePosition(event.getPartialTick());
        boolean depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean depthWrite = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        float lineWidth = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
        var shader = RenderSystem.getShader();
        // AFTER_WEATHER already applies the view rotation to model-view. Use the
        // event's matrix exactly once, including in Fabulous graphics mode.
        var modelView = RenderSystem.getModelViewStack();
        modelView.pushPose();
        modelView.setIdentity();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.lineWidth(1);
        try {
            BUFFER.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            PLANT_BUFFER.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            int highlighted = 0;
            for (BlockPos pos : nearby) {
                if (highlighted >= current.maxHighlights()) break;
                if (!mc.level.hasChunkAt(pos)) continue;
                var state = mc.level.getBlockState(pos);
                if (!state.is(VISIBLE_BLOCKS)) continue;
                double distance = Vec3.atCenterOf(pos).distanceTo(origin);
                if (distance >= current.radius()) continue;
                var shape = state.getShape(mc.level, pos);
                if (shape.isEmpty()) continue;
                AABB bounds = shape.bounds().move(pos).inflate(0.01);
                if (!event.getFrustum().isVisible(bounds)) continue;
                // Clearer as targets approach, but almost invisible within arm's reach.
                // Use distance to the shape/camera for close fading, including third person.
                double closeFade = smoothstep(0.5, 4, Math.sqrt(distanceSquaredToBox(camera, bounds)));
                // Reach the realm's opacity ceiling three blocks inside the detection edge.
                double rangeFade = smoothstep(0, 3, current.radius() - distance);
                int alpha = (int) Math.round(255 * current.peakOpacity() * closeFade * rangeFade);
                if (alpha == 0) continue;
                highlighted++;
                // Subtract in double precision before sending positions to the GPU.
                AABB relativeBounds = bounds.move(-camera.x, -camera.y, -camera.z);
                if (state.getBlock() instanceof ProceduralPlantBlock)
                    cross(pose.last().pose(), relativeBounds, plantColor(mc.level, pos, state), alpha);
                else outline(pose.last().pose(), relativeBounds, 0x91DCCD, alpha);
            }
            BufferUploader.drawWithShader(BUFFER.end());
            // Width is a draw state: use a separate batch to keep ore outlines thin.
            RenderSystem.lineWidth(2);
            BufferUploader.drawWithShader(PLANT_BUFFER.end());
        } finally {
            modelView.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.depthMask(depthWrite);
            if (depthTest) RenderSystem.enableDepthTest();
            else RenderSystem.disableDepthTest();
            if (blend) RenderSystem.enableBlend();
            else RenderSystem.disableBlend();
            RenderSystem.lineWidth(lineWidth);
            RenderSystem.setShader(() -> shader);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }

    private static int plantColor(ClientLevel level, BlockPos pos, BlockState state) {
        var entry = ClientPlantCatalog.get(state.getValue(ProceduralPlantBlock.SPECIES));
        if (entry == null) return 0xFFFFFF;
        ResourceLocation elementId = ResourceLocation.tryParse(entry.element);
        var element = elementId == null ? null : Elements.getElement(elementId);
        if (element == null) return 0xFFFFFF;

        // The block state supplies a stable fallback while block-entity data arrives.
        int growth = switch (state.getValue(ProceduralPlantBlock.TIER)) {
            case 3 -> 1000;
            case 2 -> 100;
            default -> 0;
        };
        if (level.getBlockEntity(pos) instanceof ProceduralPlantBlockEntity plant)
            growth = plant.getSpiritualGrowth();
        // A logarithmic curve gives low-growth plants noticeable color quickly,
        // with diminishing gains toward full element color at maximum growth.
        double clampedGrowth = Mth.clamp((double) growth, 0, ProceduralPlantBlockEntity.MAX_SPIRITUAL_GROWTH);
        float strength = (float) (Math.log1p(clampedGrowth)
                / Math.log1p(ProceduralPlantBlockEntity.MAX_SPIRITUAL_GROWTH));
        int red = Math.round(Mth.lerp(strength, 255f, element.color.getRed()));
        int green = Math.round(Mth.lerp(strength, 255f, element.color.getGreen()));
        int blue = Math.round(Mth.lerp(strength, 255f, element.color.getBlue()));
        return (red << 16) | (green << 8) | blue;
    }

    private static void cross(Matrix4f matrix, AABB box, int color, int alpha) {
        // Three intersecting world-space axes, rather than a camera-facing marker.
        double x = (box.minX + box.maxX) * .5;
        double y = (box.minY + box.maxY) * .5;
        double z = (box.minZ + box.maxZ) * .5;
        line(PLANT_BUFFER, matrix, box.minX, y, z, box.maxX, y, z, color, alpha);
        line(PLANT_BUFFER, matrix, x, box.minY, z, x, box.maxY, z, color, alpha);
        line(PLANT_BUFFER, matrix, x, y, box.minZ, x, y, box.maxZ, color, alpha);
    }

    private static void outline(Matrix4f matrix, AABB box, int color, int alpha) {
        for (int corner = 0; corner < 8; corner++) {
            double x = (corner & 1) == 0 ? box.minX : box.maxX;
            double y = (corner & 2) == 0 ? box.minY : box.maxY;
            double z = (corner & 4) == 0 ? box.minZ : box.maxZ;
            if ((corner & 1) == 0) line(BUFFER, matrix, x, y, z, box.maxX, y, z, color, alpha);
            if ((corner & 2) == 0) line(BUFFER, matrix, x, y, z, x, box.maxY, z, color, alpha);
            if ((corner & 4) == 0) line(BUFFER, matrix, x, y, z, x, y, box.maxZ, color, alpha);
        }
    }

    private static void line(BufferBuilder buffer, Matrix4f matrix, double x, double y, double z,
                             double endX, double endY, double endZ, int color, int alpha) {
        int red = (color >> 16) & 255, green = (color >> 8) & 255, blue = color & 255;
        buffer.vertex(matrix, (float) x, (float) y, (float) z).color(red, green, blue, alpha).endVertex();
        buffer.vertex(matrix, (float) endX, (float) endY, (float) endZ).color(red, green, blue, alpha).endVertex();
    }
}
