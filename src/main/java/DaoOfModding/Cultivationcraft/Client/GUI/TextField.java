package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;


public class TextField
{
    ResourceLocation texture = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/textfield.png");

    protected final int xPadding = 5;
    protected final int yPadding = 5;

    int x = 0;
    int y = 0;

    int width = 0;
    int height = 0;

    String text = "";

    Color textColor = Color.WHITE;
    Color backgroundColor = new Color(140, 140, 140);

    public TextField()
    {

    }

    public void setPos(int newX, int newY)
    {
        x = newX;
        y = newY;
    }

    public void setSize(int newWidth, int newHeight)
    {
        width = newWidth;
        height = newHeight;
    }

    public void setText(String newText)
    {
        text = newText;
    }

    public void setTextColor(Color newColor)
    {
        textColor = newColor;
    }

    public void render(Screen screen, Font font, PoseStack poseStack)
    {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(backgroundColor.getRed()/255f, backgroundColor.getGreen()/255f, backgroundColor.getBlue()/255f, 1);
        screen.blit(poseStack, x, y, screen.getBlitOffset(), 0, 0, width, height, width, height);

        RenderSystem.setShaderColor((backgroundColor.getRed() - 100)/255f, (backgroundColor.getGreen() - 100)/255f, (backgroundColor.getBlue() - 100)/255f, 1);
        screen.blit(poseStack, x, y, screen.getBlitOffset(), 0, 0, 1, height-1, 1, height-1);
        screen.blit(poseStack, x, y, screen.getBlitOffset(), 0, 0, width - 1, 1, width - 1, 1);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        screen.blit(poseStack, x + width - 1, y + 1, screen.getBlitOffset(), 0, 0, 1, height-1, 1, height-1);
        screen.blit(poseStack, x + 1, y + height - 1, screen.getBlitOffset(), 0, 0, width - 1, 1, width - 1, 1);

        // TODO scrolling
        BetterFontRenderer.wordwrap(font, poseStack, text, x + xPadding, y + yPadding, textColor.getRGB(), width - xPadding * 2, height - yPadding * 2);
    }
}
