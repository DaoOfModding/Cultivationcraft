package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.GUIButton;
import DaoOfModding.Cultivationcraft.Client.GUI.TextField;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Client.GUI.DropdownList;
import DaoOfModding.Cultivationcraft.Client.GUI.TechniqueIcons;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class TechniqueScreen extends GenericTabScreen
{
    protected DropdownList techniques;

    public static int selected = 0;

    protected final int techniqueXPos = (xSize - 85) / 2;
    protected final int techniqueYPos = 30;

    protected GUIButton description;
    protected GUIButton stats;

    protected GUIButton modify;

    protected TextField partDescription = new TextField();

    public TechniqueScreen()
    {
        super(1, Component.translatable("cultivationcraft.gui.technique"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/technique.png"));

        updateTechniqueList();

        partDescription.setSize(xSize - 60, 80);

        description = new GUIButton("DESCRIPTION", Component.translatable("cultivationcraft.gui.description").getString());
        description.select();
        stats = new GUIButton("STATS", Component.translatable("cultivationcraft.gui.stats").getString());

        modify = new GUIButton("MODIFY", Component.translatable("cultivationcraft.gui.modify").getString());
    }

    protected void updateTechniqueList()
    {
        techniques = new DropdownList();

        techniques.addItem(Component.translatable("cultivationcraft.gui.notechnique").getString(), Technique.class);

        ICultivatorTechniques techList = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer());

        for (Class tech : TechniqueControl.getAvailableTechnics(genericClientFunctions.getPlayer()))
        {
            try
            {
                Technique technique = (Technique) (tech.newInstance());

                // Add the technique if it is valid and either multiple copies are allowed or it isn't already set
                if (technique.isValid(genericClientFunctions.getPlayer()) && (technique.allowMultiple() || !techList.techniqueExists(technique)))
                    techniques.addItem(technique.getName(), tech);
            }
            catch (Exception e)
            {
                Cultivationcraft.LOGGER.error(tech.getName() + " is not a Technique: " + e.getMessage());
            }
        }

        updateSelection();
    }

    // Update the settings screen to the currently selected technique
    protected void updateSelection()
    {
        Technique selectedTech = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer()).getTechnique(selected);

        if (selectedTech != null)
            techniques.changeSelection(selectedTech.getName());
        else
            techniques.changeSelection(Component.translatable("cultivationcraft.gui.notechnique").getString());
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        drawGuiForgroundLayer(PoseStack, partialTicks, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double direction)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (techniques.mouseScroll((int)mouseX - (edgeSpacingX + techniqueXPos), (int)mouseY - (edgeSpacingY + techniqueYPos), direction))
            return true;

        return false;
    }

    // Change the selected technique to a new technique of the specified type
    public void changeTechnique(Class newTech)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer());

        Technique newTechnique = null;

        try
        {
            if (newTech != Technique.class)
                newTechnique = (Technique)newTech.newInstance();
        }
        catch (Exception e)
        {
            Cultivationcraft.LOGGER.warn("Error assigning technique: " + e);
        }

        if (newTechnique != null && !newTechnique.allowMultiple())
            if (techs.techniqueExists(newTechnique))
            {
                Cultivationcraft.LOGGER.warn("Tried to assign technique that already exists");
                return;
            }

        if (techs.getTechnique(selected) != null && techs.getTechnique(selected).isActive())
            techs.getTechnique(selected).deactivate(Minecraft.getInstance().player);

        techs.setTechnique(selected, newTechnique);
        updateTechniqueList();

        ClientPacketHandler.sendCultivatorTechniquesToServer();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        if (partDescription.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;


        Class changed = (Class)techniques.mouseClick((int)mouseX - (edgeSpacingX + techniqueXPos), (int)mouseY - (edgeSpacingY + techniqueYPos), buttonPressed);

        // If the technique has been changed send that change to the server
        if (changed != null)
        {
            changeTechnique(changed);
            return true;
        }

        if (buttonPressed == 0)
        {
            int techniqueSelected = TechniqueIcons.mouseOver(edgeSpacingX + 48, edgeSpacingY + 155, (int) mouseX, (int) mouseY, 18);

            if (techniqueSelected != -1)
                changeSelection(techniqueSelected);
        }

        if (description.mouseClick((int)mouseX, (int)mouseY, buttonPressed))
        {
            stats.unselect();
            description.select();

            return true;
        }
        else if (stats.mouseClick((int)mouseX, (int)mouseY, buttonPressed))
        {
            description.unselect();
            stats.select();

            return true;
        }

        Technique selectedTech = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer()).getTechnique(selected);

        if (selectedTech != null && selectedTech.canLevel() && modify.mouseClick((int)mouseX, (int)mouseY, buttonPressed))
        {
            Minecraft.getInstance().forceSetScreen(new TechniqueModifyScreen(selected));
            return true;
        }

        return false;
    }

    public void changeSelection(int newSelection)
    {
        selected = newSelection;
        updateSelection();
    }

    protected void drawGuiForgroundLayer(PoseStack PoseStack, float partialTicks, int mouseX, int mouseY)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        // Draw icons in the selection box
        TechniqueIcons.renderIcons(PoseStack, edgeSpacingX + 48,edgeSpacingY + 155, this, 18);

        // Highlight the selected technique and the technique the mouse is hovering over
        TechniqueIcons.mouseOverHighlight(PoseStack, edgeSpacingX + 48,edgeSpacingY + 155, this, 18, mouseX, mouseY);
        TechniqueIcons.highlightIcon(PoseStack, edgeSpacingX + 48,edgeSpacingY + 155, this, 18, selected);

        Technique selectedTech = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer()).getTechnique(selected);

        if (selectedTech == null)
            partDescription.setText("");
        else if (description.isSelected())
            partDescription.setText(selectedTech.getDescription());
        else if (stats.isSelected())
            partDescription.setText(selectedTech.getStats().toString() + selectedTech.getTechniqueStatString());

        description.setPos(edgeSpacingX + 127 - description.width - 10, edgeSpacingY + techniqueYPos + 15);
        description.render(PoseStack, mouseX, mouseY, this);
        stats.setPos(edgeSpacingX + 127 + 10, edgeSpacingY + techniqueYPos + 15);
        stats.render(PoseStack, mouseX, mouseY, this);

        if (selectedTech != null && selectedTech.canLevel())
        {
            modify.setPos(edgeSpacingX + 127 + 20 + modify.width, edgeSpacingY + techniqueYPos + 15);
            modify.render(PoseStack, mouseX, mouseY, this);
        }

        partDescription.setPos(edgeSpacingX + 30, edgeSpacingY + techniqueYPos + 30);
        partDescription.render(this, font, PoseStack, mouseX, mouseY);

        // Render the techniques dropdown list
        techniques.render(PoseStack, edgeSpacingX + techniqueXPos, edgeSpacingY + techniqueYPos, mouseX, mouseY, this);
    }
}
