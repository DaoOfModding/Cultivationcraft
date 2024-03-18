package DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens;

import DaoOfModding.Cultivationcraft.Client.Textures.AlphaOverlayTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.QiCondenserCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class QiCondenserScreen extends CultivationTypeScreen
{
    public QiCondenserScreen()
    {
        TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/qicondenser.png");

        xSize = 76;
        ySize = 115;
    }

    public void render(PoseStack PoseStack, float partialTicks, int xPos, int yPos, Screen screen, CultivationType cultivation, Font font)
    {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        float progressInterval = 1f / cultivation.getMaxStage();
        float currentProgress = (float)cultivation.getTechLevelProgressWithoutPrevious(cultivation.getPassive().getClass().toString()) / (float)cultivation.getMaxTechLevelWithoutPrevious();
        currentProgress *= progressInterval;
        currentProgress += progressInterval * (cultivation.getStage() - 1);

        currentProgress *= 0.9;

        ((AlphaOverlayTexture)Minecraft.getInstance().getTextureManager().getTexture(TEXTURE)).setAlphaSize(1f - currentProgress);

        screen.blit(PoseStack, xPos, yPos, screen.getBlitOffset(), 0,  0, xSize, ySize, xSize, ySize);

        ResourceLocation element = ((QiCondenserCultivation) cultivation).getCurrentElementFocus();
        int percent = (int)(((QiCondenserCultivation) cultivation).getElementFocusAmount(element) * 100);

        String text = Elements.getElement(element).getName() + " " + percent + "%";

        font.draw(PoseStack, text, xPos + xSize / 2 - font.width(text), yPos + ySize + 5, 0);
    }
}
