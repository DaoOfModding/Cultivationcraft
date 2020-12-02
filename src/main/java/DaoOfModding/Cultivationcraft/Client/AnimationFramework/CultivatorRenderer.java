package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class CultivatorRenderer
{
    public static boolean render(PlayerRenderer renderer, ClientPlayerEntity entityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        PoseHandler.setupPoseHandler(entityIn.getUniqueID(), renderer.getEntityModel());
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(entityIn.getUniqueID());

        render2(handler.model, entityIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        return true;
    }

    public static void render2(MultiLimbedModel entityModel, ClientPlayerEntity entityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        matrixStackIn.push();
        //entityIn.swingProgress = this.getSwingProgress(entityIn, partialTicks);

        boolean shouldSit = PoseHandler.shouldSit(entityIn);

        entityModel.baseModel.isSitting = shouldSit;
        entityModel.baseModel.isChild = entityIn.isChild();

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
                matrixStackIn.translate((double)((float)(-direction.getXOffset()) * f4), 0.0D, (double)((float)(-direction.getZOffset()) * f4));
            }
        }

        float totalTicks = (float)entityIn.ticksExisted + partialTicks;

        PoseHandler.applyRotations(entityIn, matrixStackIn, totalTicks, f, partialTicks);

        double height = entityModel.getHeightAdjustment();

        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.translate(0.0D, (double)-1.501F + height, 0.0D);

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

        entityModel.setLivingAnimations(entityIn, f5, f8, partialTicks);
        entityModel.setRotationAngles(entityIn, f5, f8, totalTicks, f2, f6);

        PoseHandler.doPose(entityIn.getUniqueID(), partialTicks);

        RenderType rendertype = getRenderType(entityModel, entityIn);

        if (rendertype != null)
        {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
            int i = LivingRenderer.getPackedOverlay(entityIn, 0);
            entityModel.render(matrixStackIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        matrixStackIn.pop();
    }

    private static RenderType getRenderType(MultiLimbedModel entityModel, ClientPlayerEntity entityIn)
    {
        ResourceLocation resourcelocation = getSkin(entityIn);

        boolean invis = entityIn.isInvisible();
        boolean visible = !invis && !entityIn.isInvisibleToPlayer(Minecraft.getInstance().player);
        boolean glowing = entityIn.isGlowing();

        if (invis)
        {
            return RenderType.getItemEntityTranslucentCull(resourcelocation);
        }
        else if (visible)
        {
            return entityModel.getRenderType(resourcelocation);
        }
        else
        {
            return glowing ? RenderType.getOutline(resourcelocation) : null;
        }
    }

    private static ResourceLocation getSkin(ClientPlayerEntity EntityIn)
    {
        return EntityIn.getLocationSkin();
    }
}
