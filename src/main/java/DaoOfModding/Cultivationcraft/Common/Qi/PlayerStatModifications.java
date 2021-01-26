package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartStatControl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class PlayerStatModifications
{
    private int jumpHeight = 0;

    public int getJumpHeight()
    {
        return jumpHeight;
    }

    public void setJumpHeight(int jumpHeight)
    {
        this.jumpHeight = jumpHeight;
    }

    // Increase player jump speed based on the jump height
    public void applyJump(PlayerEntity player)
    {
        Vector3d currentMotion = player.getMotion();

        // Increase not only the height jump but also multiply X and Z momentum
        player.setMotion(currentMotion.add(currentMotion.x * jumpHeight * 0.2f, jumpHeight * 0.1f, currentMotion.z * jumpHeight * 0.2f));
    }

    // Increase the distance you can fall without taking damage by the jump height
    public float reduceFallDistance(float distance)
    {
        distance -= getJumpHeight();

        if (distance < 0)
            distance = 0;

        return distance;
    }

    public void combine(PlayerStatModifications newStats)
    {
        setJumpHeight(getJumpHeight() + newStats.getJumpHeight());
    }
}
