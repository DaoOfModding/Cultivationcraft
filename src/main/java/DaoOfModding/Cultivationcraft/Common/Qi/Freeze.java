package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Freeze
{
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

    private static boolean tryExtinguish(World world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock() instanceof FireBlock)
        {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());

            return true;
        }

        return false;
    }

    public static boolean isFrozen(World world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        // If the block at the specified position is a frozen block refresh the freeze and finish
        if (tileEntity!= null && tileEntity instanceof FrozenTileEntity)
            return true;

        return false;
    }

    public static boolean tryUpdateFreeze(World world, BlockPos pos, int ticks)
    {
        // If the block at the specified position is a frozen block refresh the freeze and finish
        if (isFrozen(world, pos))
        {
            ((FrozenTileEntity)world.getTileEntity(pos)).setUnfreezeTicks(ticks);
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
        if (world.isAirBlock(pos))
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
    private static void FreezeBlock(World world, BlockPos pos, int forTicks, Direction dir, boolean client)
    {
        // Get the block and tile entity at the freeze location
        BlockState toFreeze = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);

        // If the block is already frozen, update the freeze duration and do nothing more
        if (tryUpdateFreeze(world, pos, forTicks))
            return;

        world.setBlockState(pos, BlockRegister.frozenBlock.getDefaultState(), 1 + 2 + 16 + 32);
        FrozenTileEntity frozen = (FrozenTileEntity)world.getTileEntity(pos);

        // Only set half blocks for air blocks
        if (toFreeze.getMaterial() == Material.AIR && dir != Direction.DOWN)
            frozen.setRamp(dir);

        if (client)
            frozen.setIsClient();

        frozen.setUnfreezeTicks(20);
        frozen.setFrozenBlock(toFreeze, tileEntity);
    }
}