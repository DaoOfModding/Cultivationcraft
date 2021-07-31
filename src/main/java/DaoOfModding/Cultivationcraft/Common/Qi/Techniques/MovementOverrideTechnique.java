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

        Vector3d motion = PoseHandler.getMovement(event.player.getUUID());
        motion.multiply(1, 0, 1);

        double speed = motion.length() * 0.75;

        Cultivationcraft.LOGGER.info(speed);

        if (speed < 0)
            speed = 0;

        // Determine if going in reverse or not
        if (speed > 0)
        {
            Vector3d direction = new Vector3d(event.player.getLookAngle().x, 0, event.player.getLookAngle().z).normalize();
            Vector3d movementDirection = motion.normalize();

            if (direction.subtract(movementDirection).length() > 1.7)
                speed *= -1;
        }

        updateSpeed(speed);

        if (speed != 0)
        {
            PoseHandler.addPose(event.player.getUUID(), move);
        }
    }

    // Sends the players movement speed to the technique before updating
    public void updateSpeed(double speed)
    {

    }
}
