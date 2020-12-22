package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.ExtendableModelRenderer;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericResizers;
import net.minecraft.client.renderer.entity.model.PlayerModel;

import java.util.HashMap;

public class BodyPartModels
{
    HashMap<String, ExtendableModelRenderer> models = new HashMap<String, ExtendableModelRenderer>();

    public BodyPartModels(PlayerModel base)
    {
        ExtendableModelRenderer testPart = new ExtendableModelRenderer(base, 40, 16);
        testPart.setRotationPoint(-5.0F, 2.0F, 0.0F);
        testPart.extend(GenericResizers.getRightArmResizer());

        addModel(BodyPartNames.TestPart, BodyPartNames.armPosition, testPart);
    }

    public void addModel(String ID, String limbPosition, ExtendableModelRenderer model)
    {
        models.put(ID, model);
    }

    public ExtendableModelRenderer getModel(String ID)
    {
        return models.get(ID);
    }
}
