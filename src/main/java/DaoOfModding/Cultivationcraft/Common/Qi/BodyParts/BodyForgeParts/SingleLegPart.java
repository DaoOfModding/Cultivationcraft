package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.mlmanimator.Client.Poses.GenericPoses;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class SingleLegPart extends MovementOverridePart
{
    PlayerPose Idle = new PlayerPose();

    public SingleLegPart(String partID, String position, String displayNamePos, int qiToForge)
    {
        super(partID, position, displayNamePos, qiToForge);

        Idle.addAngle(BodyPartModelNames.singleLegModel, new Vector3d(Math.toRadians(-30), 0, 0), GenericPoses.jumpLegPriority + 1, 5f, -1);
        Idle.addAngle(BodyPartModelNames.singleLegLowerModel, new Vector3d(Math.toRadians(60), 0, 0), GenericPoses.jumpLegPriority + 1, 5f, -1);
    }

    @Override
    public void onClientTick(PlayerEntity player)
    {
        Vector3d delta = PoseHandler.getPlayerPoseHandler(player.getUUID()).getDeltaMovement();

        // "Retract" leg whilst falling
        if (!player.isOnGround() && !Minecraft.getInstance().player.isInWater() && delta.y < 0)
            PoseHandler.addPose(player.getUUID(), Idle);
    }

    @Override
    public boolean overwriteForward()
    {
        // If the player presses forward and is on the ground, then hop forward
        if (Minecraft.getInstance().player.isOnGround() && !Minecraft.getInstance().player.isInWater())
        {
            Vector3d direction = Minecraft.getInstance().player.getForward().normalize();
            float speed = Minecraft.getInstance().player.getSpeed();

            // Move player forward based on their movement speed whilst jumping 1 block high
            Vector3d movement = new Vector3d(direction.x * 0.4f * speed * 10, 0.52f, direction.z * 0.4f * speed * 10);

            Minecraft.getInstance().player.setDeltaMovement(movement);

            return true;
        }

        return false;
    }

    @Override
    public boolean overwriteLeft()
    {
        if (Minecraft.getInstance().player.isOnGround() && !Minecraft.getInstance().player.isInWater())
            return true;

        return false;
    }

    @Override
    public boolean overwriteRight()
    {
        if (Minecraft.getInstance().player.isOnGround() && !Minecraft.getInstance().player.isInWater())
            return true;

        return false;
    }

    @Override
    public boolean overwriteBackward()
    {
        if (Minecraft.getInstance().player.isOnGround() && !Minecraft.getInstance().player.isInWater())
            return true;

        return false;
    }
}
