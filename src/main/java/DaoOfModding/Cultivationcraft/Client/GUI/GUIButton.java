package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class GUIButton
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/button.png");

    public static final int height = 11;
    public static final int baseWidth = 6;
    public int width = 6;

    protected static final int textureWidth = 20;

    protected boolean selected = false;
    protected boolean hover = false;

    protected String name;
    protected String ID;

    protected int xpos = 0;
    protected int ypos = 0;

    protected boolean disabled = false;

    public GUIButton(String buttonID, String text)
    {
        ID = buttonID;
        name = text;

        width = baseWidth + Minecraft.getInstance().font.width(text);
    }

    public void setText(String text)
    {
        name = text;
    }

    public void disable()
    {
        disabled = true;
    }

    public void enable()
    {
        disabled = false;
    }

    public void setPos(int x, int y)
    {
        xpos = x;
        ypos = y;
    }

    public String getID()
    {
        return ID;
    }

    public boolean mouseClick(int mouseX, int mouseY, int buttonPressed)
    {
        // Do nothing if the left button isn't pressed
        if (buttonPressed != 0 || disabled)
            return false;

        mouseX -= xpos;
        mouseY -= ypos;

        if (mouseX >= 0 && mouseX < width && mouseY >= 0 && mouseY < height)
        {
            selected = !selected;
            return true;
        }

        return false;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void unselect()
    {
        selected = false;
    }

    public void select()
    {
        selected = true;
    }

    public boolean mouseOver(int mouseX, int mouseY)
    {
        // Reset the hover boolean
        hover = false;

        if (disabled)
            return hover;

        mouseX -= xpos;
        mouseY -= ypos;

        // If the mouse is over the button set hover to true
        if (mouseX >= 0 && mouseX < width && mouseY >= 0 && mouseY < height)
            hover = true;

        return hover;
    }

    public void render(PoseStack PoseStack, int mouseX, int mouseY, GuiComponent gui)
    {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int textColor = Color.BLACK.getRGB();

        if (disabled)
        {
            RenderSystem.setShaderColor(0.7F, 0.7F, 0.7F, 0.7F);
            textColor = Color.GRAY.getRGB();
        }

        mouseOver(mouseX, mouseY);

        // Set the offset to draw the correct button texture
        int offset = 0;

        if (selected)
            offset = 14;
        else if (hover)
            offset = 7;


        // Draw the start, end and middle of the button.
        gui.blit(PoseStack, xpos, ypos, gui.getBlitOffset(), offset, 0, baseWidth / 2, height, 255, 255);
        gui.blit(PoseStack, xpos + baseWidth / 2, ypos, gui.getBlitOffset(), 0, 14 + offset * 2, width - baseWidth, height, 255, 255);
        gui.blit(PoseStack, xpos + width - baseWidth / 2, ypos, gui.getBlitOffset(), offset + baseWidth / 2, 0, baseWidth / 2, height, 255, 255);

        // Draw the button text
        Minecraft.getInstance().font.draw(PoseStack, name, xpos + baseWidth / 2, ypos + 2, textColor);
    }
}
