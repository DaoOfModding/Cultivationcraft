package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Freeze
{
    // Freeze all blocks in an area around center for range blocks
    public static void FreezeArea(World world, BlockPos center, int range, int forTicks)
    {
        for (int x = -range; x < range; x++)
            for (int y = -range; y < range; y++)
                for (int z = -range; z < range; z++)
                    if (Math.abs(x) + Math.abs(y) + Math.abs(z) <= range)
                        Freeze(world, new BlockPos(center.getX() + x, center.getY() + y, center.getZ() + z), false, forTicks);
    }


    public static void Freeze(World world, BlockPos pos, boolean freezeAir, int forTicks)
    {
        // Don't freeze air if told not to
        if (!freezeAir)
            if (world.isAirBlock(pos))
                return;

        // Get the block and tile entity at the freeze location
        BlockState toFreeze = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (toFreeze == null)
            return;

        // If the block is fire, extinguish the fire
        if (toFreeze.getBlock() instanceof FireBlock)
        {
            toFreeze = Blocks.AIR.getDefaultState();
            world.setBlockState(pos, toFreeze);

            if (!freezeAir)
                return;
        }

        // If the block at the specified position is a frozen block refresh the freeze and finish
        if (tileEntity!= null && tileEntity instanceof FrozenTileEntity)
        {
            ((FrozenTileEntity)tileEntity).setUnfreezeTicks(forTicks);
            return;
        }

        world.setBlockState(pos, BlockRegister.frozenBlock.getDefaultState());
        FrozenTileEntity frozen = (FrozenTileEntity)world.getTileEntity(pos);

        frozen.setFrozenBlock(toFreeze, tileEntity);
        frozen.setUnfreezeTicks(20);
    }
}
