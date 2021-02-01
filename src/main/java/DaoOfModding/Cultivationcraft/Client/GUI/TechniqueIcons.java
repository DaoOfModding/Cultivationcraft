package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

public class TechniqueIcons
{
    private static final ResourceLocation HIGHLIGHT = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/highlight.png");
    private static final ResourceLocation COOLDOWN = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/cooldown.png");


    // Loop through all player icons and draw them from the supplied coordinates
    // Remember to rebind texture aftwards
    public static void renderIcons(MatrixStack stack, int xpos, int ypos, AbstractGui gui, int spacing)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (techs.getTechnique(i) != null)
            {
                Minecraft.getInstance().getTextureManager().bindTexture(techs.getTechnique(i).getIcon());

                gui.blit(stack, xpos + i * spacing, ypos, gui.getBlitOffset(), 0, 0, 16, 16, 16, 16);
            }
    }

    // Highlight all techniques that are currently active
    public static void highlightActive(MatrixStack stack, int xpos, int ypos, AbstractGui gui, int spacing)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (techs.getTechnique(i) != null && techs.getTechnique(i).isActive())
                highlightIcon(stack, xpos, ypos, gui, spacing, i);
    }

    // Highlight all techniques that are currently active
    public static void showCooldowns(MatrixStack stack, int xpos, int ypos, AbstractGui gui, int spacing)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

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

    public static void mouseOverHighlight(MatrixStack stack, int xpos, int ypos, AbstractGui gui, int spacing, int mouseX, int mouseY)
    {
        int mouseOver = mouseOver(xpos, ypos, mouseX, mouseY, spacing);

        if (mouseOver == -1)
            return;

        highlightIcon(stack, xpos, ypos, gui, spacing, mouseOver);
    }

    // Highlight the selected icon
    // Remember to rebind texture afterwards
    public static void highlightIcon(MatrixStack stack, int xpos, int ypos, AbstractGui gui, int spacing, int icon)
    {
        RenderSystem.enableBlend();

        Minecraft.getInstance().getTextureManager().bindTexture(HIGHLIGHT);

        // Set the texture to be semi-transparent
        RenderSystem.color4f(1f, 1f, 1f, 0.5f);

        gui.blit(stack, xpos + icon * spacing, ypos, gui.getBlitOffset(), 0, 0, 16, 16, 16, 16);

        RenderSystem.color4f(1f, 1f, 1f, 1f);
    }

    // Darken the selected icon
    // Remember to rebind texture afterwards
    public static void cooldownIcon(MatrixStack stack, int xpos, int ypos, AbstractGui gui, int spacing, int icon)
    {
        RenderSystem.enableBlend();

        Minecraft.getInstance().getTextureManager().bindTexture(COOLDOWN);

        // Set the texture to be semi-transparent
        RenderSystem.color4f(1f, 1f, 1f, 0.5f);

        gui.blit(stack, xpos + icon * spacing, ypos, gui.getBlitOffset(), 0, 0, 16, 16, 16, 16);

        RenderSystem.color4f(1f, 1f, 1f, 1f);

        // TODO: Display how long left on cooldown?
    }
}
