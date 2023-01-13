package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.ScreenTabControl;
import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainer;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class FlyingSwordContainerScreen extends AbstractContainerScreen<FlyingSwordContainer>
{
    protected Button bindButton;

    protected static final int PROGRESS_BAR_X_POS = 43;
    protected static final int PROGRESS_BAR_Y_POS = 77;

    protected static final int PROGRESS_BAR_X_SIZE = 89;
    protected static final int PROGRESS_BAR_Y_SIZE = 4;

    protected static final int PROGRESS_BAR_U = 0;
    protected static final int PROGRESS_BAR_V = 252;

    public FlyingSwordContainerScreen(FlyingSwordContainer container, Inventory playerInv, Component title)
    {
        super(container, playerInv, title);

        imageWidth = 256;
        imageHeight = 178;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        int edgeSpacingX = (this.width - this.imageWidth) / 2;
        int edgeSpacingY = (this.height - this.imageHeight) / 2;

        if (ScreenTabControl.mouseClick((int)mouseX, (int)mouseY, edgeSpacingX, edgeSpacingY, buttonPressed))
            return true;

        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        return false;
    }

    @Override
    protected void renderLabels(PoseStack PoseStack, int mouseX, int mouseY)
    {
        final float PLAYER_LABEL_XPOS = 8;
        final float PLAYER_LABEL_DISTANCE_FROM_BOTTOM = (96 - 2);

        final float BAG_LABEL_YPOS = 24;

        final float PROGRESS_LABEL_XPOS = 42;
        final float PROGRESS_LABEL_YPOS = 64;

        String testString = Component.translatable("cultivationcraft.gui.bind").getString();

        float BAG_LABEL_XPOS = (imageHeight / 2.0F) - font.width(testString) / 2.0F;
        font.draw(PoseStack, testString, BAG_LABEL_XPOS, BAG_LABEL_YPOS, Color.darkGray.getRGB());

        float PLAYER_LABEL_YPOS = imageHeight - PLAYER_LABEL_DISTANCE_FROM_BOTTOM;
        font.draw(PoseStack, this.playerInventoryTitle.getString(),
                PLAYER_LABEL_XPOS, PLAYER_LABEL_YPOS, Color.darkGray.getRGB());


        String progress;

        if (menu.getBindPercent() == 1)
            progress = Component.translatable("cultivationcraft.gui.done").getString();
        else if (menu.getBindPercent() == 0)
            progress = "";
        else
            progress = Component.translatable("cultivationcraft.gui.remaining", (int)menu.getBindTime()).getString();

        font.draw(PoseStack, progress, PROGRESS_LABEL_XPOS, PROGRESS_LABEL_YPOS, Color.darkGray.getRGB());
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float partialTicks, int mouseX, int mouseY)
    {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int edgeSpacingX = (this.width - this.imageWidth) / 2;
        int edgeSpacingY = (this.height - this.imageHeight) / 2;
        this.blit(PoseStack, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);

        // Draw the progress bar based on bind progress
        float progress = menu.getBindPercent() * PROGRESS_BAR_X_SIZE;

        // If progress is greater than 0 draw the progress bar
        if (progress > 0)
            this.blit(PoseStack, edgeSpacingX + PROGRESS_BAR_X_POS, edgeSpacingY + PROGRESS_BAR_Y_POS, PROGRESS_BAR_U, PROGRESS_BAR_V, (int)progress, PROGRESS_BAR_Y_SIZE);
        // If progress is less than 0 draw the progress to unbind bar
        else
            this.blit(PoseStack, edgeSpacingX + PROGRESS_BAR_X_POS + PROGRESS_BAR_X_SIZE - (int)(progress * -1), edgeSpacingY + PROGRESS_BAR_Y_POS,
                    PROGRESS_BAR_U, PROGRESS_BAR_V - PROGRESS_BAR_Y_SIZE,
                    (int)progress * -1, PROGRESS_BAR_Y_SIZE);

        ScreenTabControl.highlightTabs(PoseStack, 2, mouseX, mouseY, edgeSpacingX, edgeSpacingY, this);
    }

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bindingcontainer.png");
}
