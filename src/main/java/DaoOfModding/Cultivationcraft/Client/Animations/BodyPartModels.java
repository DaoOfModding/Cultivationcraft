package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.ExtendableModelRenderer;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.defaultResizeModule;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashMap;

public class BodyPartModels
{
    HashMap<String, ExtendableModelRenderer> models = new HashMap<String, ExtendableModelRenderer>();
    HashMap<String, HashMap<String, ExtendableModelRenderer>> nonRenderingModels = new HashMap<String, HashMap<String, ExtendableModelRenderer>>();

    public BodyPartModels(PlayerModel base)
    {
        setupHeadModels(base);
    }

    private void setupHeadModels(PlayerModel base)
    {
        defaultResizeModule semiHeadResizer = new defaultResizeModule(1, new Vector3d(0, 1, 0), new Vector3d(-4, -8, -4), new Vector3d(8, 5, 8), new Vector3d(0, 1, 1));
        defaultResizeModule jawResizer = new defaultResizeModule(1, new Vector3d(0, 0, 0), new Vector3d(-4, -1, -6), new Vector3d(8, 3, 8), new Vector3d(0, 1, 1));

        // Create the top half of the head
        ExtendableModelRenderer semiHeadPart = new ExtendableModelRenderer(base, 0, 0);
        semiHeadPart.setRotationPoint(0.0F, 0.0F, 0.0F);
        semiHeadPart.extend(semiHeadResizer);

        // Create the jaw
        ExtendableModelRenderer jawPart = new ExtendableModelRenderer(base, 0, 5);
        jawPart.setRotationPoint(0.0F, -2F, 2F);
        jawPart.extend(jawResizer);

        // Attach the jaw to the head
        semiHeadPart.addChild(jawPart);

        addModel(BodyPartModelNames.jawModel, semiHeadPart);
        addReference(BodyPartModelNames.jawModel, BodyPartModelNames.jawModelLower, jawPart);
    }

    public void addModel(String ID, ExtendableModelRenderer model)
    {
        models.put(ID, model);
    }

    public void addReference(String ID, String referenceID, ExtendableModelRenderer model)
    {
        if (!nonRenderingModels.containsKey(ID))
            nonRenderingModels.put(ID, new HashMap<String, ExtendableModelRenderer>());

        nonRenderingModels.get(ID).put(referenceID, model);
    }

    public HashMap<String, ExtendableModelRenderer> getReferences(String ID)
    {
        if (!nonRenderingModels.containsKey(ID))
            nonRenderingModels.put(ID, new HashMap<String, ExtendableModelRenderer>());

        return nonRenderingModels.get(ID);
    }

    public ExtendableModelRenderer getModel(String ID)
    {
        return models.get(ID);
    }
}
