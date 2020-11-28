package DaoOfModding.Cultivationcraft.Common;

import java.util.UUID;

public class PlayerPoseHandler
{
    UUID playerID;

    PlayerPose currentPose = new PlayerPose();
    PlayerPose renderPose = new PlayerPose();

    boolean locked = false;

    public PlayerPoseHandler(UUID id)
    {
        playerID = id;
    }

    public UUID getID()
    {
        return playerID;
    }

    public void addPose(PlayerPose pose)
    {
        for (PlayerPose.Limb limb : PlayerPose.Limb.values())
            if (pose.hasAngle(limb))
                // If the current pose has no angle set for this limb, or the new pose has a higher priority for this limb
                if (!currentPose.hasAngle(limb) || currentPose.getPriority(limb) < pose.getPriority(limb))
                    currentPose.setAngle(limb, pose.getAngle(limb), pose.getPriority(limb));
    }

    public void updateRenderPose()
    {
        renderPose = currentPose;
        currentPose = new PlayerPose();
    }

    // Get the render pose
    public PlayerPose getRenderPose()
    {
        return renderPose;
    }
}
