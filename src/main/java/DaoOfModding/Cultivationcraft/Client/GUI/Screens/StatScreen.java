package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.BetterFontRenderer;
import DaoOfModding.Cultivationcraft.Client.GUI.TextField;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUI;
import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUIs;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class StatScreen extends GenericTabScreen
{
    protected String statString;

    protected int statTextX = 70;
    protected int statTextY = 35;
    protected int statTextWidth = 170;
    protected int statTextHeight = 121;

    protected TextField stats = new TextField();

    public StatScreen()
    {
        super(0, Component.translatable("cultivationcraft.gui.stats"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/stats.png"));

        statString = BodyPartStatControl.getStats(genericClientFunctions.getPlayer()).toString();
        stats.resetScroll();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (mouseX > statTextX +edgeSpacingX && mouseX < statTextX + edgeSpacingX + statTextWidth)
            if (mouseY > statTextY +edgeSpacingY && mouseY < statTextY + edgeSpacingY + statTextHeight)
                return stats.mouseClicked(mouseX - (statTextX +edgeSpacingX), mouseY - (statTextY +edgeSpacingY), buttonPressed);

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double direction)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (mouseX > statTextX +edgeSpacingX && mouseX < statTextX + edgeSpacingX + statTextWidth)
            if (mouseY > statTextY +edgeSpacingY && mouseY < statTextY + edgeSpacingY + statTextHeight)
                return stats.mouseScrolled(direction);

        return false;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        drawBody(PoseStack);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        stats.setPos(edgeSpacingX + statTextX, edgeSpacingY + statTextY);
        stats.setSize(statTextWidth, statTextHeight);
        stats.setText(statString);
        stats.render(this, font, PoseStack, mouseX, mouseY);
    }

    protected void drawBody(PoseStack PoseStack)
    {
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


        base.render(PoseStack, bodyPosX, bodyPosY, false, this);


        // Draw generic body parts if alterations don't exist
        if (!modifications.hasModification(BodyPartNames.headPosition))
        {
            BodyPartGUIs.getGUI(BodyPartNames.DefaultHead).get(0).render(PoseStack, bodyPosX, bodyPosY, false, this, base);
        }


        if (!modifications.hasModification(BodyPartNames.armPosition))
        {
            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightArm).get(0).render(PoseStack, bodyPosX, bodyPosY, false, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftArm).get(0).render(PoseStack, bodyPosX, bodyPosY, false,this, base);
        }

        if (!modifications.hasModification(BodyPartNames.legPosition))
        {
            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightLeg).get(0).render(PoseStack, bodyPosX, bodyPosY, false, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftLeg).get(0).render(PoseStack, bodyPosX, bodyPosY, false, this, base);
        }

        // Loop through and draw all other body part GUIs if they exist
        for (Map.Entry<String, BodyPart> entry : modifications.getModifications().entrySet())
        {
            // Don't redraw the body
            if (entry.getKey() != BodyPartNames.bodyPosition)
            {
                ArrayList<BodyPartGUI> guis = BodyPartGUIs.getGUI(entry.getValue().getID());

                for (BodyPartGUI gui : guis)
                    if (gui != null)
                        gui.render(PoseStack, bodyPosX, bodyPosY, false, this, base);
            }
        }
    }
}
