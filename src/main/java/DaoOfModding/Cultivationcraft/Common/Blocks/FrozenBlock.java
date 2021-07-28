package DaoOfModding.Cultivationcraft.Common.Blocks;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class FrozenBlock extends Block
{
    protected static final VoxelShape BLOCK = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    BlockState[] stairStates = new BlockState[4];
    StairsBlock[] stair = new StairsBlock[4];

    public FrozenBlock()
    {
        super(AbstractBlock.Properties.of(Material.ICE).strength(-1.0F, 3600000.0F).friction(0.989f).noDrops().noOcclusion());

        // Create appropriate block states for each stair direction
        stairStates[Direction.EAST.get2DDataValue()] = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairsBlock.FACING, Direction.EAST).setValue(StairsBlock.HALF, Half.BOTTOM).setValue(StairsBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairsBlock.WATERLOGGED, false);
        stairStates[Direction.WEST.get2DDataValue()] = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairsBlock.FACING, Direction.WEST).setValue(StairsBlock.HALF, Half.BOTTOM).setValue(StairsBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairsBlock.WATERLOGGED, false);
        stairStates[Direction.SOUTH.get2DDataValue()] = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairsBlock.FACING, Direction.SOUTH).setValue(StairsBlock.HALF, Half.BOTTOM).setValue(StairsBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairsBlock.WATERLOGGED, false);
        stairStates[Direction.NORTH.get2DDataValue()] = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairsBlock.FACING, Direction.NORTH).setValue(StairsBlock.HALF, Half.BOTTOM).setValue(StairsBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairsBlock.WATERLOGGED, false);

        for (int i = 0; i < 4; i++)
            stair[i] = new StairsBlock(stairStates[i], this.properties);

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
        TileEntity tileEntityntity = worldIn.getBlockEntity(pos);

        if (worldIn != null && tileEntityntity != null)
        {
            Direction dir = ((FrozenTileEntity)tileEntityntity).getRamp();
            BlockState frozen = ((FrozenTileEntity)tileEntityntity).getFrozenBlock();

            // If the frozen block is a stair return the voxel shape of an appropriately rotated stair
            if (dir != Direction.DOWN)
                return stair[dir.get2DDataValue()].getShape(stairStates[dir.get2DDataValue()], worldIn, pos, context);
            // If the frozen block isn't air or liquid then return the VoxelShape of the frozen block
            else if (frozen.getMaterial() != Material.AIR && !frozen.getMaterial().isLiquid())
                return ((FrozenTileEntity)worldIn.getBlockEntity(pos)).getFrozenBlock().getBlock().getShape(frozen, worldIn, pos, context);
        }

        // Return a normal block VoxelShape
        return BLOCK;
    }


    @Override
    public BlockRenderType getRenderShape(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }
}
