package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CultivationSelectionScreen extends Screen
{
    protected ResourceLocation TEXTURE;

    public final int xSize = 256;
    public final int ySize = 178;

    public CultivationSelectionScreen()
    {
        super(Component.translatable("cultivationcraft.gui.cultivationselection"));

        TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/blank.png");
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
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.blit(PoseStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }
}
