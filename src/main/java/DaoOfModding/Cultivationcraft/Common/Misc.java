package DaoOfModding.Cultivationcraft.Common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;
import java.util.Set;

public class Misc
{
    public static boolean enableHarvest = false;

    public static CompoundNBT getNBT(ItemStack item)
    {
        CompoundNBT nbtTagCompound = item.getTag();
        if (nbtTagCompound == null) {
            nbtTagCompound = new CompoundNBT();
            item.setTag(nbtTagCompound);
        }

        return nbtTagCompound;
    }

    public static Entity getEntityAtLocation(Vector3d pos, World targetWorld)
    {
        // Create a small bounding box at the specified position then search for a list of entities at that location
        AxisAlignedBB scan = new AxisAlignedBB(pos.x - 0.1, pos.y - 0.1, pos.z - 0.1, pos.x + 0.1, pos.y + 0.1, pos.z + 0.1);

        List<Entity> entities = targetWorld.getEntitiesWithinAABB(Entity.class, scan);

        if (!entities.isEmpty())
            if (entities.get(0).canBeAttackedWithItem())
                return entities.get(0);

        return null;
    }

    public static Vector3d getVector3dFromBlockPos(BlockPos pos)
    {
        return new Vector3d((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f);
    }

    public static boolean blockExists(Block block)
    {
        return (block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR && block != Blocks.AIR);
    }
}
