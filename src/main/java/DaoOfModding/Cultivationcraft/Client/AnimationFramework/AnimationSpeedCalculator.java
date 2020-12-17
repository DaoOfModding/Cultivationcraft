package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector3d;

public class AnimationSpeedCalculator
{
    public static final double defaultSpeed = 0.1;

    // Convert a movement in ticks to a speed value
    // Ticks is the amount of ticks it should take for position to change into destination
    public static double ticksToSpeed(Vector3d position, Vector3d destination, int Ticks)
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
/*
        double maxPos = getMaxValue(position);
        double maxDes = getMaxValue(destination);

        double speed = Math.abs(maxPos - maxDes);

        return speed / Ticks;
    }

    // Returns the largest value stored in a vector (ignoring sign)
    public static double getMaxValue(Vector3d vector)
    {
        double x = Math.abs(vector.x);
        double y = Math.abs(vector.y);
        double z = Math.abs(vector.z);

        if (x > y)
        {
            if (x > z)
                return vector.x;
        }
        else if (y > z)
            return vector.y;

        return vector.z;
    }*/
}
