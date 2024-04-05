package DaoOfModding.Cultivationcraft.Client.Renderers.BlockEntityRenderers;

import DaoOfModding.Cultivationcraft.Common.Blocks.custom.FrozenBlock;
import DaoOfModding.Cultivationcraft.Common.Blocks.entity.FrozenBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FrozenBlockEntityRenderer implements BlockEntityRenderer<FrozenBlockEntity> {
    public FrozenBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FrozenBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        BlockState blockState = pBlockEntity.getOldBlockState();
        String name = blockState.getBlock().getName().getString();

        if (blockState.getBlock() instanceof SkullBlock || name.contains("Banner") || name.contains("Sign")) {
            renderItem(blockState, pBlockEntity, pPoseStack, pBufferSource, pPackedOverlay);
        } else {
            renderBlock(name, blockState, pBlockEntity, pPoseStack, pBufferSource, pPackedOverlay);
        }
    }

    private void renderItem(BlockState oldBlockState, FrozenBlockEntity pBlockEntity, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getRenderStack();

        pPoseStack.pushPose();
        pPoseStack.translate(0.5D, 0.5D, 0.5D);
        pPoseStack.scale(0.999F, 0.999F, 0.999F);

        float f = 0F;
        if (oldBlockState.getBlock() instanceof BannerBlock) {
            f = (float) -oldBlockState.getValue(BannerBlock.ROTATION);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees((f * 360) / 16F));

        } else if (oldBlockState.getBlock() instanceof WallBannerBlock) {
            f = -oldBlockState.getValue(WallBannerBlock.FACING).toYRot();
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(-((f * 360) / 16F)));

        }
        if (!pBlockEntity.isSecondBlock()) {
            itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.NONE,
                    getLightLevel(Objects.requireNonNull(pBlockEntity.getLevel()), pBlockEntity.getBlockPos()),
                    pPackedOverlay, pPoseStack, pBufferSource, 1);
        }
        pPoseStack.popPose();
    }

    private void renderBlock(String name, BlockState oldBlockState, FrozenBlockEntity pBlockEntity, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedOverlay) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

        pPoseStack.pushPose();
        pPoseStack.translate(0.0005D, 0.0005D, 0.0005D);
        pPoseStack.scale(0.999F, 0.999F, 0.999F);

        if (name.contains("Chest") || name.contains("Bed")) renderSpecial(pBlockEntity, pPoseStack);
        if (!pBlockEntity.isSecondBlock()) {
            blockRenderer.renderSingleBlock(oldBlockState, pPoseStack, pBufferSource,
                    getLightLevel(Objects.requireNonNull(pBlockEntity.getLevel()), pBlockEntity.getBlockPos()),
                    pPackedOverlay);
        }
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    private void renderSpecial(FrozenBlockEntity pBlockEntity, PoseStack pPoseStack) {
        float f = pBlockEntity.getBlockState().getValue(FrozenBlock.FACING).toYRot();
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(-f));

        switch (pBlockEntity.getBlockState().getValue(FrozenBlock.FACING)) {
            case NORTH -> pPoseStack.translate(-1D, 0D, -1D);
            case SOUTH -> pPoseStack.translate(0D, 0D, 0D);
            case WEST -> pPoseStack.translate(0D, 0D, -1D);
            case EAST -> pPoseStack.translate(-1D, 0D, 0D);
        }
    }
}
