package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class TechniqueIcons
{
    protected static final ResourceLocation HIGHLIGHT = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/highlight.png");
    protected static final ResourceLocation COOLDOWN = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/cooldown.png");


    // Loop through all player icons and draw them from the supplied coordinates
    // Remember to rebind texture aftwards
    public static void renderIcons(PoseStack stack, int xpos, int ypos, GuiComponent gui, int spacing)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer());

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (techs.getTechnique(i) != null)
            {
                RenderSystem.setShaderTexture(0, techs.getTechnique(i).getIcon());

                gui.blit(stack, xpos + i * spacing, ypos, gui.getBlitOffset(), 0, 0, 16, 16, 16, 16);
            }
    }

    // Highlight all techniques that are currently active
    public static void highlightActive(PoseStack stack, int xpos, int ypos, GuiComponent gui, int spacing)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer());

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (techs.getTechnique(i) != null && techs.getTechnique(i).isActive())
                highlightIcon(stack, xpos, ypos, gui, spacing, i);
    }

    // Highlight all techniques that are currently active
    public static void showCooldowns(PoseStack stack, int xpos, int ypos, GuiComponent gui, int spacing)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer());

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (techs.getTechnique(i) != null && techs.getTechnique(i).getCooldown() > 0)
                cooldownIcon(stack, xpos, ypos, gui, spacing, i);
    }

    // Returns the technique number the mouse is over
    // Returns -1 if mouse isn't over a technique
    public static int mouseOver(int xpos, int ypos, int mouseX, int mouseY, int spacing)
    {
        if (mouseY < ypos || mouseY > ypos + 16)
            return  -1;

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (mouseX >= xpos + i * spacing && mouseX <= xpos + i * spacing + 16)
                return i;

        return -1;
    }

    public static void mouseOverHighlight(PoseStack stack, int xpos, int ypos, GuiComponent gui, int spacing, int mouseX, int mouseY)
    {
        int mouseOver = mouseOver(xpos, ypos, mouseX, mouseY, spacing);

        if (mouseOver == -1)
            return;

        highlightIcon(stack, xpos, ypos, gui, spacing, mouseOver);
    }

    // Highlight the selected icon
    // Remember to rebind texture afterwards
    public static void highlightIcon(PoseStack stack, int xpos, int ypos, GuiComponent gui, int spacing, int icon)
    {
        RenderSystem.enableBlend();

        RenderSystem.setShaderTexture(0, HIGHLIGHT);

        // Set the texture to be semi-transparent
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);

        gui.blit(stack, xpos + icon * spacing, ypos, gui.getBlitOffset(), 0, 0, 16, 16, 16, 16);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
    }

    // Darken the selected icon
    // Remember to rebind texture afterwards
    public static void cooldownIcon(PoseStack stack, int xpos, int ypos, GuiComponent gui, int spacing, int icon)
    {
        RenderSystem.enableBlend();

        RenderSystem.setShaderTexture(0, COOLDOWN);

        // Set the texture to be semi-transparent
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);

        gui.blit(stack, xpos + icon * spacing, ypos, gui.getBlitOffset(), 0, 0, 16, 16, 16, 16);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);

        // TODO: Display how long left on cooldown?
    }
}
