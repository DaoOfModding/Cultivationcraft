package DaoOfModding.Cultivationcraft.Common.Qi;

import net.minecraft.core.Direction;

public class Freeze
{
    // TODO: Fix freezing
    /*
    // TODO: Freeze logic for containers

    // Freeze all blocks in an area around center for range blocks
    public static void FreezeArea(World world, BlockPos center, int range, int forTicks)
    {
        for (int x = -range; x < range; x++)
            for (int y = -range; y < range; y++)
                for (int z = -range; z < range; z++)
                    if (Math.abs(x) + Math.abs(y) + Math.abs(z) <= range)
                        Freeze(world, new BlockPos(center.getX() + x, center.getY() + y, center.getZ() + z), forTicks);
    }

    protected static boolean tryExtinguish(World world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock() instanceof FireBlock)
        {
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

            return true;
        }

        return false;
    }

    public static boolean isFrozen(World world, BlockPos pos)
    {
        BlockEntity BlockEntity = world.getBlockEntity(pos);

        // If the block at the specified position is a frozen block refresh the freeze and finish
        if (BlockEntity!= null && BlockEntity instanceof FrozenBlockEntity)
            return true;

        return false;
    }

    public static boolean tryUpdateFreeze(World world, BlockPos pos, int ticks)
    {
        // If the block at the specified position is a frozen block refresh the freeze and finish
        if (isFrozen(world, pos))
        {
            ((FrozenBlockEntity)world.getBlockEntity(pos)).setUnfreezeTicks(ticks);
            return true;
        }

        return false;
    }

    // Try to freeze the block at the specified location
    public static void Freeze(World world, BlockPos pos, int forTicks)
    {
        // Don't freeze blocks above build height
        if (pos.getY() > 255)
            return;

        // Don't freeze air
        if (world.isEmptyBlock(pos))
            return;

        // If the block is fire, extinguish the fire instead of freezing
        if (tryExtinguish(world, pos))
            return;

        FreezeBlock(world, pos, forTicks, Direction.DOWN, false);
    }


    // Freeze blocks including air
    public static void FreezeAir(World world, BlockPos pos, int forTicks, Direction dir)
    {
        // Extinguish any fires at location
        tryExtinguish(world, pos);

        FreezeBlock(world, pos, forTicks, dir, false);
    }

    public static void FreezeAirAsClient(World world, BlockPos pos, int forTicks, Direction dir)
    {
        // Extinguish any fires at location
        tryExtinguish(world, pos);

        FreezeBlock(world, pos, forTicks, dir, true);
    }

    // Freeze the block at the specified location
    protected static void FreezeBlock(World world, BlockPos pos, int forTicks, Direction dir, boolean client)
    {
        // Get the block and tile entity at the freeze location
        BlockState toFreeze = world.getBlockState(pos);
        BlockEntity BlockEntity = world.getBlockEntity(pos);

        // If the block is already frozen, update the freeze duration and do nothing more
        if (tryUpdateFreeze(world, pos, forTicks))
            return;

        world.setBlock(pos, BlockRegister.frozenBlock.defaultBlockState(), 1 + 2 + 16 + 32);
        FrozenBlockEntity frozen = (FrozenBlockEntity)world.getBlockEntity(pos);

        // Only set half blocks for air blocks
        if (toFreeze.getMaterial() == Material.AIR && dir != Direction.DOWN)
            frozen.setRamp(dir);

        if (client)
            frozen.setIsClient();

        frozen.setUnfreezeTicks(20);
        frozen.setFrozenBlock(toFreeze, BlockEntity);
    }*/
}
