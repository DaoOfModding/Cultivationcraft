package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashMap;
import java.util.UUID;

public class Physics
{
    private static HashMap<UUID, Double> fallSpeed = new HashMap<>();

    // Increase player jump speed based on the jump height
    public static void applyJump(PlayerEntity player)
    {
        float jumpHeight = BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.jumpHeight);

        Vector3d currentMotion = player.getDeltaMovement();

        // Increase not only the height jump but also multiply X and Z momentum
        player.setDeltaMovement(currentMotion.x + (currentMotion.x * jumpHeight * 0.2f), 0.42f + jumpHeight * 0.1f, currentMotion.z + (currentMotion.z * jumpHeight * 0.2f));
    }

    public static void Bounce(PlayerEntity player)
    {
        float bounceHeight = BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.bounceHeight);

        // Do nothing if the player has no bounce stat
        if (bounceHeight == 0)
            return;

        // Do nothing if the player is in water
        if (player.isInWater())
        {
            fallSpeed.remove(player.getUUID());
            return;
        }

        // If the player is on the ground then bounce if they have been falling, otherwise do nothing
        if (player.isOnGround())
        {
            if (fallSpeed.containsKey(player.getUUID()))
            {
                double bounce = fallSpeed.get(player.getUUID()) * -bounceHeight;

                // Only bounce if above a certain threshold, to stop infinite micro-bounces
                if (bounce > 0.25)
                    player.setDeltaMovement(player.getDeltaMovement().x, bounce, player.getDeltaMovement().z);

                fallSpeed.remove(player.getUUID());
            }

            return;
        }

        // If the player is falling place their fall speed into the fallSpeed hashmap
        double fall = player.getDeltaMovement().y;

        if (fall < 0)
            fallSpeed.put(player.getUUID(), fall);
        else
            fallSpeed.remove(player.getUUID());
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
