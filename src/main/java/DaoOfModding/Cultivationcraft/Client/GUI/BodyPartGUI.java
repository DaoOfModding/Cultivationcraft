package DaoOfModding.Cultivationcraft.Client.GUI;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

public class BodyPartGUI
{
    ResourceLocation TEXTURE = null;

    // X and Y are based on body size between the values of 1 and -1
    // X and Y are based on part size above those values
    // So an X of 1 would place the body part inside the body, touching the left edge with it's left edge
    // An x of 2 would place the body part outside the body, touching the left edge with it's right edge
    float x = 0;
    float y = 0;

    int textureX = 0;
    int textureY = 0;

    boolean overrideBase = false;

    public BodyPartGUI(ResourceLocation texture, float xOffset, float yOffset, int textureWidth, int textureHeight, boolean base)
    {
        TEXTURE = texture;

        // Convert from -1 - 1 to 0 - 1
        x = (xOffset + 1) / 2;
        y = (yOffset + 1) / 2;

        textureX = textureWidth;
        textureY = textureHeight;

        overrideBase = base;
    }

    public boolean isBase()
    {
        return overrideBase;
    }

    public int getTextureWidth()
    {
        return textureX;
    }

    public int getTextureHeight()
    {
        return textureY;
    }

    public void render(MatrixStack matrixStack, int xPos, int yPos, boolean highlight, AbstractGui gui)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);

        if (highlight)
            RenderSystem.color4f(0.5f, 1, 0.75f, 1);

        gui.blit(matrixStack, xPos, yPos, gui.getBlitOffset(), 0, 0, textureX, textureY, textureY, textureX);


        RenderSystem.color4f(1, 1, 1, 1);
    }

    // Render based on base part
    public void render(MatrixStack matrixStack, int xPos, int yPos, boolean highlight, AbstractGui gui, BodyPartGUI base)
    {
        float maxWidth = base.getTextureWidth() - getTextureWidth();
        float maxHeight = base.getTextureHeight() - getTextureHeight();

        // The random +1/-1 at the end is so that body parts JUST overlap the body, rather than being completely outside it
        if (x >= 0 && x <= 1)
            xPos += maxWidth * x;
        else if (x < 0)
            xPos += getTextureWidth() * x * 2 + 1;
        else if (x > 0)
            xPos += maxWidth + getTextureWidth() * (x - 1) * 2 -1;

        if (y >= 0 && y <= 1)
            yPos += maxHeight * y;
        else if (y < 0)
            yPos += getTextureWidth() * y * 2 + 1;
        else if (y > 0)
            yPos += maxHeight + getTextureHeight() * (y - 1) * 2 - 1;

        render(matrixStack, xPos, yPos, highlight, gui);
    }
}
