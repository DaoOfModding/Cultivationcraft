package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniquesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class SkillHotbarOverlay
{
    protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");

    // Whether the hotbar is active or not
    private static boolean isActive = false;

    // The hotbar slot currently selected
    private static int skillSelected = 0;

    // Render in the post-render phase if the hotbar is active
    // In the pre-render phase if it isn't
    // This is necessary so that it renders in the right order for transparency to work properly
    public static void PreRenderSkillHotbar(MatrixStack stack)
    {
        if (!isActive)
            RenderSkillHotbar(stack);
    }

    public static void PostRenderSkillHotbar(MatrixStack stack)
    {
        if (isActive)
            RenderSkillHotbar(stack);
    }

    private static void RenderSkillHotbar(MatrixStack stack)
    {
        // Enable transparency
        RenderSystem.enableBlend();

        IngameGui gui = Minecraft.getInstance().ingameGUI;
        PlayerEntity playerentity = Minecraft.getInstance().player;

        Minecraft.getInstance().getTextureManager().bindTexture(WIDGETS_TEX_PATH);

        int scaledWidth = Minecraft.getInstance().getMainWindow().getScaledWidth() / 2;
        int scaledHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();

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

        // Reset the Z layer to its original value
        gui.setBlitOffset(blitOffset);
    }

    // Called when the use item button is pressed and the hotbar is active
    public static void useSkill(boolean keyDown)
    {
        // Send message to server to use this skill
        if (isActive())
            PacketHandler.sendTechniqueUseToServer(skillSelected, keyDown);
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
}
