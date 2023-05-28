package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.HelpItems;
import DaoOfModding.Cultivationcraft.Client.GUI.SelectableTextField;
import DaoOfModding.Cultivationcraft.Client.GUI.TextField;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class HelpScreen extends GenericTabScreen
{
    protected int helpTextX = 90;
    protected int helpTextY = 35;
    protected int helpTextWidth = 150;
    protected int helpTextHeight = 121;

    protected int selectTextX = 10;
    protected int selectTextY = 35;
    protected int selectTextWidth = 70;
    protected int selectTextHeight = 121;

    protected int nameTextY = 22;

    protected TextField helpText = new TextField();
    protected SelectableTextField selectText;
    
    public HelpScreen()
    {
        super(3, Component.translatable("cultivationcraft.gui.help"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/stats.png"));
        
        helpText.resetScroll();

        selectText = HelpItems.getText();
        selectText.setSize(selectTextWidth, selectTextHeight, (int)(selectTextWidth * 2));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (mouseX > selectTextX +edgeSpacingX && mouseX < selectTextX + edgeSpacingX + selectText.getWidth())
            if (mouseY > selectTextY +edgeSpacingY && mouseY < selectTextY + edgeSpacingY + selectTextHeight)
                selectText.mouseClicked(mouseX - (selectTextX +edgeSpacingX), mouseY - (selectTextY +edgeSpacingY), buttonPressed);

        if (mouseX > helpTextX +edgeSpacingX && mouseX < helpTextX + edgeSpacingX + helpTextWidth)
            if (mouseY > helpTextY +edgeSpacingY && mouseY < helpTextY + edgeSpacingY + helpTextHeight)
                return helpText.mouseClicked(mouseX - (helpTextX +edgeSpacingX), mouseY - (helpTextY +edgeSpacingY), buttonPressed);

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double direction)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (mouseX > selectTextX +edgeSpacingX && mouseX < selectTextX + edgeSpacingX + selectText.getWidth())
            if (mouseY > selectTextY +edgeSpacingY && mouseY < selectTextY + edgeSpacingY + selectTextHeight)
                return selectText.mouseScrolled(direction);

        if (mouseX > helpTextX +edgeSpacingX && mouseX < helpTextX + edgeSpacingX + helpTextWidth)
            if (mouseY > helpTextY +edgeSpacingY && mouseY < helpTextY + edgeSpacingY + helpTextHeight)
                return helpText.mouseScrolled(direction);

        return false;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (selectText.getSelected() != null)
            font.draw(PoseStack, selectText.getSelected().getName(), edgeSpacingX + (int)(this.xSize / 2) - (int)(font.width(selectText.getSelected().name) / 2),  edgeSpacingY + nameTextY, Color.darkGray.getRGB());

        helpText.setPos(edgeSpacingX + helpTextX, edgeSpacingY + helpTextY);
        helpText.setSize(helpTextWidth, helpTextHeight);
        helpText.setText(selectText.getText());
        helpText.render(this, font, PoseStack, mouseX, mouseY);

        selectText.setPos(edgeSpacingX +selectTextX, edgeSpacingY + selectTextY);
        selectText.render(this, font, PoseStack, mouseX, mouseY);
    }
}
