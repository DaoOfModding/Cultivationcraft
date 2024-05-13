package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.HelpItems;
import DaoOfModding.Cultivationcraft.Client.GUI.SelectableTextField;
import DaoOfModding.Cultivationcraft.Client.GUI.TextField;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConceptScreen extends GenericTabScreen
{
    protected int conceptTextX = 100;
    protected int conceptTextY = 35;
    protected int conceptTextWidth = 140;
    protected int conceptTextHeight = 121;

    protected int selectTextX = 10;
    protected int selectTextY = 35;
    protected int selectTextWidth = 80;
    protected int selectTextHeight = 121;

    protected int nameTextY = 22;

    protected TextField conceptText = new TextField();
    protected SelectableTextField selectText = new SelectableTextField();

    public ConceptScreen()
    {
        super(0, Component.translatable("cultivationcraft.gui.concept"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/stats.png"));

        generateText();

        selectText.setSize(selectTextWidth, selectTextHeight);
    }

    public void generateText()
    {
        conceptText.resetScroll();

        HashMap<ResourceLocation, ArrayList<ResourceLocation>> categories = new HashMap<>();
        selectText = new SelectableTextField();

        for (TechniqueModifier modifier : ExternalCultivationHandler.getModifiers())
        {
            if (modifier.canUse(Minecraft.getInstance().player) && modifier.hasLearnt(Minecraft.getInstance().player))
            {
                if (!categories.containsKey(modifier.CATEGORY))
                    categories.put(modifier.CATEGORY, new ArrayList<>());

                categories.get(modifier.CATEGORY).add(modifier.ID);
            }
        }

        for (Map.Entry<ResourceLocation, ArrayList<ResourceLocation>> entry : categories.entrySet())
        {
            SelectableText category = new SelectableText(entry.getKey().toShortLanguageKey());

            for (ResourceLocation location : entry.getValue())
                category.addItem(new SelectableText(location.toShortLanguageKey()));

            selectText.addSelectable(category);
        }
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

        if (mouseX > conceptTextX +edgeSpacingX && mouseX < conceptTextX + edgeSpacingX + conceptTextWidth)
            if (mouseY > conceptTextY +edgeSpacingY && mouseY < conceptTextY + edgeSpacingY + conceptTextHeight)
                return conceptText.mouseClicked(mouseX - (conceptTextX +edgeSpacingX), mouseY - (conceptTextY +edgeSpacingY), buttonPressed);

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

        if (mouseX > conceptTextX +edgeSpacingX && mouseX < conceptTextX + edgeSpacingX + conceptTextWidth)
            if (mouseY > conceptTextY +edgeSpacingY && mouseY < conceptTextY + edgeSpacingY + conceptTextHeight)
                return conceptText.mouseScrolled(direction);

        return false;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        String text = selectText.getText();

        if (selectText.getSelected() != null)
        {
            String name = Component.translatable(selectText.getSelected().getName()) + " " + Component.translatable("cultivationcraft.gui.concept");
            font.draw(PoseStack, name, edgeSpacingX + (int) (this.xSize / 2) - (int) (font.width(name) / 2), edgeSpacingY + nameTextY, Color.darkGray.getRGB());
        }

        conceptText.setPos(edgeSpacingX + conceptTextX, edgeSpacingY + conceptTextY);
        conceptText.setSize(conceptTextWidth, conceptTextHeight);
        conceptText.setText(text);
        conceptText.render(this, font, PoseStack, mouseX, mouseY);

        selectText.setPos(edgeSpacingX +selectTextX, edgeSpacingY + selectTextY);
        selectText.render(this, font, PoseStack, mouseX, mouseY);
    }
}
