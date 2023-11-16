package DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class CultivationTypeScreen
{
    ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/stats.png");

    int xSize = 1;
    int ySize = 1;

    public void render(PoseStack PoseStack, float partialTicks, int xPos, int yPos, Screen screen, CultivationType cultivation, Font font)
    {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        screen.blit(PoseStack, xPos, yPos, screen.getBlitOffset(), 0, 0, xSize, ySize, xSize, ySize);
    }
}
