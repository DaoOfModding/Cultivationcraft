package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class Physics
{
    // Increase player jump speed based on the jump height
    public static void applyJump(PlayerEntity player)
    {
        float jumpHeight = BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.jumpHeight);

        Vector3d currentMotion = player.getDeltaMovement();

        // Increase not only the height jump but also multiply X and Z momentum
        player.setDeltaMovement(currentMotion.x + (currentMotion.x * jumpHeight * 0.2f), 0.42f + jumpHeight * 0.1f, currentMotion.z + (currentMotion.z * jumpHeight * 0.2f));
    }

    // Increase the distance you can fall without taking damage by the jump height
    public static float reduceFallDistance(PlayerEntity player, float distance)
    {
        distance -= BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.jumpHeight);

        if (distance < 0)
            distance = 0;

        return distance;
    }
}
