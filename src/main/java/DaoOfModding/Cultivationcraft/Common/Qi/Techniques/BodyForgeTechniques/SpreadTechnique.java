package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;

import java.util.UUID;

public class SpreadTechnique extends Technique
{
    private boolean jumpPressed = false;
    private boolean groundCheck = false;

    private PlayerPose flapUp = new PlayerPose();
    private PlayerPose flapDown = new PlayerPose();

    public SpreadTechnique()
    {
        name = new TranslationTextComponent("cultivationcraft.technique.spread").getString();
        elementID = Elements.noElementID;

        type = useType.Toggle;
        multiple = false;

        cooldown = 20;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/wingspread.png");

        pose.addAngle(BodyPartModelNames.wingUpperArmModel, new Vector3d(Math.toRadians(45), 0, Math.toRadians(-25)), 1);
        pose.addAngle(BodyPartModelNames.wingLowerArmModel, new Vector3d(0, 0, Math.toRadians(0)), 1);

        pose.addAngle(BodyPartModelNames.wingStrand1Model, new Vector3d(0, 0, Math.toRadians(-70)), 1);
        pose.addAngle(BodyPartModelNames.wingStrand2Model, new Vector3d(0, 0, Math.toRadians(-20)), 1);
        pose.addAngle(BodyPartModelNames.wingStrand3Model, new Vector3d(0, 0, Math.toRadians(20)), 1);
        pose.addAngle(BodyPartModelNames.wingStrand4Model, new Vector3d(0, 0, Math.toRadians(60)), 1);


        flapUp.addAngle(BodyPartModelNames.wingUpperArmModel, new Vector3d(Math.toRadians(45), 0, Math.toRadians(-50)), 5, 3f, -1);
        flapUp.addAngle(BodyPartModelNames.wingLowerArmModel, new Vector3d(0, 0, Math.toRadians(15)), 5, 3f, -1);
        flapDown.addAngle(BodyPartModelNames.wingUpperArmModel, new Vector3d(Math.toRadians(45), 0, Math.toRadians(25)), 5);
        flapDown.addAngle(BodyPartModelNames.wingLowerArmModel, new Vector3d(0, 0, Math.toRadians(-15)), 5);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        // Technique is valid if the player is a body cultivator with Reverse Joint Legs
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.bodyPosition, BodyPartNames.backSubPosition, BodyPartNames.wingPart))
            return true;

        return false;
    }


    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);
        tickInactiveClient(event);

        // Reset flapping if player is on the ground
        if (event.player.isOnGround())
        {
            cooldownCount = 0;
            jumpPressed = false;
            groundCheck = false;
        }

        if (cooldownCount > cooldown * 0.5)
            PoseHandler.addPose(event.player.getUUID(), flapDown);
        else if (jumpPressed && groundCheck == false && cooldownCount == 0)
            PoseHandler.addPose(event.player.getUUID(), flapUp);

        glide(event.player);
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);
        tickInactiveServer(event);

        glide(event.player);
    }

    @Override
    public void onInput()
    {
        // Check if the jump key is depressed
        if (Minecraft.getInstance().options.keyJump.getKeyBinding().isDown())
        {
            if (Minecraft.getInstance().player.isOnGround())
                groundCheck = true;

            jumpPressed = true;
        }
        // If the jump key is released, reset the jump key checking boolean and set the cooldown
        else if (jumpPressed)
        {
            jumpPressed = false;

            // Don't flap if jumping off the ground
            if (groundCheck == true)
                groundCheck = false;
            // TODO: Update this on server
            else if (!Minecraft.getInstance().player.isOnGround() && cooldownCount == 0)
                cooldownCount = cooldown;
        }
    }

    private void glide(PlayerEntity player)
    {
        player.fallDistance = 0;

        Vector3d motion = player.getDeltaMovement();

        // TODO: Calculate this based on weight
        float maxFallSpeed = -0.4f;
        float FlapSpeed = 0.4f;

        // TODO: adjust fall speed based on X/Z movement speed
        // Ensure the player doesn't fall faster than set speed when gliding
        if (motion.y < maxFallSpeed)
            player.setDeltaMovement(motion.x, maxFallSpeed, motion.z);

        // If less than halfway through cooldown, then move upwards
        if (cooldownCount > cooldown / 2)
            if (motion.y > FlapSpeed)
                return;
            else if (motion.y > 0)
                player.setDeltaMovement(motion.x, FlapSpeed, motion.z);
            else
                player.setDeltaMovement(motion.x, motion.y + FlapSpeed, motion.z);
    }
}
