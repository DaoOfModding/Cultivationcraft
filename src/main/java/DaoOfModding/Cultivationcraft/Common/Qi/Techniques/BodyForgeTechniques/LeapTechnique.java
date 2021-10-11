package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.mlmanimator.Client.Poses.*;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;

public class LeapTechnique extends Technique
{
    protected PlayerPose defaultLeapLegs = new PlayerPose();

    private boolean leaping = false;

    public LeapTechnique()
    {
        super();

        name = new TranslationTextComponent("cultivationcraft.technique.leap").getString();
        elementID = Elements.noElementID;

        multiple = true;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/leap.png");

        pose.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(90), 0, 0), 10);
        defaultLeapLegs.addAngle(BodyPartModelNames.reverseJointLeftLegModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority+3, 1f, -1);
        defaultLeapLegs.addAngle(BodyPartModelNames.reverseJointRightLegModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority+3, 1f, -1);
        defaultLeapLegs.addAngle(BodyPartModelNames.reverseJointLeftLegLowerModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority+3, 1f, -1);
        defaultLeapLegs.addAngle(BodyPartModelNames.reverseJointRightLegLowerModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority+3, 1f, -1);
        defaultLeapLegs.addAngle(BodyPartModelNames.reverseJointLeftFootModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority+3, 1f, -1);
        defaultLeapLegs.addAngle(BodyPartModelNames.reverseJointRightFootModel, new Vector3d(Math.toRadians(0), Math.toRadians(0), 0), GenericPoses.jumpLegPriority+3, 1f, -1);

        defaultLeapLegs.disableHeadLook(false, 8);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        // Technique is valid if the player is a body cultivator with Reverse Joint Legs
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                BodyModifications.getBodyModifications(player).hasModification(BodyPartNames.legPosition, BodyPartNames.reverseJointLegPart))
            return true;

        return false;
    }

    @Override
    // What to do when the use key for this technique is pressed
    // keyDown = true when the key is pressed down, false when the key is released
    public void useKeyPressed(boolean keyDown, PlayerEntity player)
    {
        // Do nothing if the technique is already active, the key has been released or the player is not on the ground
        if (active || leaping || !keyDown || !player.isOnGround() || player.isInWater())
            return;

        active = true;
    }

    @Override
    // Ticks on server side, only called if Technique is active and owned by the player
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);

        continueTech(event);
    }

    @Override
    // Ticks on client side, only called if Technique is active
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        continueTechClient(event);
    }

    // Start the leap if not already started, otherwise check if the technique has ended
    private void continueTech(TickEvent.PlayerTickEvent event)
    {
        if (!leaping)
            doLeap(event.player);
        else
            continueLeap(event.player);

    }

    // Start the leap if not already started, otherwise check if the technique has ended
    private void continueTechClient(TickEvent.PlayerTickEvent event)
    {
        if (!leaping)
            doLeapClient(event.player);
        else
            continueLeapClient(event.player);

    }

    // Check if the leap has ended
    private void continueLeap(PlayerEntity player)
    {
        // If the player is no longer jumping turn the technique off
        if (player.isOnGround() || player.isInWater())
        {
            active = false;
            leaping = false;
        }
    }

    // Check if the leap has ended
    private void continueLeapClient(PlayerEntity player)
    {
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(player.getUUID());

        // If the player is no longer jumping or the handler can't be loaded turn the technique off
        if (player.isInWater() || handler == null || !handler.isJumping())
        {
            active = false;
            leaping = false;
        }
        else
        {
            Vector3d currentMotion = player.getDeltaMovement().normalize();

            PlayerPose newPose = defaultLeapLegs.clone();
            newPose.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians((currentMotion.y - 1) * -90), 0, 0), 10);

            pose = newPose;
        }
    }

    // Do the leap
    private void doLeapClient(PlayerEntity player)
    {
        leaping = true;
        player.setOnGround(false);

        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(player.getUUID());

        // If the player is already jumping or the handler is null do nothing
        if (handler == null || handler.isJumping())
            return;

        // Tell the handler that the player is jumping
        handler.setJumping(true);

        // Get the current motion and forward vector of the player
        Vector3d currentMotion = player.getDeltaMovement();
        Vector3d forward = player.getForward().normalize();

        // Get the modified jump height of the player
        int jumpPower = BodyPartStatControl.getStats(player.getUUID()).getJumpHeight();

        // Move the player forward based on the jump power, as well as applying a height jump of 1 block
        player.setDeltaMovement(currentMotion.add(forward.x * jumpPower * 0.4f, 0.52f, forward.z * jumpPower * 0.4f));
    }

    // Do the leap
    private void doLeap(PlayerEntity player)
    {
        leaping = true;
        player.setOnGround(false);
    }

    @Override
    public void readNBTData(CompoundNBT nbt)
    {
        super.readNBTData(nbt);

        leaping = nbt.getBoolean("leaping");
    }

    @Override
    public CompoundNBT writeNBT()
    {
        CompoundNBT nbt = super.writeNBT();

        nbt.putBoolean("leaping", leaping);

        return nbt;
    }
}
