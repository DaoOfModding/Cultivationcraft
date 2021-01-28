package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class BodyforgeScreen extends Screen
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyforge.png");

    private DropdownList bodyParts;
    private DropdownList bodySubParts;

    private ArrayList<GUIButton> buttons = new ArrayList<GUIButton>();
    private GUIButton forge;
    private GUIButton cancel;

    private final int bodyPartListXPos = 75;
    private final int bodyPartListYPos = 50;

    private final int bodySubPartListXPos = 75;
    private final int bodySubPartListYPos = 75;

    private int forgeXPos = 83;
    private final int forgeYPos = 150;

    private int cancelXPos = 118;
    private final int cancelYPos = 110;

    private final int buttonMinXPos = 75;
    private final int buttonMaxXPos = 170;

    private final int buttonMinYPos = 100;

    private final int selectedTextXPos = 122;
    private final int selectedTextYPos = 70;

    private final int xSize = 175;
    private final int ySize = 178;

    private String selectedPart = null;

    private int mode = 0;

    public BodyforgeScreen()
    {
        super(new TranslationTextComponent("cultivationcraft.gui.bodyforge"));

        String Selection = BodyModifications.getBodyModifications(Minecraft.getInstance().player).getSelection();
        updateBodyPartList();

        String forgeString = new TranslationTextComponent("cultivationcraft.gui.forge").getString();
        forge = new GUIButton("FORGE", forgeString);

        String cancelString = new TranslationTextComponent("cultivationcraft.gui.cancel").getString();
        cancel = new GUIButton("CANCEL", cancelString);

        // Centre the buttons
        forgeXPos -= Minecraft.getInstance().fontRenderer.getStringWidth(forgeString) / 2;
        cancelXPos -= Minecraft.getInstance().fontRenderer.getStringWidth(cancelString) / 2;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    private void updateBodyPartList()
    {
        bodyParts = new DropdownList();

        ClientPlayerEntity player = Minecraft.getInstance().player;

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

    private void updateBodySubPartList()
    {
        bodySubParts = new DropdownList();

        ClientPlayerEntity player = Minecraft.getInstance().player;
        ArrayList<String> positions = new ArrayList<String>();

        for (BodyPart part : BodyPartNames.getParts())
            if (equalsSelectedPosition(part.getPosition()) && !positions.contains(BodyPartNames.basePosition) && part.canBeForged(player))
                positions.add(BodyPartNames.basePosition);

        for (BodyPartOption part : BodyPartNames.getOptions())
            if (equalsSelectedPosition(part.getPosition()) && !positions.contains(part.getSubPosition()) && part.canBeForged(player))
                positions.add(part.getSubPosition());

        for (String pos : positions)
            bodySubParts.addItem(BodyPartNames.getDisplayName((String)bodyParts.getSelected(), pos), pos);

        // If no sub-positions are found make dropdown list blank
        if (positions.size() == 0)
            bodySubParts.addItem("", "");

        updateButtons();
    }

    private void updateButtons()
    {
        selectedPart = null;
        buttons.clear();

        ClientPlayerEntity player = Minecraft.getInstance().player;

        // Add a button for each part that can be forged in the selected subposition
        if (getSelectedSubPosition().compareTo(BodyPartNames.basePosition) == 0)
            for (BodyPart part : BodyPartNames.getParts())
            {
                if (equalsSelectedPosition(part.getPosition()) && part.canBeForged(player))
                {
                    GUIButton button = new GUIButton(part.getID(), part.getDisplayName());
                    buttons.add(button);
                }
            }
        else
            for (BodyPartOption part : BodyPartNames.getOptions())
                if (equalsSelectedPosition(part.getPosition()) && equalsSelectedSubPosition(part.getSubPosition()) && part.canBeForged(player))
                {
                    GUIButton button = new GUIButton(part.getID(), part.getDisplayName());
                    buttons.add(button);
                }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (ScreenTabControl.mouseClick((int)mouseX, (int)mouseY, edgeSpacingX, edgeSpacingY, buttonPressed))
            return true;

        // Change the selected position if a position is clicked in the dropdown list
        String changed = (String)bodyParts.mouseClick((int)mouseX - (edgeSpacingX + bodyPartListXPos), (int)mouseY - (edgeSpacingY + bodyPartListYPos), buttonPressed);

        if (changed != null)
        {
            updateBodySubPartList();
            return true;
        }

        if (bodySubParts.mouseClick((int)mouseX - (edgeSpacingX + bodySubPartListXPos), (int)mouseY - (edgeSpacingY + bodySubPartListYPos), buttonPressed) != null)
        {
            updateButtons();
            return true;
        }

        // Cancel the selection if the cancel button is pressed
        if (cancel.mouseClick((int)mouseX - (edgeSpacingX + cancelXPos), (int)mouseY - (edgeSpacingY + cancelYPos), buttonPressed))
        {
            ClientPacketHandler.sendBodyForgeSelectionToServer("");
            cancel.unselect();

            return true;
        }

        // Send the selected part to the server if a part is selected and the forge button is pressed
        if (forge.mouseClick((int)mouseX - (edgeSpacingX + forgeXPos), (int)mouseY - (edgeSpacingY + forgeYPos), buttonPressed))
        {
            if (selectedPart != null)
                ClientPacketHandler.sendBodyForgeSelectionToServer(selectedPart);

            forge.unselect();

            return true;
        }

        int xpos = buttonMinXPos;
        int ypos = 0;
        for (GUIButton button : buttons)
        {
            // If there is not enough space for this button, move onto the next line
            if (xpos + button.width > buttonMaxXPos)
            {
                ypos++;
                xpos = buttonMinXPos;
            }

            int currentY = buttonMinYPos + ypos * (4 + GUIButton.height);

            if (button.mouseClick((int)mouseX - (edgeSpacingX + xpos), (int)mouseY - (edgeSpacingY + currentY), buttonPressed))
            {
                changeSelection(button);
                return true;
            }

            // Move the xpos next to this button
            xpos += button.width + 3;
        }

        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        return false;
    }

    public void changeSelection(GUIButton selected)
    {
        // If the button has been turned off
        // Turn it back on and do nothing else
        if (!selected.isSelected())
        {
            selected.select();
            return;
        }

        selectedPart = selected.getID();

        for (GUIButton button : buttons)
            if (button != selected)
                button.unselect();
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

        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        drawGuiBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        drawGuiForgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    }

    protected void drawGuiForgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        // If a part has already been selected then draw information on that selection
        // Otherwise draw the part selection menu
        String Selection = BodyModifications.getBodyModifications(Minecraft.getInstance().player).getSelection();
        BodyPart part = BodyPartNames.getPart(Selection);
        if (part == null)
            part = BodyPartNames.getOption(Selection);

        if (part == null)
            drawSelection(matrixStack, mouseX, mouseY);
        else
            drawSelected(matrixStack, part, mouseX, mouseY);
    }

    protected void drawGuiBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);

        ScreenTabControl.highlightTabs(matrixStack, 2, mouseX, mouseY, edgeSpacingX, edgeSpacingY, this);

        drawBody(matrixStack);
    }

    private void drawSelection(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        if (mode == 1)
            updateBodyPartList();

        mode = 0;

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        // Render part buttons
        int xpos = buttonMinXPos;
        int ypos = 0;
        for (GUIButton button : buttons)
        {
            // If there is not enough space for this button, move onto the next line
            if (xpos + button.width > buttonMaxXPos)
            {
                ypos++;
                xpos = buttonMinXPos;
            }

            int currentY = buttonMinYPos + ypos * (4 + GUIButton.height);

            button.render(matrixStack, edgeSpacingX + xpos, edgeSpacingY + currentY, mouseX, mouseY, this);

            // Move the xpos next to this button
            xpos += button.width + 3;
        }

        forge.render(matrixStack, edgeSpacingX + forgeXPos, edgeSpacingY + forgeYPos, mouseX, mouseY, this);

        // Render the BodyPart dropdown list
        bodySubParts.render(matrixStack, edgeSpacingX + bodySubPartListXPos, edgeSpacingY + bodySubPartListYPos, mouseX, mouseY, this);
        bodyParts.render(matrixStack, edgeSpacingX + bodyPartListXPos, edgeSpacingY + bodyPartListYPos, mouseX, mouseY, this);
    }

    private void drawSelected(MatrixStack matrixStack, BodyPart part, int mouseX, int mouseY)
    {
        mode = 1;

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        String position = BodyPartNames.getDisplayName(part.getPosition());

        String subPosition = BodyPartNames.getDisplayName(BodyPartNames.basePosition);
        if (part instanceof BodyPartOption)
            subPosition = BodyPartNames.getDisplayName(part.getPosition(), ((BodyPartOption)part).getSubPosition());

        font.drawString(matrixStack, position,edgeSpacingX + selectedTextXPos - font.getStringWidth(position) / 2, edgeSpacingY + selectedTextYPos, Color.GRAY.getRGB());
        font.drawString(matrixStack, subPosition, edgeSpacingX + selectedTextXPos- font.getStringWidth(subPosition) / 2, edgeSpacingY + selectedTextYPos + font.FONT_HEIGHT, Color.GRAY.getRGB());
        font.drawString(matrixStack,part.getDisplayName(), edgeSpacingX + selectedTextXPos - font.getStringWidth(part.getDisplayName()) / 2, edgeSpacingY + selectedTextYPos + font.FONT_HEIGHT*2, Color.BLACK.getRGB());

        // Render the progress bar
        IBodyModifications modifications = BodyModifications.getBodyModifications(Minecraft.getInstance().player);
        float progress = (float)modifications.getProgress() / (float)part.getQiNeeded();

        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);

        this.blit(matrixStack, edgeSpacingX + 9, edgeSpacingY + 160, 0, 246, 157, 6);
        this.blit(matrixStack, edgeSpacingX + 10, edgeSpacingY + 161, 1, 252, (int)(155 * progress), 4);

        cancel.render(matrixStack, edgeSpacingX + cancelXPos, edgeSpacingY + cancelYPos, mouseX, mouseY, this);
    }

    protected void drawBody(MatrixStack matrixStack)
    {
        // TODO: Lock and unlock the body whilst drawing to prevent crashes from changing body parts

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        // Draw the players body parts
        int bodyPosX = edgeSpacingX + 30;
        int bodyPosY = edgeSpacingY + 60;

        IBodyModifications modifications = BodyModifications.getBodyModifications(Minecraft.getInstance().player);

        // Get the body GUI, this is special as all other GUIs positions are based off this
        BodyPartGUI base;

        base = BodyPartGUIs.getGUI(BodyPartNames.DefaultBody).get(0);

        if (modifications.hasModification(BodyPartNames.bodyPosition))
            base = BodyPartGUIs.getGUI(modifications.getModification(BodyPartNames.bodyPosition).getID()).get(0);

        boolean highlight = false;
        if (equalsSelectedPosition(BodyPartNames.bodyPosition))
            highlight = true;

        base.render(matrixStack, bodyPosX, bodyPosY, highlight, this);


        // Draw generic body parts if alterations don't exist
        highlight = false;
        if (!modifications.hasModification(BodyPartNames.headPosition))
        {
            if (equalsSelectedPosition(BodyPartNames.headPosition))
                highlight = true;

            BodyPartGUIs.getGUI(BodyPartNames.DefaultHead).get(0).render(matrixStack, bodyPosX, bodyPosY, highlight, this, base);
        }


        highlight = false;
        if (!modifications.hasModification(BodyPartNames.armPosition))
        {
            if (equalsSelectedPosition(BodyPartNames.armPosition))
                highlight = true;

            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightArm).get(0).render(matrixStack, bodyPosX, bodyPosY, highlight, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftArm).get(0).render(matrixStack, bodyPosX, bodyPosY, highlight,this, base);
        }

        highlight = false;
        if (!modifications.hasModification(BodyPartNames.legPosition))
        {
            if (equalsSelectedPosition(BodyPartNames.legPosition))
                highlight = true;

            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightLeg).get(0).render(matrixStack, bodyPosX, bodyPosY, highlight, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftLeg).get(0).render(matrixStack, bodyPosX, bodyPosY, highlight, this, base);
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
                        gui.render(matrixStack, bodyPosX, bodyPosY, highlight, this, base);
            }
        }
    }

    private String getSelectedPosition()
    {
        // Check against the position selected in the drop down menu
        String selected = ((String)bodyParts.getSelected());

        // Change the position to check to the selected part's position if one exists
        String Selection = BodyModifications.getBodyModifications(Minecraft.getInstance().player).getSelection();
        BodyPart part = BodyPartNames.getPart(Selection);
        if (part != null)
            selected = part.getPosition();

        if (selected == null)
            selected = "";

        return selected;
    }

    private String getSelectedSubPosition()
    {
        String selected = ((String)bodySubParts.getSelected());
        if (selected == null)
            selected = "";

        return selected;
    }
    private Boolean equalsSelectedPosition(String compare)
    {
        if (getSelectedPosition().compareTo(compare) == 0)
            return true;

        return false;
    }

    private Boolean equalsSelectedSubPosition(String compare)
    {
        if (getSelectedSubPosition().compareTo(compare) == 0)
            return true;

        return false;
    }
}
