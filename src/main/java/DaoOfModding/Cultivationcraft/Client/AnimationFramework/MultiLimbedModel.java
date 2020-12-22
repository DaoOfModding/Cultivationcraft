package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.util.*;

public class MultiLimbedModel
{
    private float sizeScale = 1;
    private double defaultHeight = 1.5;

    PlayerModel baseModel;

    HashMap<String, ModelRenderer> limbs = new HashMap<String, ModelRenderer>();
    ArrayList<String> toRender = new ArrayList<String>();

    public MultiLimbedModel(PlayerModel model)
    {
        // TODO: Setup so armor displays on player
        baseModel = model;

        setupDefaultLimbs();
    }

    public MultiLimbedModel(PlayerModel model, boolean withLimbs)
    {
        baseModel = model;

        if (withLimbs)
            setupDefaultLimbs();
    }

    private void setupDefaultLimbs()
    {
        ExtendableModelRenderer rightArm = new ExtendableModelRenderer(baseModel, 40, 16);
        rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        rightArm.extend(GenericResizers.getRightArmResizer());

        ExtendableModelRenderer leftArm = new ExtendableModelRenderer(baseModel, 32, 48);
        leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        leftArm.extend(GenericResizers.getLeftArmResizer());
        leftArm.mirror = true;

        ExtendableModelRenderer rightLeg = new ExtendableModelRenderer(baseModel, 0, 16);
        rightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        rightLeg.extend(GenericResizers.getRightLegResizer());

        ExtendableModelRenderer leftLeg = new ExtendableModelRenderer(baseModel, 0, 16);
        leftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        leftLeg.extend(GenericResizers.getLeftLegResizer());
        leftLeg.mirror = true;

        addLimb(GenericLimbNames.leftArm, leftArm);
        addLimb(GenericLimbNames.rightArm, rightArm);
        addLimb(GenericLimbNames.leftLeg, leftLeg);
        addLimb(GenericLimbNames.rightLeg, rightLeg);
        addNonRenderingLimb(GenericLimbNames.lowerLeftArm, leftArm.getChild(1));
        addNonRenderingLimb(GenericLimbNames.lowerRightArm, rightArm.getChild(1));
        addNonRenderingLimb(GenericLimbNames.lowerLeftLeg, leftLeg.getChild(1));
        addNonRenderingLimb(GenericLimbNames.lowerRightLeg, rightLeg.getChild(1));
    }

    // Returns a list of all limbs on this model
    public Set<String> getLimbs()
    {
        return limbs.keySet();
    }

    // Apply the supplied rotations to the specified limb
    public void rotateLimb(String limb, Vector3d angles)
    {
        if (hasLimb(limb))
        {
            ModelRenderer limbModel = getLimb(limb);

            limbModel.rotateAngleX = (float) angles.x;
            limbModel.rotateAngleY = (float) angles.y;
            limbModel.rotateAngleZ = (float) angles.z;
        }
    }

    // Returns true if this model contains the specified limb
    public boolean hasLimb(String limb)
    {
        return limbs.containsKey(limb);
    }

    public ModelRenderer getLimb(String limb)
    {
        return limbs.get(limb);
    }

    public void addLimb(String limb, ModelRenderer limbModel)
    {
        toRender.add(limb);
        limbs.put(limb, limbModel);
    }

    public void removeLimb(String limb)
    {
        toRender.remove(limb);
        limbs.remove(limb);
    }

    // Add a limb for reference purposes, don't render it
    // Usually used for referencing child limbs
    public void addNonRenderingLimb(String limb, ModelRenderer limbModel)
    {
        limbs.put(limb, limbModel);
    }

    public void setLivingAnimations(PlayerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick)
    {
        baseModel.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    public void setRotationAngles(PlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        baseModel.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public RenderType getRenderType(ResourceLocation resourcelocation)
    {
        return baseModel.getRenderType(resourcelocation);
    }

    public boolean isRenderingLimb(String limb)
    {
        if (toRender.contains(limb))
            return true;

        return false;
    }

    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        matrixStackIn.push();

        // Scale the model to match the scale size, and move it up or down so it's standing at the right height
        matrixStackIn.translate(0.0D, (1-sizeScale) * defaultHeight, 0.0D);
        matrixStackIn.scale(sizeScale, sizeScale, sizeScale);

        baseModel.bipedHead.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        baseModel.bipedBody.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        for (Map.Entry<String, ModelRenderer> limb: limbs.entrySet())
            if (isRenderingLimb(limb.getKey()))
                limb.getValue().render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        baseModel.bipedHeadwear.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        matrixStackIn.pop();
    }

    // TODO: Fix this
    public double getHeightAdjustment()
    {
        double MaxAdjustment = defaultHeight / 2.3 * sizeScale;

        ModelRenderer LeftLeg = getLimb(GenericLimbNames.leftLeg);
        ModelRenderer RightLeg = getLimb(GenericLimbNames.rightLeg);

        if (LeftLeg == null)
            return 0;
        if (RightLeg == null)
            return 0;

        // Get the largest angle change for both legs
        float largestLeft = Math.abs(LeftLeg.rotateAngleX);
        /*if (largestLeft < Math.abs(LeftLeg.rotateAngleZ))
            largestLeft = Math.abs(LeftLeg.rotateAngleZ);*/

        float largestRight = Math.abs(RightLeg.rotateAngleX);
        /*if (largestRight < Math.abs(RightLeg.rotateAngleZ))
            largestRight = Math.abs(RightLeg.rotateAngleZ);*/

        // Determine which leg has the smallest angle change
        float smallest = largestLeft;

        if (smallest > largestRight)
            smallest = largestRight;

        if (smallest >= Math.toRadians(90))
            return MaxAdjustment;

        return Math.pow(smallest / Math.toRadians(90), 2) * MaxAdjustment;
    }
}
