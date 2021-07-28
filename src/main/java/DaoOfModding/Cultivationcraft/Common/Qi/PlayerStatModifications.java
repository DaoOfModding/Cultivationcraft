package DaoOfModding.Cultivationcraft.Common.Qi;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class PlayerStatModifications
{
    private int jumpHeight = 0;

    public int getJumpHeight()
    {
        if (jumpHeight < 0)
            return 0;

        // Add 1 to jumpHeight as that is the height that a default player can jump
        return jumpHeight + 1;
    }

    private int getRawJumpHeight()
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
        // TODO : Unsure if delta movement is the correct mapping here
        Vector3d currentMotion = player.getDeltaMovement();

        // Increase not only the height jump but also multiply X and Z momentum
        player.setDeltaMovement(currentMotion.x + (currentMotion.x * jumpHeight * 0.2f), 0.42f + jumpHeight * 0.1f, currentMotion.z + (currentMotion.z * jumpHeight * 0.2f));
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
        // Subtract 1 from the combined jump height as jumpHeight starts at 1
        setJumpHeight(getRawJumpHeight() + newStats.getRawJumpHeight());
    }
}
