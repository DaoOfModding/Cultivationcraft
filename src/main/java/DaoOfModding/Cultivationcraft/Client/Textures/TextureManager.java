package DaoOfModding.Cultivationcraft.Client.Textures;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModels;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TextureManager
{
    private static HashMap<UUID, PlayerTextureManager> playerTextures = new HashMap<UUID, PlayerTextureManager>();

    private static void addPlayer(UUID playerID)
    {
        playerTextures.put(playerID, new PlayerTextureManager(playerID));
    }

    private static PlayerTextureManager getTextureManager(UUID playerID)
    {
        if (!playerTextures.containsKey(playerID))
            addPlayer(playerID);

        return playerTextures.get(playerID);
    }

    public static void addTexture(UUID playerID, String textureID, ResourceLocation textureLocation)
    {
        getTextureManager(playerID).addTexture(textureID, textureLocation);
    }

    // Update the textures for each model
    public static void updateTextures(PlayerEntity player)
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);
        PlayerTextureManager manager = getTextureManager(player.getUniqueID());

        // Loop through each body part modification for this player and set the appropriate texture
        for (BodyPart part : modifications.getModifications().values())
        {
            ResourceLocation texture = manager.getTexture(part.getTextureID());

            for (String modelID : part.getModelIDs())
                BodyPartModels.getModel(modelID).setCustomTextureForFamily(texture);

            // Loop through each body part option for this modification and set the appropriate texture
            for (BodyPartOption option : modifications.getModificationOptions(part.getPosition()).values())
            {
                ResourceLocation texture2 = manager.getTexture(option.getTextureID());

                for (ArrayList<String> models : option.getOptionModels().values())
                        for (String modelID : models)
                            BodyPartModels.getModel(modelID).setCustomTextureForFamily(texture2);
            }
        }
    }
}
