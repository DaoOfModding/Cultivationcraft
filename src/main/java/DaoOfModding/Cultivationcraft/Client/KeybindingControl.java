package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.StatScreen;
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
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeybindingControl
{
    public static KeyMapping[] keyBindings;

    protected static boolean usePressed = false;

    public static double x = 0;
    public static double y = 0;
    public static double z = 0;


    protected static boolean overwriteUp = false;
    protected static boolean overwriteDown = false;
    protected static boolean overwriteLeft = false;
    protected static boolean overwriteRight = false;
    protected static boolean overwriteJump = false;

    public static void init(RegisterKeyMappingsEvent event)
    {
        keyBindings = new KeyMapping[4];
        keyBindings[0] = new KeyMapping("Switch Hotbar", GLFW_KEY_GRAVE_ACCENT, "Cultivation");
        keyBindings[1] = new KeyMapping("Flying Sword Target", GLFW_KEY_R, "Cultivation");
        keyBindings[2] = new KeyMapping("Flying Sword Recall", GLFW_KEY_O, "Cultivation");
        keyBindings[3] = new KeyMapping("Flying Sword Screen Test", GLFW_KEY_L, "Cultivation");

        event.register(keyBindings[0]);
        event.register(keyBindings[1]);
        event.register(keyBindings[2]);
        event.register(keyBindings[3]);
    }

    public static boolean chatOpen()
    {
        if (Minecraft.getInstance().gui.getChat().getFocusedChat() != null)
            return true;

        return false;
    }

    // Handle hotbar keys being pressed
    public static void handleHotbarKeybinds()
    {
        // Don't do anything if the Skill Hotbar isn't active
        if (!SkillHotbarOverlay.isActive() || chatOpen())
            return;

        // Cancel the hotbar key press and change the skill hotbar selection to the pressed button
        for (int i = 0; i < 9; i++)
            if(Minecraft.getInstance().options.keyHotbarSlots[i].consumeClick())
                SkillHotbarOverlay.setSelection(i);
    }

    public static void handleHotbarInteracts()
    {
        // If the skill hotbar isn't active do nothing
        if (!SkillHotbarOverlay.isActive() || chatOpen())
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

        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer());

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (techs.getTechnique(i) != null)
                if (techs.getTechnique(i).isActive())
                    techs.getTechnique(i).onInput();
    }

    public static boolean handleAttackOverrides()
    {
        // If something is held in the players hand do nothing
        if (!genericClientFunctions.getPlayer().getMainHandItem().isEmpty())
            return false;

        // Get all cultivator techniques and check if any of them are active attack overrides
        int slot = CultivatorControl.getAttackOverride(genericClientFunctions.getPlayer());

        // Do nothing if there is no active attack override
        if (slot == -1)
            return false;

        AttackOverrideTechnique attackTech = (AttackOverrideTechnique)CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer()).getTechnique(slot);

        Minecraft.getInstance().options.keyAttack.setDown(false);
        // Attack with the attack override
        attackTech.attack(genericClientFunctions.getPlayer(), slot);

        return true;
    }

    public static void handleMovementOverrides()
    {
        // Don't override anything if the chat it open
        if (chatOpen())
            return;

        handleMovementTechOverrides();
        handleMovementPartOverrides();

        handleKeyOverrides();
    }

    // Set keyBindings to false if their overwrite has been set
    // Otherwise set them to the state of the key (to avoid issues with multiple key presses at the same time)
    protected static void handleKeyOverrides()
    {
        if (overwriteUp)
            Minecraft.getInstance().options.keyUp.setDown(false);
        else
            Minecraft.getInstance().options.keyUp.setDown(isDown(Minecraft.getInstance().options.keyUp));

        if (overwriteDown)
            Minecraft.getInstance().options.keyDown.setDown(false);
        else
            Minecraft.getInstance().options.keyDown.setDown(isDown(Minecraft.getInstance().options.keyDown));

        if (overwriteLeft)
            Minecraft.getInstance().options.keyLeft.setDown(false);
        else
            Minecraft.getInstance().options.keyLeft.setDown(isDown(Minecraft.getInstance().options.keyLeft));

        if (overwriteRight)
            Minecraft.getInstance().options.keyRight.setDown(false);
        else
            Minecraft.getInstance().options.keyRight.setDown(isDown(Minecraft.getInstance().options.keyRight));

        if (overwriteJump)
            Minecraft.getInstance().options.keyJump.setDown(false);
        else
            Minecraft.getInstance().options.keyJump.setDown(isDown(Minecraft.getInstance().options.keyJump));

        overwriteUp = false;
        overwriteDown = false;
        overwriteLeft = false;
        overwriteRight = false;
        overwriteJump = false;
    }

    // Check whether the key is press via InputMappings rather than keybindings
    // Fixes issues caused by pressing multiple keys at the same time
    public static boolean isDown(KeyMapping key)
    {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getKey().getValue());
    }

    protected static void handleMovementPartOverrides()
    {
        // Tick through all body modifications on the player
        IBodyModifications modifications = BodyModifications.getBodyModifications(genericClientFunctions.getPlayer());

        for (BodyPart part : modifications.getModifications().values())
            if (part instanceof MovementOverridePart)
                handlePartMovementOverride((MovementOverridePart) part);
            else if (part instanceof MovementOverridePartOption)
                handlePartMovementOverride((MovementOverridePartOption) part);
    }

    protected static void handleMovementTechOverrides()
    {
        // Get all cultivator techniques and check if any of them are active movement overrides
        int slot = CultivatorControl.getMovementOverride(genericClientFunctions.getPlayer());

        // Do nothing if there is no active movement override
        if (slot == -1)
            return;

        MovementOverrideTechnique movementTech = (MovementOverrideTechnique)CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer()).getTechnique(slot);
        handleTechMovementOverride(movementTech);
    }

    protected static void handleTechMovementOverride(MovementOverrideTechnique movementTech)
    {
        if (isDown(Minecraft.getInstance().options.keyUp) && !overwriteUp)
            if (movementTech.overwriteForward())
                overwriteUp = true;

        if (isDown(Minecraft.getInstance().options.keyDown) && !overwriteDown)
            if (movementTech.overwriteBackward())
                overwriteDown = true;

        if (isDown(Minecraft.getInstance().options.keyLeft) && !overwriteLeft)
            if (movementTech.overwriteLeft())
                overwriteLeft = true;

        if (isDown(Minecraft.getInstance().options.keyRight) && !overwriteRight)
            if (movementTech.overwriteRight())
                overwriteRight = true;

        if (isDown(Minecraft.getInstance().options.keyJump) && !overwriteJump)
            if (movementTech.overwriteJump())
                overwriteJump = true;
    }

    protected static void handlePartMovementOverride(MovementOverridePart movementPart)
    {
        if (isDown(Minecraft.getInstance().options.keyUp) && !overwriteUp)
            if (movementPart.overwriteForward())
                overwriteUp = true;

        if (isDown(Minecraft.getInstance().options.keyDown) && !overwriteDown)
            if (movementPart.overwriteBackward())
                overwriteDown = true;

        if (isDown(Minecraft.getInstance().options.keyLeft) && !overwriteLeft)
            if (movementPart.overwriteLeft())
                overwriteLeft = true;

        if (isDown(Minecraft.getInstance().options.keyRight) && !overwriteRight)
            if (movementPart.overwriteRight())
                overwriteRight = true;

        if (isDown(Minecraft.getInstance().options.keyJump) && !overwriteJump)
            if (movementPart.overwriteJump())
                overwriteJump = true;
    }

    protected static void handlePartMovementOverride(MovementOverridePartOption movementPart)
    {
        if (isDown(Minecraft.getInstance().options.keyUp) && !overwriteUp)
            if (movementPart.overwriteForward())
                overwriteUp = true;

        if (isDown(Minecraft.getInstance().options.keyDown) && !overwriteDown)
            if (movementPart.overwriteBackward())
                overwriteDown = true;

        if (isDown(Minecraft.getInstance().options.keyLeft) && !overwriteLeft)
            if (movementPart.overwriteLeft())
                overwriteLeft = true;

        if (isDown(Minecraft.getInstance().options.keyRight) && !overwriteRight)
            if (movementPart.overwriteRight())
                overwriteRight = true;

        if (isDown(Minecraft.getInstance().options.keyJump) && !overwriteJump)
            if (movementPart.overwriteJump())
                overwriteJump = true;
    }


    @SubscribeEvent
    public static void mouseScroll(InputEvent.MouseScrollingEvent event)
    {
        // Do nothing if the chat is open
        if (chatOpen())
            return;

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
    public static void onInput(InputEvent.MouseButton event)
    {
        // Do nothing if the chat is open
        if (chatOpen())
            return;

        // TODO: Make this not shit -.-

        if (Minecraft.getInstance().options.keyAttack.isDown())
        {
            event.setCanceled(handleAttackOverrides());
        }
    }

    @SubscribeEvent
    public static void onInput(InputEvent event)
    {
        // Only perform if the world is loaded and player is alive
        if (Minecraft.getInstance().level != null && genericClientFunctions.getPlayer().isAlive())
        {
            // Do nothing if the chat is open
            if (chatOpen())
                return;

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
                final HitResult result = getMouseOver(100);

                HitResult.Type type = result.getType();
                UUID targetID = null;

                Vec3 pos = result.getLocation();

                if (type == HitResult.Type.ENTITY)
                    targetID = Misc.getEntityAtLocation(result.getLocation(), Minecraft.getInstance().level).getUUID();

                // If result is a block, move position vector inside the block
                if (type == HitResult.Type.BLOCK)
                {
                    if (Misc.enableHarvest)
                    {
                        pos = pos.add(genericClientFunctions.getPlayer().getLookAngle().scale(0.1));
                        pos = new Vec3(Math.floor(pos.x), Math.floor(pos.y), Math.floor(pos.z));
                    }
                    else
                        type = HitResult.Type.MISS;
                }

                ClientPacketHandler.sendCultivatorTargetToServer(genericClientFunctions.getPlayer().getUUID(), type, pos, targetID);
            }

            if (keyBindings[2].isDown())
            {
                ClientPacketHandler.sendRecallFlyingToServer(true, genericClientFunctions.getPlayer().getUUID());
            }


            if (keyBindings[3].isDown())
            {
                Minecraft.getInstance().forceSetScreen(new StatScreen());
                //ClientPacketHandler.sendKeypressToServer(Register.keyPresses.FLYINGSWORDSCREEN);
            }
        }
    }


    // Extended getMouseOver class, ripped almost word for word from minecraft's GameRenderer.pick class
    // double d0 = the distance the raytrace should cover
    public static HitResult getMouseOver(double d0)
    {
        HitResult result = null;

        Entity entity = Minecraft.getInstance().getCameraEntity();
        if (entity != null) {
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().getProfiler().push("pick");
                Minecraft.getInstance().crosshairPickEntity = null;
                result = entity.pick(d0, 1, false);
                Vec3 Vec3 = entity.getEyePosition(1);

                int i = 3;
                double d1 = d0;

                d1 = d1 * d1;
                if (result != null) {
                    d1 = result.getLocation().distanceToSqr(Vec3);
                }

                Vec3 Vec31 = entity.getViewVector(1.0F);
                Vec3 Vec32 = Vec3.add(Vec31.x * d0, Vec31.y * d0, Vec31.z * d0);
                float f = 1.0F;
                AABB AABB = entity.getBoundingBox().expandTowards(Vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(entity, Vec3, Vec32, AABB, (p_215312_0_) -> {
                    return !p_215312_0_.isSpectator() && p_215312_0_.isPickable();
                }, d1);
                if (entityHitResult != null)
                {
                    result = entityHitResult;
                }
            }
        }

        return result;
    }
}
