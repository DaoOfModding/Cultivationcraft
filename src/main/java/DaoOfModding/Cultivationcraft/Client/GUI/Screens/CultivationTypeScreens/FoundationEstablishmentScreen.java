package DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class FoundationEstablishmentScreen extends CultivationTypeScreen
{
    public FoundationEstablishmentScreen()
    {
        TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/foundationestablishment.png");

        xSize = 76;
        ySize = 115;
    }

    public void render(PoseStack PoseStack, float partialTicks, int xPos, int yPos, Screen screen, CultivationType cultivation)
    {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        float maxProgress = cultivation.getMaxStage() * cultivation.getMaxTechLevelWithoutPrevious();
        float currentProgress = cultivation.getTechLevelProgress(cultivation.getPassive().getClass());
        float height = currentProgress / maxProgress;

        screen.blit(PoseStack, xPos, yPos + (int)(ySize * (1 - height)), screen.getBlitOffset(), 0,  (int)(ySize * (1 - height)), xSize, (int)(ySize * height), xSize, ySize);
    }
}
