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

    int width = 0;
    int height = 0;

    ArrayList<SelectableText> selectables = new ArrayList<>();
    SelectableText selected = null;

    Color textColor = Color.WHITE;
    Color backgroundColor = new Color(140, 140, 140);

    // TODO: Scrollbar
    Scrollbar scroll = new Scrollbar();

    public SelectableTextField()
    {
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

    public void setSize(int xSize, int ySize)
    {
        width = xSize;
        height = ySize;
    }

    public void addSelectable(SelectableText selectable)
    {
        selectables.add(selectable);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        // Do nothing if mouse button one isn't pressed
        if (buttonPressed != 0)
            return false;

        mouseY -= yPadding;

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

        int yPos = y + yPadding;
        int xPos = x + xPadding;

        for (SelectableText select : selectables)
        {
            select.render(screen, font, poseStack, textColor.getRGB(), xPos, yPos, xPos + width - xPadding*2);
            yPos += select.height();
        }
    }
}
