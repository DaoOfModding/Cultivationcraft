package DaoOfModding.Cultivationcraft.Client.Textures;

import DaoOfModding.mlmanimator.Client.MultiLimbedRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.UUID;

public class PlayerTextureManager
{
    private String testName = "";

    private HashMap<String, ResourceLocation> textureList = new HashMap<String, ResourceLocation>();

    Boolean changed = false;

    public PlayerTextureManager()
    {
    }

    // If the players skin has not been added to the texture manager, try to add it
    public void update(UUID playerID)
    {
        AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(playerID);

        testName = player.getDisplayName().getString();

        if (player.isSkinLoaded())
            addTexture(TextureList.skin, MultiLimbedRenderer.getSkin(player));
    }

    public boolean hasChanged()
    {
        return changed;
    }

    public void clearChanged()
    {
        changed = false;
    }

    public void addTexture(String textureID, ResourceLocation textureLocation)
    {
        // Do nothing if the added texture location already exists
        if (textureList.containsKey(textureID) && textureList.get(textureID) == textureLocation)
            return;

        System.out.println("Adding texture " + textureID + " for player " + testName + " as " + textureLocation.toString());

        changed = true;
        textureList.put(textureID, textureLocation);
    }

    public ResourceLocation getTexture(String textureID)
    {
        return textureList.get(textureID);
    }
}
