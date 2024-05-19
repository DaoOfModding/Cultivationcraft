package DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens;

import DaoOfModding.Cultivationcraft.Client.ClientListeners;
import DaoOfModding.Cultivationcraft.Client.GUI.GUIButton;
import DaoOfModding.Cultivationcraft.Client.GUI.Screens.ConceptScreen;
import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationModifyScreen;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CoreFormingScreen extends CultivationTypeScreen
{
    protected GUIButton conceptButton;

    final int midX = 38;
    final int midY = 56;

    final float coreRotationDiameter = 12;
    final float coreRotationSpeed = 0.25f;

    final int conceptButtonX = 60;
    final int conceptButtonY = 140;

    public CoreFormingScreen()
    {
        xSize = 10;
        ySize = 10;

        conceptButton = new GUIButton("CONCEPT", Component.translatable("cultivationcraft.gui.concept").getString());
    }

    public void renderButtons(PoseStack poseStack, int xPos, int yPos, int mouseX, int mouseY, GuiComponent gui)
    {
        conceptButton.setPos(xPos + conceptButtonX, yPos + conceptButtonY);
        conceptButton.render(poseStack, mouseX, mouseY, gui);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (conceptButton.mouseClick((int)mouseX, (int)mouseY, buttonPressed))
        {
            conceptButton.unselect();
            Minecraft.getInstance().forceSetScreen(new ConceptScreen());
            return true;
        }

        return false;
    }

    public void render(PoseStack PoseStack, float partialTicks, int xPos, int yPos, Screen screen, CultivationType cultivation, Font font)
    {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        CultivationType cult = cultivation;
        int coreNumber = 0;
        double maxCore = cult.getStage();

        // Stop any division by zero errors
        if (maxCore > 1)
            maxCore -= 1;

        while (cult instanceof CoreFormingCultivation)
        {
            renderCore(PoseStack, partialTicks, xPos + midX - (xSize / 2), yPos + midY, screen, ((CoreFormingCultivation) cult).getFocus(), coreNumber / maxCore);

            coreNumber++;
            cult = cult.getPreviousCultivation();
        }
    }

    public void renderCore(PoseStack PoseStack, float partialTicks, float xPos, float yPos, Screen screen, TechniqueModifier focus, double rotationPos)
    {
        // Rotate the core if it is not the first one
        if (rotationPos > 0)
        {
            // Determine the starting angle of the core
            rotationPos *= 360;
            // Move the angle based on the current tick
            rotationPos = (rotationPos + (((ClientListeners.tick + partialTicks) * coreRotationSpeed) % 360.0)) % 360.0;

            // Convert angle to radians
            rotationPos = Math.toRadians(rotationPos);

            xPos += Math.cos(rotationPos) * coreRotationDiameter;
            yPos += Math.sin(rotationPos) * coreRotationDiameter;
        }

        focus.getCoreTexture().render(PoseStack, xPos, yPos, xSize, ySize, screen.getBlitOffset());
    }
}
