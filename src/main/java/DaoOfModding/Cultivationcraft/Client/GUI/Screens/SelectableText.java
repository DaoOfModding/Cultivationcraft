package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

public class SelectableText
{
    ResourceLocation texture = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/textfield.png");

    protected String selectableName;
    protected String selectableText;
    protected ArrayList<SelectableText> selectables = new ArrayList<>();

    protected boolean expanded = false;
    protected boolean selected = false;

    protected static final int tabbing = 10;
    public static final int yPadding = 2;

    protected static final Color highlightColor = new Color(140, 200, 140);

    protected int lineHeight = 0;

    public SelectableText(String name, String text)
    {
        selectableName = name;
        selectableText = text;
    }

    public String getText()
    {
        return selectableText;
    }

    public void unselectAll()
    {
        selected = false;

        for (SelectableText select : selectables)
            select.unselectAll();
    }

    public void select()
    {
        selected = true;
    }

    public void addItem(SelectableText item)
    {
        selectables.add(item);
    }

    public int height()
    {
        if (expanded)
            return lineHeight * (selectables.size() + 1);

        return lineHeight;
    }

    public SelectableText mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        // Do nothing if mouse button one isn't pressed
        if (buttonPressed != 0)
            return null;

        // Determine which line is clicked
        int line = (int)(mouseY / lineHeight);

        // If the text is pressed then expand or contract this item
        if (line == 0)
        {
            expanded = !expanded;
            return this;
        }
        // If this item is expanded then click on the item the mouse is over
        else if (expanded && line <= selectables.size())
        {
            return selectables.get(line - 1).mouseClicked(mouseX, mouseY - line * lineHeight, buttonPressed);
        }

        return null;
    }

    public void render(Screen screen, Font font, PoseStack poseStack, int color, int xPos, int yPos, int xEnd, int yStart, int yEnd)
    {
        lineHeight = font.lineHeight + yPadding;

        // Draw nothing if this line is off the screen
        if (yPos <= yEnd - lineHeight && yPos >= yStart)
        {
            // Highlight this line if it is selected
            if (selected) {
                RenderSystem.setShaderTexture(0, texture);
                RenderSystem.setShaderColor(highlightColor.getRed() / 255f, highlightColor.getGreen() / 255f, highlightColor.getBlue() / 255f, 1);
                screen.blit(poseStack, xPos - 2, yPos - yPadding, screen.getBlitOffset(), 0, 0, xEnd - xPos + 4, lineHeight, xEnd - xPos + 4, lineHeight);
            }

            font.draw(poseStack, selectableName, xPos, yPos, color);
        }

        if (expanded)
        {
            xPos += tabbing;

            for (SelectableText select : selectables)
            {
                yPos += lineHeight;
                select.render(screen, font, poseStack, color, xPos, yPos, xEnd, yStart, yEnd);
            }
        }
    }
}
