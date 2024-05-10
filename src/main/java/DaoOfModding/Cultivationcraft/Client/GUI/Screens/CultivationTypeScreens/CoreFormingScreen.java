package DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens;

import DaoOfModding.Cultivationcraft.Client.Textures.AlphaOverlayTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class CoreFormingScreen extends CultivationTypeScreen
{
    public CoreFormingScreen()
    {
        TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/qicondenser.png");

        xSize = 76;
        ySize = 115;
    }

    public void render(PoseStack PoseStack, float partialTicks, int xPos, int yPos, Screen screen, CultivationType cultivation, Font font)
    {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        CultivationType cult = cultivation;
        int coreNumber = 0;

        while (cult instanceof CoreFormingCultivation)
        {
            renderCore(PoseStack, partialTicks, xPos, yPos, screen, ((CoreFormingCultivation) cult).getFocus(), coreNumber);

            coreNumber++;
            cult = cult.getPreviousCultivation();
        }
    }

    public void renderCore(PoseStack PoseStack, float partialTicks, int xPos, int yPos, Screen screen, TechniqueModifier focus, int number)
    {
        ((AlphaOverlayTexture) Minecraft.getInstance().getTextureManager().getTexture(TEXTURE)).setAlphaSize(0.1f);

        // TODO: Place core in position relative to coreNumber (possibly rotating)
        // TODO: Add core texture based of TechniqueModifier
        screen.blit(PoseStack, xPos, yPos, screen.getBlitOffset(), 0,  0, xSize, ySize, xSize, ySize);
    }
}
