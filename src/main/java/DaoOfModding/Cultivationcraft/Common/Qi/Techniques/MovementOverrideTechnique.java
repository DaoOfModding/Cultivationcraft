package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;

public class MovementOverrideTechnique extends Technique
{
    protected PlayerPose move = new PlayerPose();

    public MovementOverrideTechnique()
    {
        super();

        type = useType.Toggle;
        multiple = false;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        Vector3d motion = new Vector3d(event.player.getMotion().x, 0, event.player.getMotion().z);

        double speed = motion.length();

        // Determine if going in reverse or not
        if (speed > 0)
        {
            Vector3d direction = new Vector3d(event.player.getLookVec().x, 0, event.player.getLookVec().z).normalize();
            Vector3d movementDirection = motion.normalize();

            if (direction.subtract(movementDirection).length() > 1.7)
                speed *= -1;
        }

        updateSpeed(speed);

        if (speed != 0)
        {
            PoseHandler.addPose(event.player.getUniqueID(), move);
        }
    }

    // Sends the players movement speed to the technique before updating
    public void updateSpeed(double speed)
    {

    }
}
