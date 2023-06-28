package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.TextField;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Client.GUI.*;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class BodyforgeScreen extends GenericTabScreen
{
    protected DropdownList bodyParts;
    protected DropdownList bodySubParts;
    protected DropdownList forgePart;

    protected GUIButton forge;
    protected GUIButton cancel;

    protected GUIButton description;
    protected GUIButton stats;

    protected final int bodyPartListXPos = 75;
    protected final int bodyPartListYPos = 50;

    protected final int bodySubPartListXPos = 75;
    protected final int bodySubPartListYPos = 75;

    protected final int forgeListYPos = 100;

    protected final int detailsMinXPos = 165;
    protected final int detailsMaxXPos = 245;
    protected final int detailsYPos = 50;
    protected final int detailsYHeight = 100;

    protected int cancelXPos = 118;
    protected final int cancelYPos = 110;

    protected int forgeXPos = xSize / 2;
    protected final int forgeYPos = 150;

    protected final int selectedTextXPos = xSize / 2;
    protected final int selectedTextYPos = 70;

    protected int oldMode = 0;
    protected int mode = 0;
    protected boolean descriptionMode = true;

    protected TextField partDescription = new TextField();

    public BodyforgeScreen()
    {
        super(2, Component.translatable("cultivationcraft.gui.bodyforge"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyforge.png"));

        String Selection = BodyModifications.getBodyModifications(genericClientFunctions.getPlayer()).getSelection();
        updateBodyPartList();

        String forgeString = Component.translatable("cultivationcraft.gui.forge").getString();
        forge = new GUIButton("FORGE", forgeString);

        String cancelString = Component.translatable("cultivationcraft.gui.cancel").getString();
        cancel = new GUIButton("CANCEL", cancelString);

        // Centre the buttons
        forgeXPos -= Minecraft.getInstance().font.width(forgeString) / 2;
        cancelXPos -= Minecraft.getInstance().font.width(cancelString) / 2;

        description = new GUIButton("DESCRIPTION", Component.translatable("cultivationcraft.gui.description").getString());
        description.select();
        stats = new GUIButton("STATS", Component.translatable("cultivationcraft.gui.stats").getString());
    }

    protected void updateBodyPartList()
    {
        bodyParts = new DropdownList();

        Player player = genericClientFunctions.getPlayer();

        // Create a list of all body part positions that can currently be modified
        ArrayList<String> positions = new ArrayList<String>();

        for (BodyPart part : BodyPartNames.getParts())
            if (!positions.contains(part.getPosition()) && part.canBeForged(player))
                positions.add(part.getPosition());

        for (BodyPartOption part : BodyPartNames.getOptions())
            if (!positions.contains(part.getPosition()) && part.canBeForged(player))
                positions.add(part.getPosition());

        // Add all valid positions into the DropdownList with the appropriate display name
        for (String pos : positions)
            bodyParts.addItem(BodyPartNames.getDisplayName(pos), pos);

        // If there are no valid positions make dropdown list blank
        if (positions.size() == 0)
            bodyParts.addItem("", "");

        updateBodySubPartList();
    }

    protected void updateBodySubPartList()
    {
        bodySubParts = new DropdownList();

        Player player = genericClientFunctions.getPlayer();
        ArrayList<String> positions = new ArrayList<String>();

        for (BodyPart part : BodyPartNames.getParts())
            if (equalsSelectedPosition(part.getPosition()) && !positions.contains(BodyPartNames.basePosition) && part.canBeForged(player))
                positions.add(BodyPartNames.basePosition);

        for (BodyPartOption part : BodyPartNames.getOptions())
            if (equalsSelectedPosition(part.getPosition()) && !positions.contains(part.getSubPosition()) && part.canBeForged(player))
                positions.add(part.getSubPosition());

        for (String pos : positions)
            bodySubParts.addItem(BodyPartNames.getDisplayName(getSelectedPosition(), pos), pos);

        // If no sub-positions are found make dropdown list blank
        if (positions.size() == 0)
            bodySubParts.addItem("", "");

        updateButtons();
    }

    protected void updateButtons()
    {
        forgePart = new DropdownList();

        Player player = genericClientFunctions.getPlayer();

        // Add a button for each part that can be forged in the selected subposition
        if (getSelectedSubPosition().compareTo(BodyPartNames.basePosition) == 0)
            for (BodyPart part : BodyPartNames.getParts())
            {
                if (equalsSelectedPosition(part.getPosition()) && part.canBeForged(player))
                {
                    forgePart.addItem(part.getDisplayName(), part.getID());
                }
            }
        else
            for (BodyPartOption part : BodyPartNames.getOptions())
                if (equalsSelectedPosition(part.getPosition()) && equalsSelectedSubPosition(part.getSubPosition()) && part.canBeForged(player))
                {
                    forgePart.addItem(part.getDisplayName(), part.getID());
                }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        // Change the selected position if a position is clicked in the dropdown list
        if (mode == 0)
        {
            String changed = (String) bodyParts.mouseClick((int) mouseX - (edgeSpacingX + bodyPartListXPos), (int) mouseY - (edgeSpacingY + bodyPartListYPos), buttonPressed);

            if (changed != null)
            {
                partDescription.resetScroll();
                updateBodySubPartList();
                return true;
            }

            if (bodySubParts.mouseClick((int) mouseX - (edgeSpacingX + bodySubPartListXPos), (int) mouseY - (edgeSpacingY + bodySubPartListYPos), buttonPressed) != null) {
                updateButtons();
                return true;
            }

            if (forgePart.mouseClick((int) mouseX - (edgeSpacingX + bodySubPartListXPos), (int) mouseY - (edgeSpacingY + forgeListYPos), buttonPressed) != null)
            {
                partDescription.resetScroll();
                return true;
            }

            // Send the selected part to the server if a part is selected and the forge button is pressed
            if (forge.mouseClick((int) mouseX - (edgeSpacingX + forgeXPos), (int) mouseY - (edgeSpacingY + forgeYPos), buttonPressed)) {
                if (forgePart.getSelected() != null)
                    ClientPacketHandler.sendBodyForgeSelectionToServer(forgePart.getSelected().toString());

                forge.unselect();

                return true;
            }

            if (description.mouseClick((int) mouseX - (edgeSpacingX + detailsMinXPos), (int) mouseY - (edgeSpacingY + detailsYPos - 20), buttonPressed))
            {
                description.select();
                stats.unselect();

                partDescription.resetScroll();
                descriptionMode = true;
            }
            else if (stats.mouseClick((int) mouseX - (edgeSpacingX + detailsMaxXPos - stats.width), (int) mouseY - (edgeSpacingY + detailsYPos - 20), buttonPressed))
            {
                stats.select();
                description.unselect();

                partDescription.resetScroll();
                descriptionMode = false;
            }

            if (mouseX > edgeSpacingX + detailsMinXPos && mouseX < edgeSpacingX + detailsMinXPos + (detailsMaxXPos - detailsMinXPos))
                if (mouseY > edgeSpacingY + detailsYPos && mouseY < edgeSpacingY + detailsYPos + detailsYHeight)
                    return partDescription.mouseClicked(mouseX - (edgeSpacingX + detailsMinXPos), mouseY - (edgeSpacingY + detailsYPos), buttonPressed);
        }

        // Cancel the selection if the cancel button is pressed
        if (cancel.mouseClick((int)mouseX - (edgeSpacingX + cancelXPos), (int)mouseY - (edgeSpacingY + cancelYPos), buttonPressed))
        {
            if (mode != 2)
                ClientPacketHandler.sendBodyForgeSelectionToServer("");
            else
                ClientPacketHandler.sendQuestCancelToServer();

            cancel.unselect();

            updateBodyPartList();

            return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double direction)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        // Scroll through the bodyPart list
        if (bodyParts.mouseScroll((int)mouseX - (edgeSpacingX + bodyPartListXPos), (int)mouseY - (edgeSpacingY + bodyPartListYPos), direction))
            return true;

        if (bodySubParts.mouseScroll((int)mouseX - (edgeSpacingX + bodySubPartListXPos), (int)mouseY - (edgeSpacingY + bodySubPartListYPos), direction))
            return true;

        if (mode == 0)
            if (mouseX > edgeSpacingX + detailsMinXPos && mouseX < edgeSpacingX + detailsMinXPos + (detailsMaxXPos - detailsMinXPos))
                if (mouseY > edgeSpacingY + detailsYPos && mouseY < edgeSpacingY + detailsYPos + detailsYHeight)
                    return partDescription.mouseScrolled(direction);

        return false;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        drawGuiForgroundLayer(PoseStack, partialTicks, mouseX, mouseY);
    }

    protected void drawGuiDetailsForgroundLayer(PoseStack PoseStack, int mouseX, int mouseY)
    {
        if (forgePart.getSelected() == null)
            return;

        BodyPart part = BodyPartNames.getPart(forgePart.getSelected().toString());
        if (part == null)
            part = BodyPartNames.getOption(forgePart.getSelected().toString());

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        partDescription.setPos(edgeSpacingX + detailsMinXPos, edgeSpacingY + detailsYPos);
        partDescription.setSize(detailsMaxXPos - detailsMinXPos, detailsYHeight);

        if (descriptionMode)
            partDescription.setText(part.getDescription());
        else
            partDescription.setText(part.getStatChanges().toString());

        partDescription.render(this, font, PoseStack, mouseX, mouseY);

        description.render(PoseStack, edgeSpacingX + detailsMinXPos, edgeSpacingY + detailsYPos - 20, mouseX, mouseY, this);
        stats.render(PoseStack, edgeSpacingX + detailsMaxXPos - stats.width, edgeSpacingY + detailsYPos - 20, mouseX, mouseY, this);
    }

    protected void drawGuiForgroundLayer(PoseStack PoseStack, float partialTicks, int mouseX, int mouseY)
    {
        drawBody(PoseStack);

        // If a part has already been selected then draw information on that selection
        // Otherwise draw the part selection menu
        IBodyModifications modifications = BodyModifications.getBodyModifications(genericClientFunctions.getPlayer());

        String questPart = modifications.getLastForged();

        oldMode = mode;

        // If there is a current in-progress quest part then draw the quest screen, otherwise draw the part screen
        if (questPart.compareTo("") != 0)
        {
            BodyPart part = BodyPartNames.getPart(questPart);
            if (part == null)
                part = BodyPartNames.getOption(questPart);

            drawQuest(PoseStack, part, mouseX, mouseY);
        }
        else
        {
            String Selection = modifications.getSelection();
            BodyPart part = BodyPartNames.getPart(Selection);
            if (part == null)
                part = BodyPartNames.getOption(Selection);

            if (part == null)
                drawSelection(PoseStack, mouseX, mouseY);
            else
                drawSelected(PoseStack, part, mouseX, mouseY);
        }
    }

    protected void drawSelection(PoseStack PoseStack, int mouseX, int mouseY)
    {
        mode = 0;

        if (oldMode != mode)
            updateBodyPartList();

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        forge.render(PoseStack, edgeSpacingX + forgeXPos, edgeSpacingY + forgeYPos, mouseX, mouseY, this);

        // Render the BodyPart dropdown lists
        forgePart.render(PoseStack, edgeSpacingX + bodyPartListXPos, edgeSpacingY + forgeListYPos, mouseX, mouseY, this);
        bodySubParts.render(PoseStack, edgeSpacingX + bodySubPartListXPos, edgeSpacingY + bodySubPartListYPos, mouseX, mouseY, this);
        bodyParts.render(PoseStack, edgeSpacingX + bodyPartListXPos, edgeSpacingY + bodyPartListYPos, mouseX, mouseY, this);

        drawGuiDetailsForgroundLayer(PoseStack, mouseX, mouseY);
    }

    protected void drawSelected(PoseStack PoseStack, BodyPart part, int mouseX, int mouseY)
    {
        mode = 1;

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        String position = BodyPartNames.getDisplayName(part.getPosition());

        String subPosition = BodyPartNames.getDisplayName(BodyPartNames.basePosition);
        if (part instanceof BodyPartOption)
            subPosition = BodyPartNames.getDisplayName(part.getPosition(), ((BodyPartOption)part).getSubPosition());

        font.draw(PoseStack, position,edgeSpacingX + selectedTextXPos - font.width(position) / 2, edgeSpacingY + selectedTextYPos, Color.GRAY.getRGB());
        font.draw(PoseStack, subPosition, edgeSpacingX + selectedTextXPos- font.width(subPosition) / 2, edgeSpacingY + selectedTextYPos + font.lineHeight, Color.GRAY.getRGB());
        font.draw(PoseStack,part.getDisplayName(), edgeSpacingX + selectedTextXPos - font.width(part.getDisplayName()) / 2, edgeSpacingY + selectedTextYPos + font.lineHeight*2, Color.BLACK.getRGB());

        // Render the progress bar
        IBodyModifications modifications = BodyModifications.getBodyModifications(genericClientFunctions.getPlayer());
        float progress = (float)modifications.getProgress() / (float) BodyPartStatControl.getStats(genericClientFunctions.getPlayer()).getStat(StatIDs.qiCost);

        RenderSystem.setShaderTexture(0, TEXTURE);

        int length = xSize - (9 * 2);

        this.blit(PoseStack, edgeSpacingX + 9, edgeSpacingY + 160, 0, 233, length, 6);
        this.blit(PoseStack, edgeSpacingX + 10, edgeSpacingY + 161, 1, 239, (int)((length - 2) * progress), 4);

        cancel.render(PoseStack, edgeSpacingX + cancelXPos, edgeSpacingY + cancelYPos, mouseX, mouseY, this);
    }

    protected void drawQuest(PoseStack PoseStack, BodyPart part, int mouseX, int mouseY)
    {
        mode = 2;

        Quest quest = part.getQuest();
        double progress = QuestHandler.getQuestProgress(Minecraft.getInstance().player);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        String partName = part.getDisplayName();

        String position = BodyPartNames.getDisplayName(part.getPosition());

        String subPosition = BodyPartNames.getDisplayName(BodyPartNames.basePosition);
        if (part instanceof BodyPartOption)
            subPosition = BodyPartNames.getDisplayName(part.getPosition(), ((BodyPartOption)part).getSubPosition());

        String stabilizing = Component.translatable("cultivationcraft.gui.generic.stabilizing").getString();

        font.draw(PoseStack, stabilizing, edgeSpacingX + bodyPartListXPos, edgeSpacingY + 30, Color.BLACK.getRGB());
        font.draw(PoseStack,  position + "->" + subPosition + "->" + partName, edgeSpacingX + bodyPartListXPos, edgeSpacingY + 45, Color.GRAY.getRGB());


        font.draw(PoseStack, quest.getDescription(), edgeSpacingX + 10, edgeSpacingY + 130, Color.GRAY.getRGB());
        font.draw(PoseStack, (int)progress + "/" + (int)quest.complete, edgeSpacingX + bodyPartListXPos, edgeSpacingY + 145, Color.GRAY.getRGB());

        RenderSystem.setShaderTexture(0, TEXTURE);

        int length = xSize - (9 * 2);

        this.blit(PoseStack, edgeSpacingX + 9, edgeSpacingY + 160, 0, 233, length, 6);
        this.blit(PoseStack, edgeSpacingX + 10, edgeSpacingY + 161, 1, 239, (int)((length - 2) * (progress / quest.complete)), 4);

        cancel.render(PoseStack, edgeSpacingX + cancelXPos, edgeSpacingY + cancelYPos, mouseX, mouseY, this);
    }

    protected void drawBody(PoseStack PoseStack)
    {
        // TODO: Lock and unlock the body whilst drawing to prevent crashes from changing body parts

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        // Draw the players body parts
        int bodyPosX = edgeSpacingX + 30;
        int bodyPosY = edgeSpacingY + 60;

        IBodyModifications modifications = BodyModifications.getBodyModifications(genericClientFunctions.getPlayer());

        // Get the body GUI, this is special as all other GUIs positions are based off this
        BodyPartGUI base;

        base = BodyPartGUIs.getGUI(BodyPartNames.DefaultBody).get(0);

        if (modifications.hasModification(BodyPartNames.bodyPosition))
            base = BodyPartGUIs.getGUI(modifications.getModification(BodyPartNames.bodyPosition).getID()).get(0);

        boolean highlight = false;
        if (equalsSelectedPosition(BodyPartNames.bodyPosition))
            highlight = true;

        base.render(PoseStack, bodyPosX, bodyPosY, highlight, this);


        // Draw generic body parts if alterations don't exist
        highlight = false;
        if (!modifications.hasModification(BodyPartNames.headPosition))
        {
            if (equalsSelectedPosition(BodyPartNames.headPosition))
                highlight = true;

            BodyPartGUIs.getGUI(BodyPartNames.DefaultHead).get(0).render(PoseStack, bodyPosX, bodyPosY, highlight, this, base);
        }


        highlight = false;
        if (!modifications.hasModification(BodyPartNames.armPosition))
        {
            if (equalsSelectedPosition(BodyPartNames.armPosition))
                highlight = true;

            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightArm).get(0).render(PoseStack, bodyPosX, bodyPosY, highlight, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftArm).get(0).render(PoseStack, bodyPosX, bodyPosY, highlight,this, base);
        }

        highlight = false;
        if (!modifications.hasModification(BodyPartNames.legPosition))
        {
            if (equalsSelectedPosition(BodyPartNames.legPosition))
                highlight = true;

            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightLeg).get(0).render(PoseStack, bodyPosX, bodyPosY, highlight, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftLeg).get(0).render(PoseStack, bodyPosX, bodyPosY, highlight, this, base);
        }

        // Loop through and draw all other body part GUIs if they exist
        for (Map.Entry<String, BodyPart> entry : modifications.getModifications().entrySet())
        {
            // Don't redraw the body
            if (entry.getKey() != BodyPartNames.bodyPosition)
            {
                 highlight = false;

                 if (equalsSelectedPosition(entry.getValue().getPosition()))
                    highlight = true;

                 ArrayList<BodyPartGUI> guis = BodyPartGUIs.getGUI(entry.getValue().getID());

                 for (BodyPartGUI gui : guis)
                    if (gui != null)
                        gui.render(PoseStack, bodyPosX, bodyPosY, highlight, this, base);
            }
        }
    }

    protected String getSelectedPosition()
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(genericClientFunctions.getPlayer());

        // Check against the position selected in the drop down menu
        String selected = ((String)bodyParts.getSelected());

        // Change the position to check to the selected part's position if one exists
        String Selection = modifications.getSelection();

        if (mode == 2)
            Selection = modifications.getLastForged();

        BodyPart part = BodyPartNames.getPartOrOption(Selection);
        if (part != null)
            selected = part.getPosition();

        if (selected == null)
            selected = "";

        return selected;
    }

    protected String getSelectedSubPosition()
    {
        String selected = ((String)bodySubParts.getSelected());
        if (selected == null)
            selected = "";

        return selected;
    }
    protected Boolean equalsSelectedPosition(String compare)
    {
        if (getSelectedPosition().compareTo(compare) == 0)
            return true;

        return false;
    }

    protected Boolean equalsSelectedSubPosition(String compare)
    {
        if (getSelectedSubPosition().compareTo(compare) == 0)
            return true;

        return false;
    }
}
