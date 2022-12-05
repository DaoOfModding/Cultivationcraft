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
import java.util.HashMap;

public class DropdownList
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/dropdownlist.png");

    public static final int width = 85;
    public static final int closedHeight = 11;
    public static final int openHeight = 71;

    public static final int scrollbarXStart = 75;
    public static final int scrollbarYDown = 60;

    public static final int scrollBarTop = 11;
    public static final int scrollBarBottom = 51;

    private static final int scrollBarSize = 9;

    private static final int MaxSize = 6;

    private int scrollOffset = 0;

    // Map String, String = Name, Value
    // Name is what the entry displays
    // Value is the data stored in the entry
    private HashMap<String, Object> items = new HashMap<String, Object>();

    private String selected = null;

    boolean isOpen = false;

    public DropdownList()
    {
    }

    public void addItem(String name, Object value)
    {
        items.put(name, value);

        if (selected == null)
            selected = name;
    }

    public void setOpen(boolean open)
    {
        isOpen = open;
    }

    // Return the Value of the selected list entry
    public Object getSelected()
    {
        return items.get(selected);
    }


    public boolean mouseScroll(double mouseX, double mouseY, double direction)
    {
        if (!isOpen)
            return false;

        scroll((int)-direction);

        return true;
    }

    public Object mouseClick(int mouseX, int mouseY, int buttonPressed)
    {
        if (isOpen)
            return mouseClickOpen(mouseX, mouseY, buttonPressed);
        else
        {
            mouseClickClosed(mouseX, mouseY, buttonPressed);
            return null;
        }
    }

    public void mouseClickClosed(int mouseX, int mouseY, int buttonPressed)
    {
        // Do nothing if the left button isn't pressed
        if (buttonPressed != 0)
            return;

        // If the mouse is pressed on the selected item open the list
        if (mouseX >= 0 && mouseX <= width &&
            mouseY >= 0 && mouseY <= closedHeight)
            setOpen(true);
    }

    // Process mouse click. Return new selected object if an object is selected.
    public Object mouseClickOpen(int mouseX, int mouseY, int buttonPressed)
    {
        // Do nothing if the left button isn't pressed
        if (buttonPressed != 0)
            return null;

        // If the mouse is pressed outside the list then close the list
        if (mouseX < 0 || mouseX > width ||
            mouseY < 0 || mouseY > openHeight)
        {
            setOpen(false);
            return null;
        }

        // If an item on the list is clicked, change selected item
        if (mouseX >= 0 && mouseX < scrollbarXStart)
        {
            if (mouseY > scrollBarTop && mouseY < openHeight)
                if (changeSelection((float) (mouseY - scrollBarTop) / (float) (openHeight - scrollBarTop)));
                {
                    setOpen(false);
                    return items.get(selected);
                }
        }
        // Else if the scrollbar is clicked, adjust scroll position based on where
        else if (mouseX >= scrollbarXStart && mouseX < width)
            if (mouseY >= 0 && mouseY < closedHeight)
                scroll(-1);
            else if (mouseY >= scrollbarYDown && mouseY < openHeight)
                scroll(1);
            else if (mouseY > 0 && mouseY < openHeight)
                scrollTo((float)(mouseY - scrollBarTop) / (float)(scrollbarYDown - scrollBarTop));

        return null;
    }

    public void changeSelection(String selection)
    {
        selected = selection;
    }

    // Change the selected item based on location clicked on dropdown list
    public boolean changeSelection(float percent)
    {
        int selection = (int)(percent * MaxSize);

        int i = 0;
        int count = 0;

        for (String name : items.keySet())
            if (!name.contentEquals(selected))
            {
                // Only start counting once the scroll offset has been reached
                if (scrollOffset <= i)
                {
                    if (count == selection)
                    {
                        changeSelection(name);
                        return true;
                    }

                    count++;
                }
                else
                    i++;
            }

        return false;
    }

    public void scrollTo(float percent)
    {
        scrollOffset = (int)((items.size() - MaxSize) * percent);

        if (scrollOffset > items.size() - MaxSize - 1)
            scrollOffset = items.size() - MaxSize - 1;

        if (scrollOffset < 0)
            scrollOffset = 0;
    }

    // Scroll the scroll bar
    public void scroll(int amount)
    {
        scrollOffset += amount;

        if (scrollOffset > items.size() - MaxSize - 1)
            scrollOffset = items.size() - MaxSize - 1;

        if (scrollOffset < 0)
            scrollOffset = 0;
    }

    public void render(PoseStack PoseStack, int xpos, int ypos, int mouseX, int mouseY, GuiComponent gui)
    {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw the list open or closed
        if (isOpen)
        {
            gui.blit(PoseStack, xpos, ypos, gui.getBlitOffset(), width + 1, 0, width, openHeight, 255, 255);

            drawScrollBar(PoseStack, xpos, ypos, mouseX, mouseY, gui);
        }
        else
            gui.blit(PoseStack, xpos, ypos, gui.getBlitOffset(), 0, 0, width, closedHeight, 255, 255);

        // Draw the selected item
        Minecraft.getInstance().font.draw(PoseStack, selected, xpos + 2, ypos + 2, Color.white.getRGB());

        // Draw all items if open
        if (isOpen)
            renderListText(PoseStack, xpos, ypos, gui);

    }

    public void drawScrollBar(PoseStack PoseStack, int xpos, int ypos, int mouseX, int mouseY, GuiComponent gui)
    {
        // Highlight the scroll buttons if mouse is over them
        if (mouseX-xpos >= scrollbarXStart && mouseX-xpos < width)
            if (mouseY-ypos >= 0 && mouseY-ypos < closedHeight)
                gui.blit(PoseStack, xpos + scrollbarXStart, ypos, gui.getBlitOffset(), width*2 + 2, 0, width-scrollbarXStart, closedHeight, 255, 255);
            else if (mouseY-ypos >= scrollbarYDown && mouseY-ypos < openHeight)
                gui.blit(PoseStack, xpos + scrollbarXStart, ypos+scrollbarYDown, gui.getBlitOffset(), width*2 + 2, scrollbarYDown, width-scrollbarXStart, closedHeight, 255, 255);

        // Draw the scrollbar
        float percent = (float)scrollOffset / ((float)items.size() - (float)MaxSize - 1);
        int scrollPosition = scrollBarTop + (int)((scrollBarBottom - scrollBarTop) * percent);

        gui.blit(PoseStack, xpos + scrollbarXStart, ypos + scrollPosition, gui.getBlitOffset(), width*2 + 2, closedHeight, width-scrollbarXStart, scrollBarSize, 255, 255);
    }

    // Draw visible list items
    private void renderListText(PoseStack PoseStack, int xpos, int ypos, GuiComponent gui)
    {
        int i = 0;

        for (String name : items.keySet())
            if (!name.contentEquals(selected))
            {
                // Only start displaying list items from the scroll offset
                if (scrollOffset <= i)
                    Minecraft.getInstance().font.draw(PoseStack, name, xpos + 2, ypos + 2 + (i + 1 - scrollOffset) * (closedHeight - 1), Color.white.getRGB());

                i++;

                // Stop displaying items once MaxSize items have been displayed
                if (i == MaxSize + scrollOffset)
                    break;
            }
    }
}
