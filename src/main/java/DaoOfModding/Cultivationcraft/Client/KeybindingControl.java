package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Animations.CultivatorModelHandler;
import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Misc;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.MovementOverridePart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.MovementOverridePartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivatorControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.MovementOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeybindingControl
{
    public static KeyBinding[] keyBindings;

    private static boolean usePressed = false;

    public static double x = 0;
    public static double y = 0;
    public static double z = 0;


    private static boolean overwriteUp = false;
    private static boolean overwriteDown = false;
    private static boolean overwriteLeft = false;
    private static boolean overwriteRight = false;
    private static boolean overwriteJump = false;

    public static void init()
    {
        keyBindings = new KeyBinding[4];
        keyBindings[0] = new KeyBinding("Switch Hotbar", GLFW_KEY_GRAVE_ACCENT, "Cultivation");
        keyBindings[1] = new KeyBinding("Flying Sword Target", GLFW_KEY_R, "Cultivation");
        keyBindings[2] = new KeyBinding("Flying Sword Recall", GLFW_KEY_O, "Cultivation");
        keyBindings[3] = new KeyBinding("Flying Sword Screen Test", GLFW_KEY_L, "Cultivation");

        for (KeyBinding binding : keyBindings)
            ClientRegistry.registerKeyBinding(binding);
    }


    // Handle hotbar keys being pressed
    public static void handleHotbarKeybinds()
    {
        // Don't do anything if the Skill Hotbar isn't active
        if (!SkillHotbarOverlay.isActive())
            return;

        // Cancel the hotbar key press and change the skill hotbar selection to the pressed button
        for (int i = 0; i < 9; i++)
            if(Minecraft.getInstance().options.keyHotbarSlots[i].isDown())
            {
                SkillHotbarOverlay.setSelection(i);

                Minecraft.getInstance().options.keyHotbarSlots[i].setDown(false);
            }
    }

    public static void handleHotbarInteracts()
    {
        // If the skill hotbar isn't active do nothing
        if (!SkillHotbarOverlay.isActive())
        {
            usePressed = false;
            return;
        }

        // Is the use button is pressed, use currently selected skill and set usePressed to true
        // If the button isn't pressed but usePressed is true, tell the selected skill that the button has been released
        if (Minecraft.getInstance().options.keyUse.isDown())
        {
            usePressed = true;

            SkillHotbarOverlay.useSkill(true);
        }
        else if (usePressed && !Minecraft.getInstance().options.keyUse.isDown())
        {
            usePressed = false;

            SkillHotbarOverlay.useSkill(false);
        }
    }

    public static void handleSkillKeyPresses()
    {
        // Tell all active techniques that a button has been pressed

        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player);

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (techs.getTechnique(i) != null)
                if (techs.getTechnique(i).isActive())
                    techs.getTechnique(i).onInput();
    }

    public static boolean handleAttackOverrides()
    {
        // If something is held in the players hand do nothing
        if (!Minecraft.getInstance().player.getMainHandItem().isEmpty())
            return false;

        // Get all cultivator techniques and check if any of them are active attack overrides
        int slot = CultivatorControl.getAttackOverride(Minecraft.getInstance().player);

        // Do nothing if there is no active attack override
        if (slot == -1)
            return false;

        AttackOverrideTechnique attackTech = (AttackOverrideTechnique)CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player).getTechnique(slot);

        Minecraft.getInstance().options.keyAttack.getKeyBinding().setDown(false);

        // Attack with the attack override
        attackTech.attack(Minecraft.getInstance().player, slot);

        return true;
    }

    public static void handleMovementOverrides()
    {
        handleMovementTechOverrides();
        handleMovementPartOverrides();

        handleKeyOverrides();
    }

    // Set keyBindings to false if their overwrite has been set
    // Otherwise set them to the state of the key (to avoid issues with multiple key presses at the same time)
    private static void handleKeyOverrides()
    {
        if (overwriteUp)
            Minecraft.getInstance().options.keyUp.getKeyBinding().setDown(false);
        else
            Minecraft.getInstance().options.keyUp.getKeyBinding().setDown(isDown(Minecraft.getInstance().options.keyUp));

        if (overwriteDown)
            Minecraft.getInstance().options.keyDown.getKeyBinding().setDown(false);
        else
            Minecraft.getInstance().options.keyDown.getKeyBinding().setDown(isDown(Minecraft.getInstance().options.keyDown));

        if (overwriteLeft)
            Minecraft.getInstance().options.keyLeft.getKeyBinding().setDown(false);
        else
            Minecraft.getInstance().options.keyLeft.getKeyBinding().setDown(isDown(Minecraft.getInstance().options.keyLeft));

        if (overwriteRight)
            Minecraft.getInstance().options.keyRight.getKeyBinding().setDown(false);
        else
            Minecraft.getInstance().options.keyRight.getKeyBinding().setDown(isDown(Minecraft.getInstance().options.keyRight));

        if (overwriteJump)
            Minecraft.getInstance().options.keyJump.getKeyBinding().setDown(false);
        else
            Minecraft.getInstance().options.keyJump.getKeyBinding().setDown(isDown(Minecraft.getInstance().options.keyJump));

        overwriteUp = false;
        overwriteDown = false;
        overwriteLeft = false;
        overwriteRight = false;
        overwriteJump = false;
    }

    // Check whether the key is press via InputMappings rather than keybindings
    // Fixes issues caused by pressing multiple keys at the same time
    public static boolean isDown(KeyBinding key)
    {
        return InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getKeyBinding().getKey().getValue());
    }

    private static void handleMovementPartOverrides()
    {
        // Tick through all body modifications on the player
        IBodyModifications modifications = BodyModifications.getBodyModifications(Minecraft.getInstance().player);

        for (BodyPart part : modifications.getModifications().values())
            if (part instanceof MovementOverridePart)
                handlePartMovementOverride((MovementOverridePart) part);
            else if (part instanceof MovementOverridePartOption)
                handlePartMovementOverride((MovementOverridePartOption) part);
    }

    private static void handleMovementTechOverrides()
    {
        // Get all cultivator techniques and check if any of them are active movement overrides
        int slot = CultivatorControl.getMovementOverride(Minecraft.getInstance().player);

        // Do nothing if there is no active movement override
        if (slot == -1)
            return;

        MovementOverrideTechnique movementTech = (MovementOverrideTechnique)CultivatorTechniques.getCultivatorTechniques(Minecraft.getInstance().player).getTechnique(slot);
        handleTechMovementOverride(movementTech);
    }

    private static void handleTechMovementOverride(MovementOverrideTechnique movementTech)
    {
        if (isDown(Minecraft.getInstance().options.keyUp))
            if (movementTech.overwriteForward())
                overwriteUp = true;

        if (isDown(Minecraft.getInstance().options.keyDown))
            if (movementTech.overwriteBackward())
                overwriteDown = true;

        if (isDown(Minecraft.getInstance().options.keyLeft))
            if (movementTech.overwriteLeft())
                overwriteLeft = true;

        if (isDown(Minecraft.getInstance().options.keyRight))
            if (movementTech.overwriteRight())
                overwriteRight = true;

        if (isDown(Minecraft.getInstance().options.keyJump))
            if (movementTech.overwriteJump())
                overwriteJump = true;
    }

    private static void handlePartMovementOverride(MovementOverridePart movementPart)
    {
        if (isDown(Minecraft.getInstance().options.keyUp))
            if (movementPart.overwriteForward())
                overwriteUp = true;

        if (isDown(Minecraft.getInstance().options.keyDown))
            if (movementPart.overwriteBackward())
                overwriteDown = true;

        if (isDown(Minecraft.getInstance().options.keyLeft))
            if (movementPart.overwriteLeft())
                overwriteLeft = true;

        if (isDown(Minecraft.getInstance().options.keyRight))
            if (movementPart.overwriteRight())
                overwriteRight = true;

        if (isDown(Minecraft.getInstance().options.keyJump))
            if (movementPart.overwriteJump())
                overwriteJump = true;
    }

    private static void handlePartMovementOverride(MovementOverridePartOption movementPart)
    {
        if (isDown(Minecraft.getInstance().options.keyUp))
            if (movementPart.overwriteForward())
                overwriteUp = true;

        if (isDown(Minecraft.getInstance().options.keyDown))
            if (movementPart.overwriteBackward())
                overwriteDown = true;

        if (isDown(Minecraft.getInstance().options.keyLeft))
            if (movementPart.overwriteLeft())
                overwriteLeft = true;

        if (isDown(Minecraft.getInstance().options.keyRight))
            if (movementPart.overwriteRight())
                overwriteRight = true;

        if (isDown(Minecraft.getInstance().options.keyJump))
            if (movementPart.overwriteJump())
                overwriteJump = true;
    }


    @SubscribeEvent
    public static void mouseScroll(InputEvent.MouseScrollEvent event)
    {
        // If the skill hotbar is active
        if (SkillHotbarOverlay.isActive())
        {
            // Scroll the skill hotbar
            SkillHotbarOverlay.changeCurrentSelection(event.getScrollDelta());

            // Cancel this scroll action if the Skill Hotbar is active
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onInput(InputEvent.ClickInputEvent event)
    {
        if (event.getKeyBinding() == Minecraft.getInstance().options.keyAttack.getKeyBinding())
        {
            event.setCanceled(handleAttackOverrides());
        }
    }

    @SubscribeEvent
    public static void onInput(InputEvent event)
    {
        // Only perform if the world is loaded and player is alive
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().player.isAlive())
        {
            handleHotbarKeybinds();
            handleHotbarInteracts();
            //handleMovementOverrides();
            handleSkillKeyPresses();

            if (keyBindings[0].isDown())
            {
                SkillHotbarOverlay.switchActive();
                ClientPacketHandler.sendKeypressToServer(Register.keyPresses.SKILLHOTBARSWITCH);
            }

            if (keyBindings[1].isDown())
            {
                final RayTraceResult result = getMouseOver(100);

                RayTraceResult.Type type = result.getType();
                UUID targetID = null;

                Vector3d pos = result.getLocation();

                if (type == RayTraceResult.Type.ENTITY)
                    targetID = Misc.getEntityAtLocation(result.getLocation(), Minecraft.getInstance().level).getUUID();

                // If result is a block, move position vector inside the block
                if (type == RayTraceResult.Type.BLOCK)
                {
                    if (Misc.enableHarvest)
                    {
                        pos = pos.add(Minecraft.getInstance().player.getLookAngle().scale(0.1));
                        pos = new Vector3d(Math.floor(pos.x), Math.floor(pos.y), Math.floor(pos.z));
                    }
                    else
                        type = RayTraceResult.Type.MISS;
                }

                ClientPacketHandler.sendCultivatorTargetToServer(Minecraft.getInstance().player.getUUID(), type, pos, targetID);
            }

            if (keyBindings[2].isDown())
            {
                ClientPacketHandler.sendRecallFlyingToServer(true, Minecraft.getInstance().player.getUUID());
            }


            if (keyBindings[3].isDown())
            {
                ClientPacketHandler.sendKeypressToServer(Register.keyPresses.FLYINGSWORDSCREEN);
            }
        }
    }


    // Extended getMouseOver class, ripped almost word for word from minecraft's GameRenderer.pick class
    // double d0 = the distance the raytrace should cover
    public static RayTraceResult getMouseOver(double d0)
    {
        RayTraceResult result = null;

        Entity entity = Minecraft.getInstance().getCameraEntity();
        if (entity != null) {
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().getProfiler().push("pick");
                Minecraft.getInstance().crosshairPickEntity = null;
                result = entity.pick(d0, 1, false);
                Vector3d vector3d = entity.getEyePosition(1);

                int i = 3;
                double d1 = d0;

                d1 = d1 * d1;
                if (result != null) {
                    d1 = result.getLocation().distanceToSqr(vector3d);
                }

                Vector3d vector3d1 = entity.getViewVector(1.0F);
                Vector3d vector3d2 = vector3d.add(vector3d1.x * d0, vector3d1.y * d0, vector3d1.z * d0);
                float f = 1.0F;
                AxisAlignedBB axisalignedbb = entity.getBoundingBox().expandTowards(vector3d1.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                EntityRayTraceResult entityraytraceresult = ProjectileHelper.getEntityHitResult(entity, vector3d, vector3d2, axisalignedbb, (p_215312_0_) -> {
                    return !p_215312_0_.isSpectator() && p_215312_0_.isPickable();
                }, d1);
                if (entityraytraceresult != null)
                {
                    result = entityraytraceresult;
                }
            }
        }

        return result;
    }
}
