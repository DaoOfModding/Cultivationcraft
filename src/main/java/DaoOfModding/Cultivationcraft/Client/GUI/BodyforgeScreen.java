package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartGUIs;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartList;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;
import java.util.Map;

public class BodyforgeScreen extends Screen
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyforge.png");

    private final int xSize = 175;
    private final int ySize = 178;

    public BodyforgeScreen()
    {
        super(new TranslationTextComponent("cultivationcraft.gui.bodyforge"));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        if (ScreenTabControl.mouseClick((int)mouseX, (int)mouseY, edgeSpacingX, edgeSpacingY, buttonPressed))
            return true;

        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
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
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

    }

    protected void drawGuiBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);

        ScreenTabControl.highlightTabs(matrixStack, 2, mouseX, mouseY, edgeSpacingX, edgeSpacingY, this);


        // Draw the players body parts
        int bodyPosX = edgeSpacingX + 40;
        int bodyPosY = edgeSpacingY + 60;

        IBodyModifications modifications = BodyModifications.getBodyModifications(Minecraft.getInstance().player);

        // Get the body GUI, this is special as all other GUIs positions are based off this
        BodyPartGUI base;

        if (modifications.hasModification(BodyPartNames.bodyPosition))
            base = BodyPartGUIs.getGUI(modifications.getModification(BodyPartNames.bodyPosition).getModelIDs().get(0));
        else
            base = BodyPartGUIs.getGUI(BodyPartNames.DefaultBody);

        base.render(matrixStack, bodyPosX, bodyPosY, this);


        // Draw generic body parts if alterations don't exist
        if (!modifications.hasModification(BodyPartNames.headPosition))
            BodyPartGUIs.getGUI(BodyPartNames.DefaultHead).render(matrixStack, bodyPosX, bodyPosY, this, base);

        if (!modifications.hasModification(BodyPartNames.armPosition))
        {
            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightArm).render(matrixStack, bodyPosX, bodyPosY, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftArm).render(matrixStack, bodyPosX, bodyPosY, this, base);
        }

        if (!modifications.hasModification(BodyPartNames.legPosition))
        {
            BodyPartGUIs.getGUI(BodyPartNames.DefaultRightLeg).render(matrixStack, bodyPosX, bodyPosY, this, base);
            BodyPartGUIs.getGUI(BodyPartNames.DefaultLeftLeg).render(matrixStack, bodyPosX, bodyPosY, this, base);
        }

        // Loop through and draw all other body part GUIs if they exist
        for (Map.Entry<String, BodyPart> entry : modifications.getModifications().entrySet())
        {
            // Don't redraw the body
            if (entry.getKey() != BodyPartNames.bodyPosition)
                for (String ID : entry.getValue().getModelIDs())
                {
                    BodyPartGUI gui = BodyPartGUIs.getGUI(ID);

                    if (gui != null)
                        gui.render(matrixStack, bodyPosX, bodyPosY, this, base);
                }
        }
    }
}
