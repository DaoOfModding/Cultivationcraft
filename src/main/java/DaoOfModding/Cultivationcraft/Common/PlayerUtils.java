package DaoOfModding.Cultivationcraft.Common;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class PlayerUtils
{
    public static boolean isClientPlayerCharacter(Player player)
    {
        return (player.level.isClientSide && Minecraft.getInstance().player.getUUID().compareTo(player.getUUID()) == 0);
    }

    public static Direction vectorToDirection(Vec3 direction)
    {
        Direction dir;

        if (direction.y > 0.4)
            dir = Direction.UP;
        else if (direction.y < -0.4)
            dir = Direction.DOWN;
        else if (Math.abs(direction.x) > Math.abs(direction.z))
        {
            if (direction.x > 0.0D) {
                dir = Direction.WEST;
            } else {
                dir = Direction.EAST;
            }
        } else if (direction.z > 0.0D) {
            dir = Direction.NORTH;
        } else {
            dir = Direction.SOUTH;
        }

        return dir;
    }
}
