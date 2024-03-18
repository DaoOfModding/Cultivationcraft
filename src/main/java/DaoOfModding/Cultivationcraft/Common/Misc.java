package DaoOfModding.Cultivationcraft.Common;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;

public class Misc
{
    public static boolean enableHarvest = false;

    public static CompoundTag getNBT(ItemStack item)
    {
        CompoundTag nbtTagCompound = item.getTag();
        if (nbtTagCompound == null) {
            nbtTagCompound = new CompoundTag();
            item.setTag(nbtTagCompound);
        }

        return nbtTagCompound;
    }

    public static Entity getEntityAtLocation(Vec3 pos, ClientLevel targetWorld)
    {
        // Create a small bounding box at the specified position then search for a list of entities at that location
        AABB scan = new AABB(pos.x - 0.1, pos.y - 0.1, pos.z - 0.1, pos.x + 0.1, pos.y + 0.1, pos.z + 0.1);

        List<Entity> entities = targetWorld.getEntitiesOfClass(Entity.class, scan);

        if (!entities.isEmpty())
            if (entities.get(0).isAttackable())
                return entities.get(0);

        return null;
    }

    public static Vec3 getVec3FromBlockPos(BlockPos pos)
    {
        return new Vec3((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f);
    }

    public static boolean blockExists(Block block)
    {
        return (block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR && block != Blocks.AIR);
    }

    // Move supplied color towards white
    // 0 = no change, 1 = fully white
    public static Color saturate(Color toSaturate, float amount)
    {
        float f = 1.0F - amount;

        float r = toSaturate.getRed() * f + 255f * amount;
        float g = toSaturate.getGreen() * f + 255f * amount;
        float b = toSaturate.getBlue() * f + 255f * amount;

        return new Color(r/255f, g/255f, b/255f);
    }
}
