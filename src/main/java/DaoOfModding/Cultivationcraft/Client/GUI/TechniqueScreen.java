package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class TechniqueScreen extends Screen
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/technique.png");

    private DropdownList techniques;

    private final int xSize = 175;
    private final int ySize = 178;

    public static int selected = 0;

    private final int techniqueXPos = 25;
    private final int techniqueYPos = 50;

    public TechniqueScreen()
    {
        super(new TranslationTextComponent("cultivationcraft.gui.technique"));

        updateTechniqueList();
    }

    private void updateTechniqueList()
    {
        techniques = new DropdownList();

        techniques.addItem(new TranslationTextComponent("cultivationcraft.gui.notechnique").getString(), Technique.class);

        ICultivatorTechniques techList = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

        for (Class tech : TechniqueControl.getAvailableTechnics(Minecraft.getInstance().player))
        {
            try
            {
                Technique technique = (Technique) (tech.newInstance());

                // Add the technique if it is valid and either multiple copies are allowed or it isn't already set
                if (technique.isValid(Minecraft.getInstance().player) && (technique.allowMultiple() || !techList.techniqueExists(technique)))
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
    private void updateSelection()
    {
        Technique selectedTech = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player).getTechnique(selected);

        if (selectedTech != null)
            techniques.changeSelection(selectedTech.getName());
        else
            techniques.changeSelection(new TranslationTextComponent("cultivationcraft.gui.notechnique").getString());
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
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

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

        techs.setTechnique(selected, newTechnique);
        updateTechniqueList();

        ClientPacketHandler.sendCultivatorTechniquesToServer();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (ScreenTabControl.mouseClick((int)mouseX, (int)mouseY, edgeSpacingX, edgeSpacingY, buttonPressed))
            return true;

        Class changed = (Class)techniques.mouseClick((int)mouseX - (edgeSpacingX + techniqueXPos), (int)mouseY - (edgeSpacingY + techniqueYPos), buttonPressed);

        // If the technique has been changed send that change to the server
        if (changed != null)
        {
            changeTechnique(changed);
            return true;
        }

        if (buttonPressed == 0)
        {
            int techniqueSelected = TechniqueIcons.mouseOver(edgeSpacingX + 8, edgeSpacingY + 155, (int) mouseX, (int) mouseY, 18);

            if (techniqueSelected != -1)
                changeSelection(techniqueSelected);
        }

        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        return false;
    }

    public void changeSelection(int newSelection)
    {
        selected = newSelection;
        updateSelection();
    }

    protected void drawGuiForgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        // Draw icons in the selection box
        TechniqueIcons.renderIcons(matrixStack, edgeSpacingX + 8,edgeSpacingY + 155, this, 18);

        // Highlight the selected technique and the technique the mouse is hovering over
        TechniqueIcons.mouseOverHighlight(matrixStack, edgeSpacingX + 8,edgeSpacingY + 155, this, 18, mouseX, mouseY);
        TechniqueIcons.highlightIcon(matrixStack, edgeSpacingX + 8,edgeSpacingY + 155, this, 18, selected);

        // Render the techniques dropdown list
        techniques.render(matrixStack, edgeSpacingX + techniqueXPos, edgeSpacingY + techniqueYPos, mouseX, mouseY, this);
    }

    protected void drawGuiBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);

        ScreenTabControl.highlightTabs(matrixStack, 1, mouseX, mouseY, edgeSpacingX, edgeSpacingY, this);
    }
}
