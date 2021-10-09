package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
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

    private PlayerPose inAir = new PlayerPose();

    public SpreadTechnique()
    {
        name = new TranslationTextComponent("cultivationcraft.technique.spread").getString();
        elementID = Elements.noElementID;

        type = useType.Toggle;
        multiple = false;

        cooldown = 20;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/wingspread.png");

        pose.addAngle(BodyPartModelNames.rwingUpperArmModel, new Vector3d(Math.toRadians(45), Math.toRadians(-35), Math.toRadians(-25)), 1);
        pose.addAngle(BodyPartModelNames.lwingUpperArmModel, new Vector3d(Math.toRadians(45), Math.toRadians(35), Math.toRadians(25)), 1);

        pose.addAngle(BodyPartModelNames.rwingStrand1Model, new Vector3d(0, 0, Math.toRadians(-70)), 1);
        pose.addAngle(BodyPartModelNames.rwingStrand2Model, new Vector3d(0, 0, Math.toRadians(-20)), 1);
        pose.addAngle(BodyPartModelNames.rwingStrand3Model, new Vector3d(0, 0, Math.toRadians(20)), 1);
        pose.addAngle(BodyPartModelNames.rwingStrand4Model, new Vector3d(0, 0, Math.toRadians(60)), 1);

        pose.addAngle(BodyPartModelNames.lwingStrand1Model, new Vector3d(0, 0, Math.toRadians(70)), 1);
        pose.addAngle(BodyPartModelNames.lwingStrand2Model, new Vector3d(0, 0, Math.toRadians(20)), 1);
        pose.addAngle(BodyPartModelNames.lwingStrand3Model, new Vector3d(0, 0, Math.toRadians(-20)), 1);
        pose.addAngle(BodyPartModelNames.lwingStrand4Model, new Vector3d(0, 0, Math.toRadians(-60)), 1);


        flapUp.addAngle(BodyPartModelNames.rwingUpperArmModel, new Vector3d(Math.toRadians(45), Math.toRadians(-75), Math.toRadians(-25)), 5, 3f, -1);
        flapUp.addAngle(BodyPartModelNames.lwingUpperArmModel, new Vector3d(Math.toRadians(45), Math.toRadians(75), Math.toRadians(25)), 5, 3f, -1);
        flapDown.addAngle(BodyPartModelNames.rwingUpperArmModel, new Vector3d(Math.toRadians(45), Math.toRadians(0), Math.toRadians(-15)), 5);
        flapDown.addAngle(BodyPartModelNames.lwingUpperArmModel, new Vector3d(Math.toRadians(45), Math.toRadians(0), Math.toRadians(15)), 5);

        flapUp.addAngle(BodyPartModelNames.rwingStrand1Model, new Vector3d(Math.toRadians(-45), 0, Math.toRadians(-70)), 5);
        flapUp.addAngle(BodyPartModelNames.rwingStrand2Model, new Vector3d(Math.toRadians(-35), 0, Math.toRadians(-20)), 5);
        flapUp.addAngle(BodyPartModelNames.rwingStrand3Model, new Vector3d(Math.toRadians(-25), 0, Math.toRadians(20)), 5);
        flapUp.addAngle(BodyPartModelNames.rwingStrand4Model, new Vector3d(Math.toRadians(-15), 0, Math.toRadians(60)), 5);

        flapDown.addAngle(BodyPartModelNames.rwingStrand1Model, new Vector3d(Math.toRadians(-20), 0, Math.toRadians(-70)), 5);
        flapDown.addAngle(BodyPartModelNames.rwingStrand2Model, new Vector3d(Math.toRadians(-15), 0, Math.toRadians(-20)), 5);
        flapDown.addAngle(BodyPartModelNames.rwingStrand3Model, new Vector3d(Math.toRadians(-10), 0, Math.toRadians(20)), 5);
        flapDown.addAngle(BodyPartModelNames.rwingStrand4Model, new Vector3d(Math.toRadians(-5), 0, Math.toRadians(60)), 5);

        flapUp.addAngle(BodyPartModelNames.lwingStrand1Model, new Vector3d(Math.toRadians(-45), 0, Math.toRadians(70)), 5);
        flapUp.addAngle(BodyPartModelNames.lwingStrand2Model, new Vector3d(Math.toRadians(-35), 0, Math.toRadians(20)), 5);
        flapUp.addAngle(BodyPartModelNames.lwingStrand3Model, new Vector3d(Math.toRadians(-25), 0, Math.toRadians(-20)), 5);
        flapUp.addAngle(BodyPartModelNames.lwingStrand4Model, new Vector3d(Math.toRadians(-15), 0, Math.toRadians(-60)), 5);

        flapDown.addAngle(BodyPartModelNames.lwingStrand1Model, new Vector3d(Math.toRadians(-20), 0, Math.toRadians(70)), 5);
        flapDown.addAngle(BodyPartModelNames.lwingStrand2Model, new Vector3d(Math.toRadians(-15), 0, Math.toRadians(20)), 5);
        flapDown.addAngle(BodyPartModelNames.lwingStrand3Model, new Vector3d(Math.toRadians(-10), 0, Math.toRadians(-20)), 5);
        flapDown.addAngle(BodyPartModelNames.lwingStrand4Model, new Vector3d(Math.toRadians(-5), 0, Math.toRadians(-60)), 5);

        inAir.addAngle(GenericLimbNames.leftArm, new Vector3d(Math.toRadians(0.0D), Math.toRadians(0.0D), Math.toRadians(-30.0D)), 11, 5.0F, -1);
        inAir.addAngle(GenericLimbNames.rightArm, new Vector3d(Math.toRadians(0.0D), Math.toRadians(0.0D), Math.toRadians(30.0D)), 11, 5.0F, -1);
        inAir.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(30.0D), Math.toRadians(0.0D), Math.toRadians(0.0D)), 11, 5.0F, -1);

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
        if (event.player.isOnGround() || event.player.isInWater())
        {
            cooldownCount = 0;
            jumpPressed = false;
            groundCheck = false;
        }
        else
            PoseHandler.addPose(event.player.getUUID(), inAir);

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
            if (Minecraft.getInstance().player.isOnGround() || Minecraft.getInstance().player.isInWater())
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

        // Ensure the player doesn't fall faster than set speed when gliding
        if (motion.y < maxFallSpeed)
            player.setDeltaMovement(motion.x, maxFallSpeed, motion.z);

        Vector3d dir = player.getLookAngle();
        dir.multiply(new Vector3d(1, 0, 1));
        dir.normalize();

        // If less than halfway through cooldown, then move upwards
        if (cooldownCount > cooldown / 2)
        {
            // TODO: Calculated flight movement speed
            double xMotion = motion.x + FlapSpeed / 4 * dir.x;
            double zMotion = motion.z + FlapSpeed / 4 * dir.z;
            double upMotion;

            if (motion.y > FlapSpeed)
                upMotion = motion.y;
            else if (motion.y > 0)
                upMotion = FlapSpeed;
            else
                upMotion = motion.y + FlapSpeed;

            player.setDeltaMovement(xMotion, upMotion, zMotion);
        }
    }
}
