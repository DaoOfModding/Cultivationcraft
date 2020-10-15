package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Misc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class CultivatorStats implements ICultivatorStats
{
    private double flyingItemSpeed = 0.06;
    private double flyingItemMAXSpeed = 2;
    private double flyingItemturnSpeed = 0.4;
    private double flyingItemControlRange = 50;

    private RayTraceResult.Type targetType = RayTraceResult.Type.MISS;
    private Entity targetEntity = null;
    private BlockPos targetBlock = null;

    private boolean recallOn = false;

    private boolean disconnected = false;

    public double getFlyingItemSpeed()
    {
        return  flyingItemSpeed;
    }

    public void setFlyingItemSpeed(double newSpeed)
    {
        flyingItemSpeed = newSpeed;
    }

    public double getFlyingItemTurnSpeed()
    {
        return  flyingItemturnSpeed;
    }

    public void setFlyingItemTurnSpeed(double newSpeed)
    {
        flyingItemturnSpeed = newSpeed;
    }

    public double getFlyingItemMaxSpeed()
    {
        return flyingItemMAXSpeed;
    }

    public void setFlyingItemMaxSpeed(double newSpeed)
    {
        flyingItemMAXSpeed = newSpeed;
    }

    public double getFlyingControlRange()
    {
        return flyingItemControlRange;
    }

    public void setFlyingControlRange(double newRange)
    {
        flyingItemControlRange = newRange;
    }

    // Returns a Vector3d containing the location of the target, should ONLY be called if hasTarget is true
    public Vector3d getTarget()
    {
        if(targetType == RayTraceResult.Type.BLOCK)
        {
            return Misc.getVector3dFromBlockPos(targetBlock);
        }
        else if(targetType == RayTraceResult.Type.ENTITY)
        {
            return targetEntity.getPositionVec();
        }
        else
        {
            // THIS SHOULD NEVER BE REACHED

            return new Vector3d(0, 0, 0);
        }
    }

    public RayTraceResult.Type getTargetType()
    {
        return targetType;
    }

    // Returns the UUID of the target if it is an entity
    public UUID getTargetID()
    {
        if (targetType == RayTraceResult.Type.ENTITY)
            return targetEntity.getUniqueID();

        return null;
    }

    public void setTarget(Vector3d pos, RayTraceResult.Type type, World targetWorld, UUID targetID)
    {
        targetType = RayTraceResult.Type.MISS;

        if (type == RayTraceResult.Type.BLOCK)
        {
            // Set the target to the block at the specified position
            targetBlock = new BlockPos(pos);

            // Set the target type to block if the targeted block isn't air
            if (Misc.blockExists(targetWorld.getBlockState(targetBlock).getBlock()))
                targetType = type;
        }
        else if (type == RayTraceResult.Type.ENTITY)
        {
            // Create a large bounding box at the specified position then search for a list of entities at that location
            AxisAlignedBB scan = new AxisAlignedBB(pos.x - 10, pos.y - 10, pos.z - 10, pos.x + 10, pos.y + 10, pos.z + 10);

            List<Entity> entities = targetWorld.getEntitiesWithinAABB(Entity.class, scan);

            // If entities have been found, search through and try to find one with the targetID
            if (!entities.isEmpty())
            {
                for (Entity testEntity : entities)
                    if (testEntity.getUniqueID().equals(targetID))
                    {
                        targetEntity = testEntity;
                        targetType = type;
                    }
            }
        }
    }

    // Returns whether a target has been set
    public boolean hasTarget(World world)
    {
        return checkTargets(world);
    }

    // Checks whether current targets are valid, clearing the appropriate variables if they are not
    // Returns true if there is a valid target, false if not.
    private boolean checkTargets(World world)
    {
        if (targetType == RayTraceResult.Type.ENTITY)
            return checkEntityTarget();
        else if (targetType == RayTraceResult.Type.BLOCK)
            return checkBlockTarget(world);

        return false;
    }

    private boolean checkEntityTarget()
    {
        if (targetEntity != null)
        {
            if (targetEntity.isAlive())
                return true;
            else
                targetEntity = null;
        }

        targetType = RayTraceResult.Type.MISS;

        return false;
    }

    private boolean checkBlockTarget(World world)
    {
        if (targetBlock != null && Misc.blockExists(world.getBlockState(targetBlock).getBlock()))
            return true;

        targetType = RayTraceResult.Type.MISS;
        targetBlock = null;

        return false;
    }

    public boolean getRecall()
    {
        return recallOn;
    }
    public void setRecall(boolean recall)
    {
        recallOn = recall;
    }

    // Return a specified players CultivatorStats
    public static ICultivatorStats getCultivatorStats(PlayerEntity player)
    {
        return player.getCapability(CultivatorStatsCapability.CULTIVATOR_STATS_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("getting cultivator stats"));
    }


    public void setDisconnected(boolean value)
    {
        disconnected = value;
    }

    public boolean isDisconnected()
    {
        return disconnected;
    }
}
