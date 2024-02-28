package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Common.Qi.QiProjectile;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class QiProjectileRenderer extends EntityRenderer<QiProjectile>
{
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(Cultivationcraft.MODID, "textures/particle/qiparticlefx.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucentEmissive(TEXTURE_LOCATION);

    public QiProjectileRenderer(EntityRendererProvider.Context renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void render(QiProjectile p_114080_, float p_114081_, float p_114082_, PoseStack p_114083_, MultiBufferSource p_114084_, int p_114085_)
    {
        int alpha = (int)(p_114080_.getAlpha() * 255f);

        p_114083_.pushPose();
        p_114083_.scale(1.0F, 1.0F, 1.0F);
        p_114083_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        p_114083_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        PoseStack.Pose posestack$pose = p_114083_.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = p_114084_.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 0, 0, 1, alpha);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 0, 1, 1, alpha);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 1, 1, 0, alpha);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 1, 0, 0, alpha);
        p_114083_.popPose();
        super.render(p_114080_, p_114081_, p_114082_, p_114083_, p_114084_, p_114085_);
    }

    private static void vertex(VertexConsumer p_114090_, Matrix4f p_114091_, Matrix3f p_114092_, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_, int alpha) {
        p_114090_.vertex(p_114091_, p_114094_ - 0.5F, (float)p_114095_ - 0.25F, 0.0F).color(255, 255, 255, alpha).uv((float)p_114096_, (float)p_114097_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114093_).normal(p_114092_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(QiProjectile p_114078_) {
        return TEXTURE_LOCATION;
    }
}
