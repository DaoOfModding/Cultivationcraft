package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.ExtendableModelRenderer;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.defaultResizeModule;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashMap;

public class BodyPartModels
{
    private static HashMap<String, ExtendableModelRenderer> models = new HashMap<String, ExtendableModelRenderer>();
    private static HashMap<String, HashMap<String, ExtendableModelRenderer>> nonRenderingModels = new HashMap<String, HashMap<String, ExtendableModelRenderer>>();

    public static void setupModels()
    {
        setupHeadModels();
        setupLegModels();
    }

    private static void setupHeadModels()
    {
        defaultResizeModule semiHeadResizer = new defaultResizeModule(1, new Vector3d(0, 1, 0), new Vector3d(-4, -8, -4), new Vector3d(8, 5, 8), new Vector3d(0, 1, 1));
        defaultResizeModule jawResizer = new defaultResizeModule(1, new Vector3d(0, 0, 0), new Vector3d(-4, -1, -6), new Vector3d(8, 3, 8), new Vector3d(0, 1, 1));

        // Create the top half of the head
        ExtendableModelRenderer semiHeadPart = new ExtendableModelRenderer(64, 64, 0, 0);
        semiHeadPart.setRotationPoint(0.0F, 0.0F, 0.0F);
        semiHeadPart.extend(semiHeadResizer);
        semiHeadPart.setLooking(true);

        // Create the jaw
        ExtendableModelRenderer jawPart = new ExtendableModelRenderer(64, 64, 0, 5);
        jawPart.setRotationPoint(0.0F, -2F, 2F);
        jawPart.extend(jawResizer);

        // Attach the jaw to the head
        semiHeadPart.addChild(jawPart);

        addModel(BodyPartModelNames.jawModel, semiHeadPart);
        addReference(BodyPartModelNames.jawModel, BodyPartModelNames.jawModelLower, jawPart);
    }

    private static void setupLegModels()
    {
        defaultResizeModule reverseJointResizer = new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(-1.99, 0, -1.99), new Vector3d(3.98, 10, 3.98), new Vector3d(0, 1, 1));
        defaultResizeModule footResizer = new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(-1.99, 0, 0.01), new Vector3d(4, 2, 4), new Vector3d(0, 1, -1));

        ExtendableModelRenderer leftReverseJoint = new ExtendableModelRenderer(64, 64, 0, 16);
        leftReverseJoint.setRotationPoint(1.9F, 11.0F, 0.0F);
        leftReverseJoint.extend(reverseJointResizer);
        leftReverseJoint.mirror = true;

        ExtendableModelRenderer leftFoot = new ExtendableModelRenderer(64, 64, 0, 26);
        leftFoot.setRotationPoint(0F, 5.0F, -4F);
        leftFoot.extend(footResizer);
        leftFoot.mirror = true;

        leftReverseJoint.getChildren().get(0).addChild(leftFoot);

        // Reinitialize the resizers
        reverseJointResizer = new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(-1.99, 0, -1.99), new Vector3d(3.98, 10, 3.98), new Vector3d(0, 1, 1));
        footResizer = new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(-1.99, 0, 0.01), new Vector3d(4, 2, 4), new Vector3d(0, 1, -1));

        ExtendableModelRenderer rightReverseJoint = new ExtendableModelRenderer(64, 64, 0, 16);
        rightReverseJoint.setRotationPoint(-1.9F, 11.0F, 0.0F);
        rightReverseJoint.extend(reverseJointResizer);

        ExtendableModelRenderer rightFoot = new ExtendableModelRenderer(64, 64, 0, 26);
        rightFoot.setRotationPoint(0F, 5.0F, -4F);
        rightFoot.extend(footResizer);

        rightReverseJoint.getChildren().get(0).addChild(rightFoot);

        addModel(BodyPartModelNames.reverseJointLeftLegModel, leftReverseJoint);
        addReference(BodyPartModelNames.reverseJointLeftLegModel, BodyPartModelNames.reverseJointLeftLegLowerModel, leftReverseJoint.getChildren().get(0));
        addReference(BodyPartModelNames.reverseJointLeftLegModel, BodyPartModelNames.reverseJointLeftFootModel, leftFoot);

        addModel(BodyPartModelNames.reverseJointRightLegModel, rightReverseJoint);
        addReference(BodyPartModelNames.reverseJointRightLegModel, BodyPartModelNames.reverseJointRightLegLowerModel, rightReverseJoint.getChildren().get(0));
        addReference(BodyPartModelNames.reverseJointRightLegModel, BodyPartModelNames.reverseJointRightFootModel, rightFoot);
    }

    public static void addModel(String ID, ExtendableModelRenderer model)
    {
        models.put(ID, model);
    }

    public static void addReference(String ID, String referenceID, ExtendableModelRenderer model)
    {
        if (!nonRenderingModels.containsKey(ID))
            nonRenderingModels.put(ID, new HashMap<String, ExtendableModelRenderer>());

        nonRenderingModels.get(ID).put(referenceID, model);
    }

    public static HashMap<String, ExtendableModelRenderer> getReferences(String ID)
    {
        if (!nonRenderingModels.containsKey(ID))
            nonRenderingModels.put(ID, new HashMap<String, ExtendableModelRenderer>());

        return nonRenderingModels.get(ID);
    }

    public static ExtendableModelRenderer getModel(String ID)
    {
        return models.get(ID);
    }
}
