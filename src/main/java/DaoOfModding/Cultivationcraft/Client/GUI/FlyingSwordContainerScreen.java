package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainer;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class FlyingSwordContainerScreen extends ContainerScreen<FlyingSwordContainer>
{
    private Button bindButton;

    private static final int PROGRESS_BAR_X_POS = 43;
    private static final int PROGRESS_BAR_Y_POS = 77;

    private static final int PROGRESS_BAR_X_SIZE = 89;
    private static final int PROGRESS_BAR_Y_SIZE = 4;

    private static final int PROGRESS_BAR_U = 0;
    private static final int PROGRESS_BAR_V = 252;

    public FlyingSwordContainerScreen(FlyingSwordContainer container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);

        imageWidth = 175;
        imageHeight = 178;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
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
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        final float PLAYER_LABEL_XPOS = 8;
        final float PLAYER_LABEL_DISTANCE_FROM_BOTTOM = (96 - 2);

        final float BAG_LABEL_YPOS = 24;

        final float PROGRESS_LABEL_XPOS = 42;
        final float PROGRESS_LABEL_YPOS = 64;

        String testString = new TranslationTextComponent("cultivationcraft.gui.bind").getString();

        float BAG_LABEL_XPOS = (imageHeight / 2.0F) - font.width(testString) / 2.0F;
        font.draw(matrixStack, testString, BAG_LABEL_XPOS, BAG_LABEL_YPOS, Color.darkGray.getRGB());

        float PLAYER_LABEL_YPOS = imageHeight - PLAYER_LABEL_DISTANCE_FROM_BOTTOM;
        font.draw(matrixStack, this.inventory.getDisplayName().getString(),
                PLAYER_LABEL_XPOS, PLAYER_LABEL_YPOS, Color.darkGray.getRGB());


        String progress;

        if (menu.getBindPercent() == 1)
            progress = new TranslationTextComponent("cultivationcraft.gui.done").getString();
        else if (menu.getBindPercent() == 0)
            progress = "";
        else
            progress = new TranslationTextComponent("cultivationcraft.gui.remaining", (int)menu.getBindTime()).getString();

        font.draw(matrixStack, progress, PROGRESS_LABEL_XPOS, PROGRESS_LABEL_YPOS, Color.darkGray.getRGB());
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        Minecraft mc = Minecraft.getInstance();
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bind(TEXTURE);

        int edgeSpacingX = (this.width - this.imageWidth) / 2;
        int edgeSpacingY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);

        // Draw the progress bar based on bind progress
        float progress = menu.getBindPercent() * PROGRESS_BAR_X_SIZE;

        // If progress is greater than 0 draw the progress bar
        if (progress > 0)
            this.blit(matrixStack, edgeSpacingX + PROGRESS_BAR_X_POS, edgeSpacingY + PROGRESS_BAR_Y_POS, PROGRESS_BAR_U, PROGRESS_BAR_V, (int)progress, PROGRESS_BAR_Y_SIZE);
        // If progress is less than 0 draw the progress to unbind bar
        else
            this.blit(matrixStack, edgeSpacingX + PROGRESS_BAR_X_POS + PROGRESS_BAR_X_SIZE - (int)(progress * -1), edgeSpacingY + PROGRESS_BAR_Y_POS,
                    PROGRESS_BAR_U, PROGRESS_BAR_V - PROGRESS_BAR_Y_SIZE,
                    (int)progress * -1, PROGRESS_BAR_Y_SIZE);

        ScreenTabControl.highlightTabs(matrixStack, 2, mouseX, mouseY, edgeSpacingX, edgeSpacingY, this);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bindingcontainer.png");
}
