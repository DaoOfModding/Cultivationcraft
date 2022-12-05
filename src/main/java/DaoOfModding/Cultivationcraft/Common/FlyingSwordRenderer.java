package DaoOfModding.Cultivationcraft.Common;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;

public class FlyingSwordRenderer extends EntityRenderer<FlyingSwordEntity>
{
    private final ItemRenderer itemRenderer;
    private final Random random = new Random();

    public FlyingSwordRenderer(EntityRendererProvider.Context renderManagerIn)
    {
        super(renderManagerIn);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    protected int getModelCount(ItemStack stack) {
        int i = 1;
        if (stack.getCount() > 48) {
            i = 5;
        } else if (stack.getCount() > 32) {
            i = 4;
        } else if (stack.getCount() > 16) {
            i = 3;
        } else if (stack.getCount() > 1) {
            i = 2;
        }

        return i;
    }

    public void render(FlyingSwordEntity entityIn, float entityYaw, float partialTicks, PoseStack PoseStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
        PoseStackIn.pushPose();
        ItemStack itemstack = entityIn.getItem();
        int i = itemstack.isEmpty() ? 187 : Item.getId(itemstack.getItem()) + itemstack.getDamageValue();
        this.random.setSeed((long)i);
        BakedModel ibakedmodel = this.itemRenderer.getModel(itemstack, entityIn.level, (LivingEntity)null, 0);
        boolean flag = ibakedmodel.isGui3d();
        int j = this.getModelCount(itemstack);
        float f = 0.25F;

        // Bobbing up and down
        float f1 = 0;

        // Only bob if Flying Sword is in control range
        if (entityIn.isInRange())
            f1 = shouldBob() ? Mth.sin(((float)entityIn.getAge() + partialTicks) / 10.0F + entityIn.bobOffs) * 0.1F + 0.1F : 0;


        float f2 = ibakedmodel.getTransforms().getTransform(ItemTransforms.TransformType.GROUND).scale.y();
        PoseStackIn.translate(0.0D, (double)(f1 + 0.25F * f2), 0.0D);


        // Rotate model to match items pitch and yaw
        PoseStackIn.mulPose(Vector3f.YP.rotation(entityIn.getYRot()));
        PoseStackIn.mulPose(Vector3f.ZP.rotation(entityIn.getXRot()));

        if (!flag) {
            float f7 = -0.0F * (float)(j - 1) * 0.5F;
            float f8 = -0.0F * (float)(j - 1) * 0.5F;
            float f9 = -0.09375F * (float)(j - 1) * 0.5F;
            PoseStackIn.translate((double)f7, (double)f8, (double)f9);
        }

        for(int k = 0; k < j; ++k) {
            PoseStackIn.pushPose();
            if (k > 0) {
                if (flag) {
                    float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    PoseStackIn.translate(shouldSpreadItems() ? f11 : 0, shouldSpreadItems() ? f13 : 0, shouldSpreadItems() ? f10 : 0);
                } else {
                    float f12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float f14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    PoseStackIn.translate(shouldSpreadItems() ? f12 : 0, shouldSpreadItems() ? f14 : 0, 0.0D);
                }
            }

            this.itemRenderer.render(itemstack, ItemTransforms.TransformType.GROUND, false, PoseStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, ibakedmodel);
            PoseStackIn.popPose();
            if (!flag) {
                PoseStackIn.translate(0.0, 0.0, 0.09375F);
            }
        }

        PoseStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, PoseStackIn, bufferIn, packedLightIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(FlyingSwordEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
    /*==================================== FORGE START ===========================================*/

    /**
     * @return If items should spread out when rendered in 3D
     */
    public boolean shouldSpreadItems() {
        return false;
    }

    /**
     * @return If items should have a bob effect
     */
    public boolean shouldBob() {
        return true;
    }
    /*==================================== FORGE END =============================================*/
}