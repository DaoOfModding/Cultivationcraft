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

    Scrollbar scroll = new Scrollbar();

    protected int scrolled = 0;

    public TextField()
    {

    }

    public void setPos(int newX, int newY)
    {
        x = newX;
        y = newY;

        scroll.setPos(x + width - 1 - scroll.buttonSize, y+1);
    }

    public void setSize(int newWidth, int newHeight)
    {
        width = newWidth;
        height = newHeight;

        scroll.setYHeight(newHeight-2);
        scroll.setPos(x + width - 1 - scroll.buttonSize, y+1);
    }

    public void setText(String newText)
    {
        text = newText;
    }

    public void setTextColor(Color newColor)
    {
        textColor = newColor;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (mouseX > width - 1 - scroll.buttonSize && mouseX < width - 1)
            if (mouseY > 0 && mouseY < height)
                return scroll.mouseClicked(mouseX - width - 1 - scroll.buttonSize, mouseY - 1, buttonPressed);

        return false;
    }

    public boolean mouseScrolled(double direction)
    {
        scroll.scroll((int)-direction);

        return true;
    }

    public void render(Screen screen, Font font, PoseStack poseStack, int mouseX, int mouseY)
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


        int textWidth = width - xPadding * 2;
        int lines = BetterFontRenderer.countLines(font, text, textWidth);

        // If there are more lines to draw than can be displayed then adjusted the textWidth to not include the space taken by the scroll bar and recalculate the lines
        if (lines > height)
        {
            textWidth -= scroll.buttonSize;
            lines = BetterFontRenderer.countLines(font, text, textWidth);
        }

        BetterFontRenderer.wordwrap(font, poseStack, text, x + xPadding, y + yPadding, textColor.getRGB(), textWidth, height - yPadding * 2, scroll.scrollPosition);

        int scrollSize = Math.max(BetterFontRenderer.countLines(font, text, width - xPadding * 2 - scroll.buttonSize) - (height  - yPadding * 2), 0);
        scroll.scrollInterval = font.lineHeight + 1;
        scroll.setSize(scrollSize);

        if (lines > height)
            scroll.render(screen, poseStack, mouseX, mouseY);
    }
}
