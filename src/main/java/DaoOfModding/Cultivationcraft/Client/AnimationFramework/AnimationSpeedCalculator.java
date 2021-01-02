package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector3d;

public class AnimationSpeedCalculator
{
    public static final float defaultSpeedInTicks = 100;
    public static final float defaultSpeedPerTick = 0.02f;

    // Convert a movement in ticks to a speed value
    // Ticks is the amount of ticks it should take for position to change into destination
    public static double ticksToSpeed(Vector3d position, Vector3d destination, float Ticks)
    {
        Vector3d toMove = position.subtract(destination);
        Vector3d direction = toMove.normalize();

        double ticksNeeded = 0;

        if (direction.x != 0)
            ticksNeeded = toMove.x / direction.x;

        else if (direction.y != 0)
            ticksNeeded = toMove.y / direction.y;

        else if (direction.z != 0)
            ticksNeeded = toMove.z / direction.z;

        return Math.abs(ticksNeeded / (double)Ticks);
    }
}
