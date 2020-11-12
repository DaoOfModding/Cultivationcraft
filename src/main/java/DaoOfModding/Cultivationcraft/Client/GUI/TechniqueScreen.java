package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class TechniqueScreen extends Screen
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/technique.png");

    private final int xSize = 175;
    private final int ySize = 178;

    public TechniqueScreen()
    {
        super(new TranslationTextComponent("cultivationcraft.gui.technique"));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        drawGuiBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        drawGuiForgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (ScreenTabControl.mouseClick((int)mouseX, (int)mouseY, edgeSpacingX, edgeSpacingY, buttonPressed))
            return true;

        if (buttonPressed == 0)
        {
            int techniqueSelected = TechniqueIcons.mouseOver(edgeSpacingX + 8, edgeSpacingY + 155, (int) mouseX, (int) mouseY, 18);

            if (techniqueSelected != -1) {
                // TODO: Something when technique is clicked here
            }
        }

        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        return false;
    }

    protected void drawGuiForgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        // Draw icons in the selection box
        TechniqueIcons.renderIcons(matrixStack, edgeSpacingX + 8,edgeSpacingY + 155, this, 18);

        TechniqueIcons.mouseOverHighlight(matrixStack, edgeSpacingX + 8,edgeSpacingY + 155, this, 18, mouseX, mouseY);
    }

    protected void drawGuiBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);

        ScreenTabControl.highlightTabs(matrixStack, 1, mouseX, mouseY, edgeSpacingX, edgeSpacingY, this);
    }
}
