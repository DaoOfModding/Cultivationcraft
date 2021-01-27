package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

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

    ExtendableModelRenderer body;
    HashMap<String, ExtendableModelRenderer> limbs = new HashMap<String, ExtendableModelRenderer>();

    private boolean lock = false;

    public MultiLimbedModel(PlayerModel model)
    {
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
        //TODO: Setup armor models

        ExtendableModelRenderer body = new ExtendableModelRenderer(baseModel, 16, 16);
        body.setRotationPoint(0, 0, 0);
        body.extend(GenericResizers.getBodyResizer());

        ExtendableModelRenderer head = new ExtendableModelRenderer(baseModel, 0, 0);
        head.setRotationPoint(0, 0, 0);
        head.extend(GenericResizers.getHeadResizer());
        head.setLooking(true);

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

        addBody(body);
        addLimb(GenericLimbNames.head, head);
        addLimb(GenericLimbNames.leftArm, leftArm);
        addLimb(GenericLimbNames.rightArm, rightArm);
        addLimb(GenericLimbNames.leftLeg, leftLeg);
        addLimb(GenericLimbNames.rightLeg, rightLeg);
        addLimbReference(GenericLimbNames.lowerLeftArm, leftArm.getChildren().get(0));
        addLimbReference(GenericLimbNames.lowerRightArm, rightArm.getChildren().get(0));
        addLimbReference(GenericLimbNames.lowerLeftLeg, leftLeg.getChildren().get(0));
        addLimbReference(GenericLimbNames.lowerRightLeg, rightLeg.getChildren().get(0));
    }

    // Stop multi-threading breaking stuff via locking
    private void lock()
    {
        while (lock) {}

        lock = true;
    }

    private void unlock()
    {
        lock = false;
    }

    // Returns a list of all limbs on this model
    public Set<String> getLimbs()
    {
        return limbs.keySet();
    }

    public ExtendableModelRenderer getBody()
    {
        return body;
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

    public ExtendableModelRenderer getLimb(String limb)
    {
        return limbs.get(limb);
    }

    public void addBody(ExtendableModelRenderer bodyModel)
    {
        lock();

        limbs.put(GenericLimbNames.body, bodyModel);

        if (body != null)
            body.fosterChildren(bodyModel);

        body = bodyModel;

        unlock();
    }

    // Adds specified limb onto the body
    public void addLimb(String limb, ExtendableModelRenderer limbModel)
    {
        lock();

        body.addChild(limbModel);
        addLimbReference(limb, limbModel);

        unlock();
    }

    public void removeLimb(String limb)
    {
        lock();

        body.removeChild(limbs.get(limb));
        limbs.remove(limb);

        unlock();
    }

    // Add a limb for reference purposes
    // Usually used for referencing child limbs
    public void addLimbReference(String limb, ExtendableModelRenderer limbModel)
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

    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        lock();

        matrixStackIn.push();

        // Scale the model to match the scale size, and move it up or down so it's standing at the right height
        matrixStackIn.translate(0.0D, (1-sizeScale) * defaultHeight, 0.0D);
        matrixStackIn.scale(sizeScale, sizeScale, sizeScale);

        // Render the body, as all limbs are children or sub-children of the body, this should render everything
        body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        // TODO: Reverse head rotations (Probably by applying body rotations *-1 to the head rotation

        matrixStackIn.pop();

        unlock();
    }

    // Calculate the height adjustment for each limb
    public void calculateHeightAdjustment()
    {
        MatrixStack stack = new MatrixStack();

        body.calculateMinHeight(stack);
    }

    // Find the limb at the lowest height and return it's height
    public float getHeightAdjustment()
    {
        float lowest = getHeightAdjustment(body, Float.MAX_VALUE * -1);

        return lowest * sizeScale / 16;
    }

    // Find the minimum height of the provided limb and all of it's children compared to the provided value
    private float getHeightAdjustment(ExtendableModelRenderer limbModel, float lowest)
    {
        float testHeight = limbModel.getMinHeight();

        if (testHeight > lowest)
            lowest = testHeight;

        for (ExtendableModelRenderer testLimb : limbModel.getChildren())
            lowest = getHeightAdjustment(testLimb, lowest);

        return lowest;
    }
}
