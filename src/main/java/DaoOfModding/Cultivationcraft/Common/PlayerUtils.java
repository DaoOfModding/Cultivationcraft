package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;

public class PlayerUtils
{
    public static boolean lookingUp(PlayerEntity player)
    {
        return player.getLookAngle().y > 0.05;
    }

    public static boolean lookingDown(PlayerEntity player)
    {
        return player.getLookAngle().y < -0.75;
    }

    public static Direction movementDirection(PlayerEntity player)
    {
        double x = player.xOld - player.xCloak;
        double z = player.zOld - player.zCloak;

        Direction dir;

        if (Math.abs(x) > Math.abs(z))
        {
            if (x > 0)
                dir = Direction.WEST;
            else
                dir = Direction.EAST;
        }
        else
        {
            if (z > 0)
                dir = Direction.NORTH;
            else
                dir = Direction.SOUTH;
        }

        return dir;
    }

    public static Direction invertDirection(Direction dir)
    {
        if (dir == Direction.WEST)
            return Direction.EAST;

        if (dir == Direction.EAST)
            return Direction.WEST;

        if (dir == Direction.SOUTH)
            return Direction.NORTH;

        if (dir == Direction.NORTH)
            return Direction.SOUTH;

        return Direction.DOWN;
    }
}
