package DaoOfModding.Cultivationcraft.Client.Textures;

import DaoOfModding.mlmanimator.Client.MultiLimbedRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.UUID;

public class PlayerTextureManager
{
    private HashMap<String, String> modelTextureList = new HashMap<String, String>();
    private HashMap<String, ResourceLocation> textureList = new HashMap<String, ResourceLocation>();

    public PlayerTextureManager()
    {
    }

    // If the players skin has not been added to the texture manager, try to add it
    public void update(UUID playerID)
    {
        if (getTexture(TextureList.skin) == null)
        {
            AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) Minecraft.getInstance().level.getPlayerByUUID(playerID);

            if (player.isSkinLoaded())
                addTexture(TextureList.skin, MultiLimbedRenderer.getSkin(player));
        }
    }

    public void addModel(String modelID, String textureID)
    {
        modelTextureList.put(modelID, textureID);
    }

    public ResourceLocation getModel(String modelID)
    {
        String textureID = TextureList.skin;

        if (modelTextureList.containsKey(modelID))
            textureID = modelTextureList.get(modelID);

        return getTexture(textureID);
    }

    public void addTexture(String textureID, ResourceLocation textureLocation)
    {
        textureList.put(textureID, textureLocation);
    }

    public ResourceLocation getTexture(String textureID)
    {
        return textureList.get(textureID);
    }
}
