package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MultiLimbedRenderer
{
    // Yeah, I know this is an AWFUL way to do things
    // It's a hack to get around the base ModelRenderer render function being full of private variables
    protected static IRenderTypeBuffer currentBuffer;
    protected static MultiLimbedModel currentModel;
    protected static ClientPlayerEntity currentEntity;
    protected static IVertexBuilder currentVertexBuilder;
    protected static ResourceLocation lastSkin = null;

    private static Field eyeHeightField;
    private static Field thirdPersonField;
    private static Method cameraMoveFunction;

    private static final double defaultCameraDistance = 0.5f;
    private static double decayingDistance = defaultCameraDistance;

    private static boolean fakeThird = false;

    private static boolean enableFullBodyFirstPerson = true;

    public static void setup()
    {
        eyeHeightField = ObfuscationReflectionHelper.findField(Entity.class,"eyeHeight");
        thirdPersonField = ObfuscationReflectionHelper.findField(ActiveRenderInfo.class, "thirdPerson");
        cameraMoveFunction = ObfuscationReflectionHelper.findMethod(ActiveRenderInfo.class, "setPosition", double.class, double.class, double.class);
    }

    // Toggle on the third person boolean in ActiveRenderInfo to allow the player model to be drawn even when in first person
    public static void fakeThirdPersonOn(double partialTicks)
    {
        if (!enableFullBodyFirstPerson)
            return;

        ActiveRenderInfo rendererInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();

        if (rendererInfo.isThirdPerson())
            return;

        fakeThird = true;

        try
        {
            thirdPersonField.setBoolean(rendererInfo, true);
        }
        catch(Exception e)
        {
            Cultivationcraft.LOGGER.error("Error adjusting third person toggle");
        }

        pushBackCamera(partialTicks);
    }

    // Push the camera to be infront of the player, but not so far infront that it sees through blocks
    private static void pushBackCamera(double partialTicks)
    {
        ActiveRenderInfo rendererInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
        Entity viewerEntity = rendererInfo.getRenderViewEntity();

        // Calculate the camera position and player direction
        Vector3d pos = new Vector3d(MathHelper.lerp((double)partialTicks, viewerEntity.prevPosX, viewerEntity.getPosX()), MathHelper.lerp((double)partialTicks, viewerEntity.prevPosY, viewerEntity.getPosY()), MathHelper.lerp((double)partialTicks, viewerEntity.prevPosZ, viewerEntity.getPosZ()));
        pos = pos.add(0, viewerEntity.getEyeHeight(), 0);
        Vector3d direction = Vector3d.fromPitchYaw(0, viewerEntity.getYaw((float)partialTicks));

        // Calculate the amount the camera needs to be pushed back to not hit a wall, set decayingDistance to equal that amount if it is smaller than it
        double cameraPush = calcCameraDistance(rendererInfo.getRenderViewEntity(), defaultCameraDistance + 0.15, direction) - 0.15;

        if (cameraPush < decayingDistance)
            decayingDistance = cameraPush;

        // Pushing camera position forward by decayingDistance across the X & Z axies
        pos = pos.add(direction.scale(decayingDistance));

        // Move the camera so that it's just in front of the head rather than inside it
        // Adjust the amount by decayingDistance so that it is pushed back enough to not see through walls
        try
        {
            cameraMoveFunction.invoke(rendererInfo, pos.x, pos.y, pos.z);
        }
        catch(Exception e)
        {
            Cultivationcraft.LOGGER.error("Error adjusting camera position");
        }
    }

    private static void decayCameraPushback(float partialTick)
    {
        if (decayingDistance == defaultCameraDistance)
            return;

        decayingDistance += partialTick / 40.0;

        if (decayingDistance > defaultCameraDistance)
            decayingDistance = defaultCameraDistance;
    }

    // Calculate the distance the camera should be from the specified entity (up to a max of startingDistance)
    private static double calcCameraDistance(Entity renderViewEntity, double startingDistance, Vector3d direction)
    {
        // Get the players position
        Vector3d pos = renderViewEntity.getPositionVec().add(0, renderViewEntity.getEyeHeight(), 0);

        // Test against 8 seperate points
        for(int i = 0; i < 8; ++i)
        {
            float f = (float) ((i & 1) * 2 - 1);
            float f1 = (float) ((i >> 1 & 1) * 2 - 1);
            float f2 = (float) ((i >> 2 & 1) * 2 - 1);
            f = f * 0.15F;
            f1 = f1 * 0.15F;
            f2 = f2 * 0.15F;

            // Calculate the position to test
            Vector3d modifiedPos = pos.add(f, f1, f2);

            // Calculate the camera position
            Vector3d testPos = modifiedPos.add(direction.scale(startingDistance));

            // Test the distance between the players and camera position, checking if it's blocked by anything visualy
            RayTraceResult raytraceresult = renderViewEntity.world.rayTraceBlocks(new RayTraceContext(modifiedPos, testPos, RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, renderViewEntity));

            // If it is blocked, check the distance and set that to the new camera distance
            if (raytraceresult.getType() != RayTraceResult.Type.MISS)
            {
                double d0 = raytraceresult.getHitVec().distanceTo(pos);
                if (d0 < startingDistance)
                    startingDistance = d0;
            }
        }

        return startingDistance;
    }

    public static boolean isFakeThirdPerson()
    {
        return fakeThird;
    }

    // Toggle off the third person boolean so that the camera will still render in first person
    public static void fakeThirdPersonOff()
    {
        if (!enableFullBodyFirstPerson)
            return;

        if (!fakeThird)
            return;

        ActiveRenderInfo rendererInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();

        try
        {
            thirdPersonField.setBoolean(rendererInfo, false);
        }
        catch(Exception e)
        {
            Cultivationcraft.LOGGER.error("Error adjusting third person toggle");
        }

        fakeThird = false;
    }
    public static void doModelCalculations(ClientPlayerEntity entityIn, MatrixStack matrixStackIn, float partialTicks, PlayerPoseHandler handler)
    {
        PoseHandler.applyRotations(entityIn, matrixStackIn, partialTicks, 0, partialTicks);
        PoseHandler.doPose(entityIn.getUniqueID(), partialTicks);

        handler.model.calculateHeightAdjustment();
    }

    public static void adjustEyeHeight(ClientPlayerEntity player, PlayerPoseHandler handler)
    {

        float eyeHeight = handler.model.calculateEyeHeight() * -1;

        try
        {
            eyeHeightField.setFloat(player, eyeHeight);
        }
        catch(Exception e)
        {
            Cultivationcraft.LOGGER.error("Error adjusting player eye height");
        }
    }

    public static boolean renderFirstPerson(ClientPlayerEntity entityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        PoseHandler.setupPoseHandler(entityIn);
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(entityIn.getUniqueID());

        if(!enableFullBodyFirstPerson)
            doModelCalculations(entityIn, matrixStackIn, partialTicks, handler);

        adjustEyeHeight(entityIn, handler);

        // Decay the camera pushback so it reverts from being pushed back smoothly rather than being jerked forwards
        decayCameraPushback(partialTicks);

        render2FirstPerson(handler.model, entityIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        return enableFullBodyFirstPerson;
    }

    public static void render2FirstPerson(MultiLimbedModel entityModel, ClientPlayerEntity entityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        matrixStackIn.push();

        /*
        PoseHandler.applyRotations(entityIn, matrixStackIn, totalTicks, f, partialTicks);

        entityModel.setLivingAnimations(entityIn, f5, f8, partialTicks);
        entityModel.setRotationAngles(entityIn, f5, f8, totalTicks, f2, f6);


        PoseHandler.doPose(entityIn.getUniqueID(), partialTicks);

        entityModel.calculateHeightAdjustment();
        double height = entityModel.getHeightAdjustment();*/

        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        //matrixStackIn.translate(0.0D, 0, 0.0D);

        currentModel = entityModel;
        currentEntity = entityIn;
        currentBuffer = bufferIn;

        RenderType rendertype = getRenderType(getSkin(currentEntity));

        if (rendertype != null)
        {
            int i = LivingRenderer.getPackedOverlay(entityIn, 0);
            entityModel.renderFirstPerson(matrixStackIn, null, packedLightIn, i, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        lastSkin = null;

        matrixStackIn.pop();
    }

    public static boolean render(ClientPlayerEntity entityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        PoseHandler.setupPoseHandler(entityIn);
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(entityIn.getUniqueID());

        render2(handler.model, entityIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        return true;
    }

    public static void render2(MultiLimbedModel entityModel, ClientPlayerEntity entityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        matrixStackIn.push();

        boolean shouldSit = PoseHandler.shouldSit(entityIn);

        entityModel.baseModel.isSitting = shouldSit;

        float f = MathHelper.interpolateAngle(partialTicks, entityIn.prevRenderYawOffset, entityIn.renderYawOffset);
        float f1 = MathHelper.interpolateAngle(partialTicks, entityIn.prevRotationYawHead, entityIn.rotationYawHead);

        float f2 = f1 - f;
        if (shouldSit && entityIn.getRidingEntity() instanceof LivingEntity)
        {
            LivingEntity livingentity = (LivingEntity)entityIn.getRidingEntity();
            f = MathHelper.interpolateAngle(partialTicks, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
            f2 = f1 - f;
            float f3 = MathHelper.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);
        if (entityIn.getPose() == Pose.SLEEPING) {
            Direction direction = entityIn.getBedDirection();
            if (direction != null) {
                float f4 = entityIn.getEyeHeight(Pose.STANDING) - 0.1F;
                matrixStackIn.translate((-direction.getXOffset() * f4), 0.0D, (-direction.getZOffset() * f4));
            }
        }

        float totalTicks = (float)entityIn.ticksExisted + partialTicks;

        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entityIn.isAlive()) {
            f8 = MathHelper.lerp(partialTicks, entityIn.prevLimbSwingAmount, entityIn.limbSwingAmount);
            f5 = entityIn.limbSwing - entityIn.limbSwingAmount * (1.0F - partialTicks);
            if (entityIn.isChild()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        PoseHandler.applyRotations(entityIn, matrixStackIn, totalTicks, f, partialTicks);

        entityModel.setLivingAnimations(entityIn, f5, f8, partialTicks);
        entityModel.setRotationAngles(entityIn, f5, f8, totalTicks, f2, f6);


        PoseHandler.doPose(entityIn.getUniqueID(), partialTicks);

        entityModel.calculateHeightAdjustment();
        double height = entityModel.getHeightAdjustment();

        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.translate(0.0D, 0 - height, 0.0D);

        currentModel = entityModel;
        currentEntity = entityIn;
        currentBuffer = bufferIn;

        RenderType rendertype = getRenderType(getSkin(currentEntity));

        if (rendertype != null)
        {
            int i = LivingRenderer.getPackedOverlay(entityIn, 0);
            entityModel.render(matrixStackIn, null, packedLightIn, i, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        lastSkin = null;

        matrixStackIn.pop();
    }

    // Returns the vertex builder for the current entity
    public static IVertexBuilder getVertexBuilder()
    {
        return getVertexBuilder(getSkin(currentEntity));
    }

    // Returns the vertex builder for the current entity using the supplied skin
    public static IVertexBuilder getVertexBuilder(ResourceLocation resourceLocation)
    {
        // If the last vertexbuilder call used the same skin, then don't bother recreating it
        if (lastSkin != resourceLocation)
        {
            RenderType rendertype = getRenderType(resourceLocation);
            currentVertexBuilder = currentBuffer.getBuffer(rendertype);
        }

        return currentVertexBuilder;
    }

    public static RenderType getRenderType(ResourceLocation resourcelocation)
    {
        boolean invis = currentEntity.isInvisible();
        boolean visible = !invis && !currentEntity.isInvisibleToPlayer(Minecraft.getInstance().player);
        boolean glowing = currentEntity.isGlowing();

        if (invis)
        {
            return RenderType.getItemEntityTranslucentCull(resourcelocation);
        }
        else if (visible)
        {
            return currentModel.getRenderType(resourcelocation);
        }
        else
        {
            return glowing ? RenderType.getOutline(resourcelocation) : null;
        }
    }

    public static ResourceLocation getSkin(ClientPlayerEntity EntityIn)
    {
        return EntityIn.getLocationSkin();
    }
}
