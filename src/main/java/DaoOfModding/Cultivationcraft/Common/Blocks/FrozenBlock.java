package DaoOfModding.Cultivationcraft.Common.Blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class FrozenBlock extends Block
{
    protected static final VoxelShape SLAB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape BLOCK = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public FrozenBlock()
    {
        super(AbstractBlock.Properties.create(Material.ICE).hardnessAndResistance(-1.0F, 3600000.0F).slipperiness(0.989f).noDrops().notSolid().variableOpacity());
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new FrozenTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if (worldIn != null && worldIn.getTileEntity(pos) != null)
        {
            if (((FrozenTileEntity)worldIn.getTileEntity(pos)).isRamp())
                return SLAB;


            //return ((FrozenTileEntity)worldIn.getTileEntity(pos)).getFrozenBlock().getBlock().getShape(state, worldIn, pos, context);
        }

        return BLOCK;
    }


        @Override
    public BlockRenderType getRenderType(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }
}
