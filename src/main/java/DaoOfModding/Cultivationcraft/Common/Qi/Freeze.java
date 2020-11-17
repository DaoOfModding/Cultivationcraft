package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Freeze
{
    public static void Freeze(World world, BlockPos pos, boolean freezeAir)
    {
        // Get the block and tile entity at the freeze location
        BlockState toFreeze = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (toFreeze == null)
            return;

        // If the block at the specified position is a frozen block refresh the freeze and finish
        if (tileEntity!= null && tileEntity instanceof FrozenTileEntity)
        {
            ((FrozenTileEntity)tileEntity).setUnfreezeTicks(20);
            return;
        }

        // Don't freeze air if told not to
        if (!freezeAir)
            if (toFreeze.getBlock() == Blocks.AIR
                    || toFreeze.getBlock() == Blocks.CAVE_AIR
                    || toFreeze.getBlock() == Blocks.VOID_AIR)
                return;

        world.setBlockState(pos, BlockRegister.frozenBlock.getDefaultState());
        FrozenTileEntity frozen = (FrozenTileEntity)world.getTileEntity(pos);

        frozen.setFrozenBlock(toFreeze, tileEntity);
        frozen.setUnfreezeTicks(20);
    }
}
