package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SingleLegPart extends MovementOverridePart
{
    PlayerPose Idle = new PlayerPose();

    public SingleLegPart(String partID, String position, String displayNamePos, int qiToForge)
    {
        super(partID, position, displayNamePos, qiToForge);

        Idle.addAngle(BodyPartModelNames.singleLegModel, new Vec3(Math.toRadians(-30), 0, 0), GenericPoses.jumpLegPriority + 1, 5f, -1);
        Idle.addAngle(BodyPartModelNames.singleLegLowerModel, new Vec3(Math.toRadians(60), 0, 0), GenericPoses.jumpLegPriority + 1, 5f, -1);
    }

    @Override
    public void onClientTick(Player player)
    {
        Vec3 delta = Physics.getDelta(player);

        // "Retract" leg whilst falling
        if (!player.isOnGround() && !genericClientFunctions.getPlayer().isInWater() && delta.y < 0)
            PoseHandler.addPose(player.getUUID(), Idle);
    }

    @Override
    public boolean overwriteForward()
    {
        // If the player presses forward and is on the ground, then hop forward
        if (genericClientFunctions.getPlayer().isOnGround() && !genericClientFunctions.getPlayer().isInWater())
        {
            Vec3 direction = genericClientFunctions.getPlayer().getForward().normalize();
            float speed = genericClientFunctions.getPlayer().getSpeed();

            // Move player forward based on their movement speed whilst jumping 1 block high
            Vec3 movement = new Vec3(direction.x * 0.4f * speed * 10, 0.52f, direction.z * 0.4f * speed * 10);

            genericClientFunctions.getPlayer().setDeltaMovement(movement);

            return true;
        }

        return false;
    }

    @Override
    public boolean overwriteLeft()
    {
        if (genericClientFunctions.getPlayer().isOnGround() && !genericClientFunctions.getPlayer().isInWater())
            return true;

        return false;
    }

    @Override
    public boolean overwriteRight()
    {
        if (genericClientFunctions.getPlayer().isOnGround() && !genericClientFunctions.getPlayer().isInWater())
            return true;

        return false;
    }

    @Override
    public boolean overwriteBackward()
    {
        if (genericClientFunctions.getPlayer().isOnGround() && !genericClientFunctions.getPlayer().isInWater())
            return true;

        return false;
    }
}
