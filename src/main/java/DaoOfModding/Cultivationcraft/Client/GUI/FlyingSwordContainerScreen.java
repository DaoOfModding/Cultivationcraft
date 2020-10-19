package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainer;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class FlyingSwordContainerScreen extends ContainerScreen<FlyingSwordContainer>
{
    private Button bindButton;

    public FlyingSwordContainerScreen(FlyingSwordContainer container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);
    }
    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        final float PLAYER_LABEL_XPOS = 8;
        final float PLAYER_LABEL_DISTANCE_FROM_BOTTOM = (96 - 2);

        final float BAG_LABEL_YPOS = 6;
        float BAG_LABEL_XPOS = (xSize / 2.0F) - font.getStringWidth("Test") / 2.0F;
        font.drawString(matrixStack, "Test", BAG_LABEL_XPOS, BAG_LABEL_YPOS, Color.darkGray.getRGB());

        float PLAYER_LABEL_YPOS = ySize - PLAYER_LABEL_DISTANCE_FROM_BOTTOM;
        font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(),
                PLAYER_LABEL_XPOS, PLAYER_LABEL_YPOS, Color.darkGray.getRGB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation("cultivationcraft", "textures/gui/testcontainer.png");
}
