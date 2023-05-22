package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.SelectableTextField;
import DaoOfModding.Cultivationcraft.Client.GUI.TextField;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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

    protected TextField helpText = new TextField();
    protected SelectableTextField selectText = new SelectableTextField();
    
    public HelpScreen()
    {
        super(3, Component.translatable("cultivationcraft.gui.help"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/stats.png"));
        
        helpText.resetScroll();

        for (int i = 0; i < 5; i++)
        {
            SelectableText testField = new SelectableText("Test" + i, "Text" + i);

            for (int j = 0; j < 5; j++)
                testField.addItem(new SelectableText("Test" + i + "-" + j, "Text" + i + "-" + j));

            selectText.addSelectable(testField);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (mouseX > helpTextX +edgeSpacingX && mouseX < helpTextX + edgeSpacingX + helpTextWidth)
            if (mouseY > helpTextY +edgeSpacingY && mouseY < helpTextY + edgeSpacingY + helpTextHeight)
                return helpText.mouseClicked(mouseX - (helpTextX +edgeSpacingX), mouseY - (helpTextY +edgeSpacingY), buttonPressed);

        if (mouseX > selectTextX +edgeSpacingX && mouseX < selectTextX + edgeSpacingX + selectTextWidth)
            if (mouseY > selectTextY +edgeSpacingY && mouseY < selectTextY + edgeSpacingY + selectTextHeight)
                return selectText.mouseClicked(mouseX - (selectTextX +edgeSpacingX), mouseY - (selectTextY +edgeSpacingY), buttonPressed);

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double direction)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (mouseX > helpTextX +edgeSpacingX && mouseX < helpTextX + edgeSpacingX + helpTextWidth)
            if (mouseY > helpTextY +edgeSpacingY && mouseY < helpTextY + edgeSpacingY + helpTextHeight)
                return helpText.mouseScrolled(direction);

        if (mouseX > selectTextX +edgeSpacingX && mouseX < selectTextX + edgeSpacingX + selectTextWidth)
            if (mouseY > selectTextY +edgeSpacingY && mouseY < selectTextY + edgeSpacingY + selectTextHeight)
                return selectText.mouseScrolled(direction);

        return false;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        helpText.setPos(edgeSpacingX + helpTextX, edgeSpacingY + helpTextY);
        helpText.setSize(helpTextWidth, helpTextHeight);
        helpText.setText(selectText.getText());
        helpText.render(this, font, PoseStack, mouseX, mouseY);

        selectText.setPos(edgeSpacingX +selectTextX, edgeSpacingY + selectTextY);
        selectText.setSize(selectTextWidth, selectTextHeight);
        selectText.render(this, font, PoseStack, mouseX, mouseY);
    }
}
