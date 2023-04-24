package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.ScreenTabControl;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class GenericTabScreen extends Screen
{
    protected ResourceLocation TEXTURE;

    public final int xSize = 256;
    public final int ySize = 178;

    protected int tab;
    protected final ResourceLocation tabTexture = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/tabscreen.png");

    GenericTabScreen(int activeTab, Component name, ResourceLocation tabLocation)
    {
        super(name);

        tab = activeTab;
        TEXTURE = tabLocation;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (ScreenTabControl.mouseClick((int)mouseX, (int)mouseY, edgeSpacingX, edgeSpacingY, buttonPressed))
            return true;

        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        return false;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(PoseStack);
        drawGuiBackgroundLayer(PoseStack, partialTicks, mouseX, mouseY);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
    }

    protected void drawGuiBackgroundLayer(PoseStack PoseStack, float partialTicks, int mouseX, int mouseY)
    {
        // Draw the generic tab screen
        RenderSystem.setShaderTexture(0, tabTexture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(PoseStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);

        ScreenTabControl.highlightTabs(PoseStack, tab, mouseX, mouseY, edgeSpacingX, edgeSpacingY, this);
        ScreenTabControl.tabText(PoseStack, edgeSpacingX, edgeSpacingY, this.font);

        // Draw anything specific to this tab
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.blit(PoseStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }
}
