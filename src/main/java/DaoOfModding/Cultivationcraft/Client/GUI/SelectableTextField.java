package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.SelectableText;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

public class SelectableTextField
{
    ResourceLocation texture = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/textfield.png");

    protected final int xPadding = 5;
    protected final int yPadding = 5;

    int x = 0;
    int y = 0;

    int minWidth = 0;
    int maxWidth = 0;
    int width = 0;
    int height = 0;

    ArrayList<SelectableText> selectables = new ArrayList<>();
    SelectableText selected = null;

    Color textColor = Color.WHITE;
    Color backgroundColor = new Color(140, 140, 140);

    int totalHeight = 0;

    Scrollbar scroll = new Scrollbar();

    public SelectableTextField()
    {
        scroll.setScrollPosition(0);
    }

    public String getText()
    {
        if (selected == null)
            return "";

        return selected.getText();
    }

    public void setPos(int xPos, int yPos)
    {
        x = xPos;
        y = yPos;
    }

    public ArrayList<SelectableText> getSelectables()
    {
        return selectables;
    }

    public int getWidth()
    {
        return width;
    }

    // Set size of the text field without any expansion when mousing over it
    public void setSize(int xSize, int ySize)
    {
        setSize(xSize, ySize, xSize);
    }

    public void setSize(int xSize, int ySize, int expandedXSize)
    {
        minWidth = xSize;
        maxWidth = expandedXSize;
        width = minWidth;

        height = ySize;

        scroll.setYHeight(ySize-2);
    }

    public void addSelectable(SelectableText selectable)
    {
        selectables.add(selectable);
    }

    public SelectableText getSelected()
    {
        return selected;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        // Do nothing if mouse button one isn't pressed
        if (buttonPressed != 0)
            return false;

        // Only try to click on scrollbar if it is visible
        if (totalHeight > height)
            if (mouseX > width - 1 - scroll.buttonSize && mouseX < width - 1)
                if (mouseY > 0 && mouseY < height)
                    return scroll.mouseClicked(mouseX - width - 1 - scroll.buttonSize, mouseY - 1, buttonPressed);

        mouseY -= yPadding;
        mouseY += scroll.scrollPosition;

        for (SelectableText selectable : selectables)
        {
            if (mouseY > selectable.height())
            {
                mouseY -= selectable.height();
            }
            else
            {
                SelectableText select = selectable.mouseClicked(mouseX, mouseY, buttonPressed);

                if (select == null)
                    return false;

                for (SelectableText selectOff : selectables)
                    selectOff.unselectAll();

                select.select();

                selected = select;
                return true;
            }
        }

        return false;
    }

    public boolean mouseScrolled(double direction)
    {
        scroll.scroll((int)-direction * scroll.scrollInterval);

        return true;
    }

    // If the mouse is hovering over the box, expand it
    protected void isMouseOver(int mouseX, int mouseY)
    {
        if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height)
            width = maxWidth;
        else
            width = minWidth;

        scroll.setPos(x + width - 1 - scroll.buttonSize, y+1);
    }

    public void render(Screen screen, Font font, PoseStack poseStack, int mouseX, int mouseY)
    {
        isMouseOver(mouseX, mouseY);

        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(backgroundColor.getRed()/255f, backgroundColor.getGreen()/255f, backgroundColor.getBlue()/255f, 1);
        screen.blit(poseStack, x, y, screen.getBlitOffset(), 0, 0, width, height, width, height);

        RenderSystem.setShaderColor((backgroundColor.getRed() - 100)/255f, (backgroundColor.getGreen() - 100)/255f, (backgroundColor.getBlue() - 100)/255f, 1);
        screen.blit(poseStack, x, y, screen.getBlitOffset(), 0, 0, 1, height-1, 1, height-1);
        screen.blit(poseStack, x, y, screen.getBlitOffset(), 0, 0, width - 1, 1, width - 1, 1);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        screen.blit(poseStack, x + width - 1, y + 1, screen.getBlitOffset(), 0, 0, 1, height-1, 1, height-1);
        screen.blit(poseStack, x + 1, y + height - 1, screen.getBlitOffset(), 0, 0, width - 1, 1, width - 1, 1);

        int yPos = y + yPadding - scroll.scrollPosition;
        int xPos = x + xPadding;

        totalHeight = 0;

        for (SelectableText select : selectables)
        {
            select.render(screen, font, poseStack, textColor.getRGB(), xPos, yPos, xPos + width - xPadding*2, y + yPadding, y + height - yPadding);
            yPos += select.height();

            totalHeight += select.height();
        }

        if (totalHeight < height)
        {
            scroll.setScrollPosition(0);
        }
        else
        {
            scroll.scrollInterval = font.lineHeight + SelectableText.yPadding;
            scroll.setSize(totalHeight - height + scroll.scrollInterval);

            scroll.render(screen, poseStack, mouseX, mouseY);
        }
    }
}
