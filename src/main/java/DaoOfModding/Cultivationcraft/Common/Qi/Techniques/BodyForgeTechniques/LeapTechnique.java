package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericLimbNames;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PlayerPose;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PlayerPoseHandler;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.PoseHandler;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartNames;
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
    private boolean leaping = false;

    public LeapTechnique()
    {
        name = new TranslationTextComponent("cultivationcraft.technique.leap").getString();
        elementID = Elements.noElementID;

        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/leap.png");

        pose.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians(90), 0, 0), 10);
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


        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(player.getUniqueID());

        // Only activate if player is not already jumping
        if (handler != null && !handler.isJumping())
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

        continueTech(event);
    }

    // Start the leap if not already started, otherwise check if the technique has ended
    private void continueTech(TickEvent.PlayerTickEvent event)
    {
        if (!leaping)
            doLeap(event.player);
        else
            continueLeap(event.player);

    }

    // Check if the leap has ended
    private void continueLeap(PlayerEntity player)
    {
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(player.getUniqueID());

        // If the player is no longer jumping or the handler can't be loaded turn the technique off
        if (handler == null || !handler.isJumping())
        {
            active = false;
            leaping = false;
        }
        else
        {
            Vector3d currentMotion = player.getMotion().normalize();

            PlayerPose newPose = new PlayerPose();
            newPose.addAngle(GenericLimbNames.body, new Vector3d(Math.toRadians((currentMotion.y - 1) * -90), 0, 0), 10);

            pose = newPose;
        }
    }

    // Do the leap
    private void doLeap(PlayerEntity player)
    {
        PlayerPoseHandler handler = PoseHandler.getPlayerPoseHandler(player.getUniqueID());

        // Cancel this leap if the handler can't be loaded
        if (handler == null)
        {
            active = false;
            return;
        }

        leaping = true;
        player.setOnGround(false);

        // Tell the handler that the player is jumping
        handler.setJumping(true);

        // Get the current motion and forward vector of the player
        Vector3d currentMotion = player.getMotion();
        Vector3d forward = player.getForward().normalize();

        // Get the modified jump height of the player
        int jumpPower = BodyPartStatControl.getStats(player.getUniqueID()).getJumpHeight();

        // Move the player forward based on the jump power, as well as applying a height jump of 1 block
        player.setMotion(currentMotion.add(forward.x * jumpPower * 0.4f, 0.52f, forward.z * jumpPower * 0.4f));
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
