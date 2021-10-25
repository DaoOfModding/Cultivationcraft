package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.BetterFontRenderer;
import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUI;
import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUIs;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class StatScreen extends GenericTabScreen
{
    private String statString;

    protected int statTextX = 100;
    protected int statTextY = 45;
    protected int statTextWidth = 138;

    public StatScreen()
    {
        super(0, new TranslationTextComponent("cultivationcraft.gui.stats"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/stats.png"));

        statString = BodyPartStatControl.getStats(Minecraft.getInstance().player.getUUID()).toString();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        drawBody(matrixStack);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        BetterFontRenderer.wordwrap(font, matrixStack, statString, edgeSpacingX + statTextX, edgeSpacingY + statTextY, Color.WHITE.getRGB(), statTextWidth);
    }

    protected void drawBody(MatrixStack matrixStack)
    {
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


        base.render(matrixStack, bodyPosX, bodyPosY, false, this);


        // Draw generic body parts if alterations don't exist
        if (!modifications.hasModification(BodyPartNames.headPosition))
        {
            BodyPartGUIs.getGUI(BodyPartNames.DefaultHead).get(0).render(matrixStack, bodyPosX, bodyPosY, false, this, base);
        }


        if (!modifications.hasModification(BodyPartNames.armPosition))
        {
            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightArm).get(0).render(matrixStack, bodyPosX, bodyPosY, false, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftArm).get(0).render(matrixStack, bodyPosX, bodyPosY, false,this, base);
        }

        if (!modifications.hasModification(BodyPartNames.legPosition))
        {
            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightLeg).get(0).render(matrixStack, bodyPosX, bodyPosY, false, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftLeg).get(0).render(matrixStack, bodyPosX, bodyPosY, false, this, base);
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
                        gui.render(matrixStack, bodyPosX, bodyPosY, false, this, base);
            }
        }
    }
}
