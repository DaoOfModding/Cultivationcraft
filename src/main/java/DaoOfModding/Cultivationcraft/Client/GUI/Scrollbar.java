package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class Scrollbar
{
    ResourceLocation texture = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/scrollbar.png");

    protected int scrollPosition = 0;
    protected int scrollSize = 0;

    protected int x;
    protected int y;

    protected int yHeight = 70;

    public final int buttonSize = 11;

    protected int scrollInterval = 8;

    public Scrollbar()
    {
    }

    public void setScrollPosition(int scroll)
    {
        scrollPosition = scroll;
    }

    public void setPos(int xPos, int yPos)
    {
        x = xPos;
        y = yPos;
    }

    public void setSize(int size)
    {
        scrollSize = size;
    }

    public void setYHeight(int newHeight)
    {
        yHeight = newHeight;
    }


    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (buttonPressed != 0)
            return false;

        if (mouseY < buttonSize)
        {
            scroll(-scrollInterval);
            return true;
        }

        if (mouseY > yHeight - buttonSize)
        {
            scroll(scrollInterval);
            return true;
        }

        // Move to the position clicked on the scrollbar
        double scrollPercent = mouseY - buttonSize;
        scrollPercent = scrollPercent / (yHeight - buttonSize*2);

        scrollPosition = (int)(scrollSize * scrollPercent + 0.5);

        return true;
    }

    // Scroll the scroll bar
    public void scroll(int amount)
    {
        scrollPosition += amount;

        if (scrollPosition > scrollSize)
            scrollPosition = scrollSize;

        if (scrollPosition < 0)
            scrollPosition = 0;
    }

    public void render(Screen screen, PoseStack poseStack, int mouseX, int mouseY)
    {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        screen.blit(poseStack, x, y, screen.getBlitOffset(), 0, 0, buttonSize, buttonSize, 255, 255);
        screen.blit(poseStack, x, y + buttonSize, screen.getBlitOffset(), 0, buttonSize, buttonSize, yHeight - buttonSize*2, 255, 255);
        screen.blit(poseStack, x, y + yHeight - buttonSize, screen.getBlitOffset(), 0, 244, buttonSize, buttonSize, 255, 255);


        // Highlight the scroll buttons if mouse is over them
        if (mouseX-x >= 0 && mouseX-x < buttonSize)
        {
            if (mouseY-y >= 0 && mouseY-y < buttonSize)
                screen.blit(poseStack, x, y, screen.getBlitOffset(), buttonSize, 0, buttonSize, buttonSize, 255, 255);
            else if (mouseY-y >= yHeight-buttonSize && mouseY-y < yHeight)
                screen.blit(poseStack, x, y + yHeight - buttonSize, screen.getBlitOffset(), buttonSize, 244, buttonSize, buttonSize, 255, 255);
        }

        // Draw the scrollbar
//        float percent = scrollPosition / (scrollSize);
        //int scrollPosition = scrollBarTop + (int)((scrollBarBottom - scrollBarTop) *

        float percent = (float)scrollPosition / (float)scrollSize;
        int yPos = (int)((yHeight-buttonSize*2 - 9) * percent);

        screen.blit(poseStack, x, y + buttonSize + yPos, screen.getBlitOffset(), buttonSize, buttonSize, buttonSize, buttonSize + 9, 255, 255);
    }
}
