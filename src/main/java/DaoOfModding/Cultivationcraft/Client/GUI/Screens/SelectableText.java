package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
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

    protected MutableComponent name;
    protected MutableComponent text;

    public SelectableText(String name, String text)
    {
        selectableName = name;
        selectableText = text;
    }

    public SelectableText(String component)
    {
        name = Component.translatable(component);
        text = Component.translatable(component.concat(".text"));
    }

    public String getText()
    {
        if (text == null)
            return selectableText;

        return text.getString();
    }

    public String getName()
    {
        if (name == null)
            return selectableName;

        return name.getString();
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
        int height = lineHeight;

        if (expanded)
        {
            for (SelectableText selectable : selectables)
                height += selectable.height();
        }

        return height;
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
        else if (expanded)
        {
            int entryLine = 0;
            int lineCheck = 0;

            for (SelectableText selectable : selectables)
            {
                lineCheck += selectable.height() / lineHeight;

                if (line <= lineCheck)
                    return selectable.mouseClicked(mouseX, mouseY - (entryLine+1) * lineHeight, buttonPressed);

                entryLine += selectable.height() / lineHeight;
            }
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
            if (selected)
            {
                RenderSystem.setShaderTexture(0, texture);
                RenderSystem.setShaderColor(highlightColor.getRed() / 255f, highlightColor.getGreen() / 255f, highlightColor.getBlue() / 255f, 1);
                screen.blit(poseStack, xPos - 2, yPos - yPadding, screen.getBlitOffset(), 0, 0, xEnd - xPos + 4, lineHeight, xEnd - xPos + 4, lineHeight);
            }

            FormattedText formatedName = font.substrByWidth(FormattedText.of(getName()), xEnd - xPos);

            font.draw(poseStack, formatedName.getString(), xPos, yPos, color);
        }

        if (expanded)
        {
            xPos += tabbing;

            yPos += lineHeight;

            for (SelectableText select : selectables)
            {
                select.render(screen, font, poseStack, color, xPos, yPos, xEnd, yStart, yEnd);
                yPos += select.height();
            }
        }
    }
}
