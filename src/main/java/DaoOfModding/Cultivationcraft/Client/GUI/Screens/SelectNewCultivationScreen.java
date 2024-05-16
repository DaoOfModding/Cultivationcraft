package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.GUIButton;
import DaoOfModding.Cultivationcraft.Client.GUI.SelectableTextField;
import DaoOfModding.Cultivationcraft.Client.GUI.TextField;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class SelectNewCultivationScreen extends Screen
{
    protected ResourceLocation TEXTURE;

    public final int xSize = 256;
    public final int ySize = 178;

    protected int descTextX = 90;
    protected int descTextY = 35;
    protected int descTextWidth = 150;
    protected int descTextHeight = 101;

    protected int selectTextX = 10;
    protected int selectTextY = 35;
    protected int selectTextWidth = 70;
    protected int selectTextHeight = 101;

    protected int nameTextY = 22;

    protected TextField description = new TextField();
    protected SelectableTextField selection = new SelectableTextField();

    protected GUIButton select;

    protected int selectButtonX = 127;
    protected int selectButtonY = 145;

    protected String name = Component.translatable("cultivationcraft.gui.advanceselection").getString();

    public SelectNewCultivationScreen()
    {
        super(Component.translatable("cultivationcraft.gui.cultivationselection"));

        TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/blank.png");

        description.resetScroll();

        for (CultivationType advance : CultivatorStats.getCultivatorStats(genericClientFunctions.getPlayer()).getCultivation().getAdvancements(Minecraft.getInstance().player))
        {
            selection.addSelectable(new SelectableText(advance.getID()));
        }

        selection.setSize(selectTextWidth, selectTextHeight, (int)(selectTextWidth * 2));

        select = new GUIButton("advance", Component.translatable("cultivationcraft.gui.advance").getString());
        select.disable();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        if (select.mouseClick((int)mouseX , (int)mouseY, buttonPressed))
        {
            ClientPacketHandler.sendBreakthroughToServer(selection.getSelected().getID().toString());
            this.onClose();

            return true;
        }

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (mouseX > selectTextX +edgeSpacingX && mouseX < selectTextX + edgeSpacingX + selection.getWidth())
            if (mouseY > selectTextY +edgeSpacingY && mouseY < selectTextY + edgeSpacingY + selectTextHeight)
                selection.mouseClicked(mouseX - (selectTextX +edgeSpacingX), mouseY - (selectTextY +edgeSpacingY), buttonPressed);

        if (mouseX > descTextX +edgeSpacingX && mouseX < descTextX + edgeSpacingX + descTextWidth)
            if (mouseY > descTextY +edgeSpacingY && mouseY < descTextY + edgeSpacingY + descTextHeight)
                return description.mouseClicked(mouseX - (descTextX +edgeSpacingX), mouseY - (descTextY +edgeSpacingY), buttonPressed);

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double direction)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (mouseX > selectTextX +edgeSpacingX && mouseX < selectTextX + edgeSpacingX + selection.getWidth())
            if (mouseY > selectTextY +edgeSpacingY && mouseY < selectTextY + edgeSpacingY + selectTextHeight)
                return selection.mouseScrolled(direction);

        if (mouseX > descTextX +edgeSpacingX && mouseX < descTextX + edgeSpacingX + descTextWidth)
            if (mouseY > descTextY +edgeSpacingY && mouseY < descTextY + edgeSpacingY + descTextHeight)
                return description.mouseScrolled(direction);

        return false;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.blit(PoseStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);

        font.draw(PoseStack, name, edgeSpacingX + (int)(this.xSize / 2) - (int)(font.width(name) / 2),  edgeSpacingY + nameTextY, Color.darkGray.getRGB());

        description.setPos(edgeSpacingX + descTextX, edgeSpacingY + descTextY);
        description.setSize(descTextWidth, descTextHeight);
        description.setText(selection.getText());
        description.render(this, font, PoseStack, mouseX, mouseY);

        selection.setPos(edgeSpacingX +selectTextX, edgeSpacingY + selectTextY);
        selection.render(this, font, PoseStack, mouseX, mouseY);

        if (selection.getSelected() != null)
            select.enable();
        else
            select.disable();

        select.setPos(edgeSpacingX + selectButtonX - select.width / 2, selectButtonY + edgeSpacingY);
        select.render(PoseStack, mouseX, mouseY, this);
    }
}
