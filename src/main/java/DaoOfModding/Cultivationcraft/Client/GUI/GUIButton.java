package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GUIButton
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/button.png");

    public static final int height = 11;
    public static final int baseWidth = 6;
    public int width = 6;

    private static final int textureWidth = 20;

    private boolean selected = false;
    private boolean hover = false;

    private String name;
    private String ID;

    public GUIButton(String buttonID, String text)
    {
        ID = buttonID;
        name = text;

        width = baseWidth + Minecraft.getInstance().fontRenderer.getStringWidth(text);
    }

    public String getID()
    {
        return ID;
    }

    public boolean mouseClick(int mouseX, int mouseY, int buttonPressed)
    {
        // Do nothing if the left button isn't pressed
        if (buttonPressed != 0)
            return false;

        if (mouseX >= 0 && mouseX < width && mouseY >= 0 && mouseY < height)
        {
            selected = !selected;
            return true;
        }

        return false;
    }

    public void unselect()
    {
        selected = false;
    }

    public boolean mouseOver(int mouseX, int mouseY)
    {
        // Reset the hover boolean
        hover = false;

        // If the mouse is over the button set hover to true
        if (mouseX >= 0 && mouseX < width && mouseY >= 0 && mouseY < height)
            hover = true;

        return hover;
    }

    public void render(MatrixStack matrixStack, int xpos, int ypos, int mouseX, int mouseY, AbstractGui gui)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);

        mouseOver(mouseX - xpos, mouseY - ypos);

        // Set the offset to draw the correct button texture
        int offset = 0;

        if (selected)
            offset = 14;
        else if (hover)
            offset = 7;


        // Draw the start, end and middle of the button.
        gui.blit(matrixStack, xpos, ypos, gui.getBlitOffset(), offset, 0, baseWidth / 2, height, 255, 255);
        gui.blit(matrixStack, xpos + baseWidth / 2, ypos, gui.getBlitOffset(), 0, 14 + offset * 2, width - baseWidth, height, 255, 255);
        gui.blit(matrixStack, xpos + width - baseWidth / 2, ypos, gui.getBlitOffset(), offset + baseWidth / 2, 0, baseWidth / 2, height, 255, 255);


        // Draw the button text
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, name, xpos + baseWidth / 2, ypos + 2, Color.BLACK.getRGB());
    }
}
