package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class SkillHotbarOverlay
{
    protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");

    // Whether the hotbar is active or not
    protected static boolean isActive = false;

    // The hotbar slot currently selected
    protected static int skillSelected = 0;

    // Render in the post-render phase if the hotbar is active
    // In the pre-render phase if it isn't
    // This is necessary so that it renders in the right order for transparency to work properly
    public static void PreRenderSkillHotbar(PoseStack stack)
    {
        if (!isActive)
            RenderSkillHotbar(stack);
    }

    public static void PostRenderSkillHotbar(PoseStack stack)
    {
        if (isActive)
            RenderSkillHotbar(stack);
    }

    protected static void RenderSkillHotbar(PoseStack stack)
    {
        // Enable transparency
        RenderSystem.enableBlend();

        GuiComponent gui = Minecraft.getInstance().gui;

        RenderSystem.setShaderTexture(0, WIDGETS_TEX_PATH);

        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        // Store the current Z layer to reset back to later
        int blitOffset = gui.getBlitOffset();

        // Set the Z-layer to either in front of or behind the item hotbar based on whether this hotbar is active or not
        if (isActive)
            gui.setBlitOffset(200);
        else
            gui.setBlitOffset(-91);

        // Draw the hotbar
        gui.blit(stack, scaledWidth - 83, scaledHeight - 30, 0, 0, 182, 22);

        // Draw the selection box
        gui.blit(stack, scaledWidth - 83 - 1 + skillSelected * 20, scaledHeight - 30 - 1, 0, 22, 24, 22);

        // Draw icons in the selection box
        TechniqueIcons.renderIcons(stack, scaledWidth + 3 - 83,scaledHeight + 3 - 30, gui, 20);
        TechniqueIcons.highlightActive(stack, scaledWidth + 3 - 83,scaledHeight + 3 - 30, gui, 20);
        TechniqueIcons.showCooldowns(stack, scaledWidth + 3 - 83,scaledHeight + 3 - 30, gui, 20);

        // Reset the Z layer to its original value
        gui.setBlitOffset(blitOffset);
    }

    // Called when the use item button is pressed and the hotbar is active
    public static void useSkill(boolean keyDown)
    {
        if (!isActive())
            return;

        Technique tech = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player).getTechnique(skillSelected);

        if (tech != null && (tech.getType() != Technique.useType.Channel || !(tech.isActive() && keyDown)))
        // Send message to server to use this skill
            ClientPacketHandler.sendTechniqueUseToServer(skillSelected, keyDown);
    }

    public static void setSelection(int selection)
    {
        skillSelected = selection;
    }

    public static void changeCurrentSelection(double direction)
    {
        if (direction > 0.0D) {
            direction = 1.0D;
        }

        if (direction < 0.0D) {
            direction = -1.0D;
        }

        skillSelected -= direction;

        if (skillSelected > 8)
            skillSelected = 0;
        else if (skillSelected < 0)
            skillSelected = 8;
    }

    public static void switchActive()
    {
        isActive = !isActive;
    }

    public static boolean isActive()
    {
        return isActive;
    }

    public static void reset()
    {
        isActive = false;
        skillSelected = 0;
    }
}
