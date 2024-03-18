package DaoOfModding.Cultivationcraft.Client.Textures;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class initTextures
{
    // Initialise special texture types into the texture manager
    public static void init()
    {
        ResourceLocation texturealpha = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/qicondenser.png");
        ResourceLocation texture = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/foundationestablishment.png");

        AlphaOverlayTexture tex = new AlphaOverlayTexture(texture, texturealpha);
        Minecraft.getInstance().getTextureManager().register(texturealpha, tex);
    }
}
