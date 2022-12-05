package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GlidePart extends BodyPart
{
    PlayerPose jump = new PlayerPose();

    Vec3 prevMotion = new Vec3(0, 0, 0);

    public GlidePart(String partID, String position, String displayNamePos, int qiToForge)
    {
        super(partID, position, displayNamePos, qiToForge);

        jump.addAngle(GenericLimbNames.leftArm, new Vec3(Math.toRadians(0), 0, Math.toRadians(-90)), GenericPoses.jumpArmPriority + 5);
        jump.addAngle(GenericLimbNames.rightArm, new Vec3(Math.toRadians(0), 0, Math.toRadians(90)), GenericPoses.jumpArmPriority + 5);
    }

    @Override
    public void onClientTick(Player player)
    {
        // Do nothing if the player is not in the air
        if (player.isOnGround() || player.isInWater())
            return;

        player.fallDistance = 0;

        float weightModifier = BodyPartStatControl.getPlayerStatControl(player.getUUID()).getFlightWeightModifier();

        float maxHorizontalSpeed = 0.75f;
        float horizontalSpeedIncrease = 0.025f * weightModifier;
        float minFallSpeed = -0.005f * (float)Math.pow((1.0f / weightModifier), 2);

        // Get direction of player movement
        Vec3 currentMotion = Physics.getDelta(player);

        double horizontalSpeed = Math.abs(currentMotion.x) + Math.abs(currentMotion.z);

        double speedModifer = 0;

        // Slow down if moving above max speed, otherwise SLOWLY ramp up to it
        if (horizontalSpeed < maxHorizontalSpeed)
            speedModifer = horizontalSpeedIncrease;

        Vec3 direction = new Vec3(player.getLookAngle().x, 0, player.getLookAngle().z).normalize();

        double xMotion = currentMotion.x + direction.x * speedModifer;
        double zMotion = currentMotion.z + direction.z * speedModifer;
        double yMotion = currentMotion.y;

        double horizontalSpeedPercentage = (1 - (horizontalSpeed / maxHorizontalSpeed));

        // Only reduce fall speed if falling
        // Adjust fall speed based on horizontal movement
        if (yMotion < 0)
            yMotion = minFallSpeed + ((currentMotion.y - minFallSpeed) * horizontalSpeedPercentage);

        player.setDeltaMovement(xMotion, yMotion, zMotion);

        // Add gliding pose
        PlayerPose newPose = jump.clone();

        double angle = 0;

        // Only do turning animations if going above a certain speed and falling
        if (horizontalSpeedPercentage > 0.4 && currentMotion.y < 0)
            angle = Math.atan2(prevMotion.z, prevMotion.x) - Math.atan2(direction.z, direction.x);

        // Stop the angle from randomly flippin' out for no reason sometimes
        if (angle > 0.2)
            angle = 0.2;
        else if (angle < -0.2)
            angle = -0.2;

        // Rotated the x rotation based on horizontal speed
        // With some give in either direction
        double xRot = 1.75 * (1 - horizontalSpeedPercentage) - 0.375;

        if (xRot < 0)
            xRot = 0;
        else if (xRot > 1)
            xRot = 1;

        // Calculate xRot based on horizontal movement speed
        // Calculate zRot based on rotation
        newPose.addAngle(GenericLimbNames.body, new Vec3(Math.toRadians(90 * xRot), 0, Math.toRadians(180 * angle)), 11);
        PoseHandler.addPose(player.getUUID(), newPose);

        prevMotion = direction;
    }
}
