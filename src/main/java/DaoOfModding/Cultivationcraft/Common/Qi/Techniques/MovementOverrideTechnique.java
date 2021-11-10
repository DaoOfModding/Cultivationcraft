package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPoseHandler;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;

public class MovementOverrideTechnique extends Technique
{
    public MovementOverrideTechnique()
    {
        super();

        multiple = false;
    }

    // Called when the keybinding for the specified input is pressed
    // If any of these return true, then vanilla movement for that input will be cancelled
    public boolean overwriteForward()
    {
        return false;
    }

    public boolean overwriteLeft()
    {
        return false;
    }

    public boolean overwriteRight()
    {
        return false;
    }

    public boolean overwriteBackward()
    {
        return false;
    }

    public boolean overwriteJump()
    {
        return false;
    }
}
