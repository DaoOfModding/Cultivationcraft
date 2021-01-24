package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.ExtendableModelRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;

import java.util.HashMap;
import java.util.UUID;

public class BodyPartList
{
    static HashMap<UUID, BodyPartModels> models = new HashMap<UUID, BodyPartModels>();

    public static void addPlayer(UUID playerID, PlayerModel model)
    {
        models.put(playerID, new BodyPartModels(model));
    }

    public static boolean playerExists(UUID playerID)
    {
        return models.containsKey(playerID);
    }

    public static ExtendableModelRenderer getModel(UUID playerID, String ID)
    {
        return models.get(playerID).getModel(ID);
    }

    public static HashMap<String, ExtendableModelRenderer> getModelReferences(UUID playerID, String ID)
    {
        return models.get(playerID).getReferences(ID);
    }
}
