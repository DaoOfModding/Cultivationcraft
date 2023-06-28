package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class SpreadTechnique extends Technique
{
    protected boolean jumpPressed = false;
    protected boolean groundCheck = false;

    protected PlayerPose flapUp = new PlayerPose();
    protected PlayerPose flapDown = new PlayerPose();

    protected PlayerPose inAir = new PlayerPose();

    protected float staminaUse = 0.2f;

    public SpreadTechnique()
    {
        super();

        elytraDisables = true;

        langLocation = "cultivationcraft.technique.spread";
        Element = Elements.noElement;

        type = useType.Toggle;
        multiple = false;

        cooldown = 20;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/wingspread.png");

        pose.addAngle(BodyPartModelNames.rwingUpperArmModel, new Vec3(Math.toRadians(45), Math.toRadians(-35), Math.toRadians(-25)), 1);
        pose.addAngle(BodyPartModelNames.lwingUpperArmModel, new Vec3(Math.toRadians(45), Math.toRadians(35), Math.toRadians(25)), 1);

        pose.addAngle(BodyPartModelNames.rwingStrand1Model, new Vec3(0, 0, Math.toRadians(-70)), 1);
        pose.addAngle(BodyPartModelNames.rwingStrand2Model, new Vec3(0, 0, Math.toRadians(-20)), 1);
        pose.addAngle(BodyPartModelNames.rwingStrand3Model, new Vec3(0, 0, Math.toRadians(20)), 1);
        pose.addAngle(BodyPartModelNames.rwingStrand4Model, new Vec3(0, 0, Math.toRadians(60)), 1);

        pose.addAngle(BodyPartModelNames.lwingStrand1Model, new Vec3(0, 0, Math.toRadians(70)), 1);
        pose.addAngle(BodyPartModelNames.lwingStrand2Model, new Vec3(0, 0, Math.toRadians(20)), 1);
        pose.addAngle(BodyPartModelNames.lwingStrand3Model, new Vec3(0, 0, Math.toRadians(-20)), 1);
        pose.addAngle(BodyPartModelNames.lwingStrand4Model, new Vec3(0, 0, Math.toRadians(-60)), 1);


        flapUp.addAngle(BodyPartModelNames.rwingUpperArmModel, new Vec3(Math.toRadians(45), Math.toRadians(-75), Math.toRadians(-25)), 5, 3f, -1);
        flapUp.addAngle(BodyPartModelNames.lwingUpperArmModel, new Vec3(Math.toRadians(45), Math.toRadians(75), Math.toRadians(25)), 5, 3f, -1);
        flapDown.addAngle(BodyPartModelNames.rwingUpperArmModel, new Vec3(Math.toRadians(45), Math.toRadians(0), Math.toRadians(-15)), 5);
        flapDown.addAngle(BodyPartModelNames.lwingUpperArmModel, new Vec3(Math.toRadians(45), Math.toRadians(0), Math.toRadians(15)), 5);

        flapUp.addAngle(BodyPartModelNames.rwingStrand1Model, new Vec3(Math.toRadians(-45), 0, Math.toRadians(-70)), 5);
        flapUp.addAngle(BodyPartModelNames.rwingStrand2Model, new Vec3(Math.toRadians(-35), 0, Math.toRadians(-20)), 5);
        flapUp.addAngle(BodyPartModelNames.rwingStrand3Model, new Vec3(Math.toRadians(-25), 0, Math.toRadians(20)), 5);
        flapUp.addAngle(BodyPartModelNames.rwingStrand4Model, new Vec3(Math.toRadians(-15), 0, Math.toRadians(60)), 5);

        flapDown.addAngle(BodyPartModelNames.rwingStrand1Model, new Vec3(Math.toRadians(-20), 0, Math.toRadians(-70)), 5);
        flapDown.addAngle(BodyPartModelNames.rwingStrand2Model, new Vec3(Math.toRadians(-15), 0, Math.toRadians(-20)), 5);
        flapDown.addAngle(BodyPartModelNames.rwingStrand3Model, new Vec3(Math.toRadians(-10), 0, Math.toRadians(20)), 5);
        flapDown.addAngle(BodyPartModelNames.rwingStrand4Model, new Vec3(Math.toRadians(-5), 0, Math.toRadians(60)), 5);

        flapUp.addAngle(BodyPartModelNames.lwingStrand1Model, new Vec3(Math.toRadians(-45), 0, Math.toRadians(70)), 5);
        flapUp.addAngle(BodyPartModelNames.lwingStrand2Model, new Vec3(Math.toRadians(-35), 0, Math.toRadians(20)), 5);
        flapUp.addAngle(BodyPartModelNames.lwingStrand3Model, new Vec3(Math.toRadians(-25), 0, Math.toRadians(-20)), 5);
        flapUp.addAngle(BodyPartModelNames.lwingStrand4Model, new Vec3(Math.toRadians(-15), 0, Math.toRadians(-60)), 5);

        flapDown.addAngle(BodyPartModelNames.lwingStrand1Model, new Vec3(Math.toRadians(-20), 0, Math.toRadians(70)), 5);
        flapDown.addAngle(BodyPartModelNames.lwingStrand2Model, new Vec3(Math.toRadians(-15), 0, Math.toRadians(20)), 5);
        flapDown.addAngle(BodyPartModelNames.lwingStrand3Model, new Vec3(Math.toRadians(-10), 0, Math.toRadians(-20)), 5);
        flapDown.addAngle(BodyPartModelNames.lwingStrand4Model, new Vec3(Math.toRadians(-5), 0, Math.toRadians(-60)), 5);

        inAir.addAngle(GenericLimbNames.leftArm, new Vec3(Math.toRadians(0.0D), Math.toRadians(0.0D), Math.toRadians(-30.0D)), 11, 5.0F, -1);
        inAir.addAngle(GenericLimbNames.rightArm, new Vec3(Math.toRadians(0.0D), Math.toRadians(0.0D), Math.toRadians(30.0D)), 11, 5.0F, -1);
        inAir.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(30.0D), Math.toRadians(0.0D), Math.toRadians(0.0D)), 11, 5.0F, -1);

    }

    @Override
    public boolean isValid(Player player)
    {
        // Technique is valid if the player is a body cultivator with Wings
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

            return;
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
        Reflection.allowFlight((ServerPlayer) event.player);
        
        super.tickServer(event);
        tickInactiveServer(event);

        event.player.fallDistance = 0;
    }

    @Override
    public void onInput()
    {
        // Check if the jump key is depressed
        if (Minecraft.getInstance().options.keyJump.isDown())
        {
            if (jumpPressed == false)
                sendInfo(1);

            flapUp(genericClientFunctions.getPlayer());
        }
        // If the jump key is released, reset the jump key checking boolean and set the cooldown
        else if (jumpPressed)
        {
            sendInfo(2);
            jumpPressed = false;

            flapDown(genericClientFunctions.getPlayer());
        }
    }

    protected void flapUp(Player player)
    {
        if (player.isOnGround() || player.isInWater())
            groundCheck = true;

        jumpPressed = true;
    }

    protected void flapDown(Player player)
    {
        // Don't flap if jumping off the ground
        if (groundCheck == true)
            groundCheck = false;
        else if (!player.isOnGround() && cooldownCount == 0)
            if (StaminaHandler.consumeStamina(player, staminaUse))
                cooldownCount = cooldown;
    }

    @Override
    public void processInfo(Player player, int info)
    {
        if (info == 2)
            flapDown(player);
        else if (info == 1)
            flapUp(player);
    }

    protected void glide(Player player)
    {
        player.fallDistance = 0;

        Vec3 motion = Physics.getDelta(player);

        float weightModifier = BodyPartStatControl.getPlayerStatControl(player).getFlightWeightModifier();

        float maxFallSpeed = -0.4f * (1 / weightModifier);
        float FlapSpeed = 0.4f * weightModifier;

        // Ensure the player doesn't fall faster than set speed when gliding
        if (motion.y < maxFallSpeed)
            player.setDeltaMovement(motion.x, maxFallSpeed, motion.z);

        Vec3 dir = player.getLookAngle();
        dir.multiply(new Vec3(1, 0, 1));
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

        QuestHandler.progressQuest(player, Quest.FLIGHT, new Vec3(player.getDeltaMovement().x, 0, player.getDeltaMovement().z).length());
    }
}
