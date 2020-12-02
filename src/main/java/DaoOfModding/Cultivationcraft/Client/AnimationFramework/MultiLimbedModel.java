package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector3d;

import java.util.*;

public class MultiLimbedModel
{
    PlayerModel baseModel;

    HashMap<String, ModelRenderer> limbs = new HashMap<String, ModelRenderer>();

    public MultiLimbedModel(PlayerModel model)
    {
        baseModel = model;

        addLimb("LEFTARM", baseModel.bipedLeftArm);
        addLimb("RIGHTARM", baseModel.bipedRightArm);
        addLimb("LEFTLEG", baseModel.bipedLeftLeg);
        addLimb("RIGHTLEG", baseModel.bipedRightLeg);
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
        limbs.put(limb, limbModel);
    }
}
