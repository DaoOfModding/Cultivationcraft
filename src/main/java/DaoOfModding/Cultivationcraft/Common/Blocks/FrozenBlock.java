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
    protected static final VoxelShape BLOCK = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    BlockState[] stairStates = new BlockState[4];
    StairsBlock[] stair = new StairsBlock[4];

    public FrozenBlock()
    {
        super(AbstractBlock.Properties.create(Material.ICE).hardnessAndResistance(-1.0F, 3600000.0F).slipperiness(0.989f).noDrops().notSolid().variableOpacity());

        // Create appropriate block states for each stair direction
        stairStates[Direction.EAST.getHorizontalIndex()] = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.BOTTOM).with(StairsBlock.SHAPE, StairsShape.STRAIGHT).with(StairsBlock.WATERLOGGED, false);
        stairStates[Direction.WEST.getHorizontalIndex()] = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.BOTTOM).with(StairsBlock.SHAPE, StairsShape.STRAIGHT).with(StairsBlock.WATERLOGGED, false);
        stairStates[Direction.SOUTH.getHorizontalIndex()] = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, Half.BOTTOM).with(StairsBlock.SHAPE, StairsShape.STRAIGHT).with(StairsBlock.WATERLOGGED, false);
        stairStates[Direction.NORTH.getHorizontalIndex()] = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.BOTTOM).with(StairsBlock.SHAPE, StairsShape.STRAIGHT).with(StairsBlock.WATERLOGGED, false);

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
        TileEntity tileEntityntity = worldIn.getTileEntity(pos);

        if (worldIn != null && tileEntityntity != null)
        {
            Direction dir = ((FrozenTileEntity)tileEntityntity).getRamp();
            BlockState frozen = ((FrozenTileEntity)tileEntityntity).getFrozenBlock();

            // If the frozen block is a stair return the voxel shape of an appropriately rotated stair
            if (dir != Direction.DOWN)
                return stair[dir.getHorizontalIndex()].getShape(stairStates[dir.getHorizontalIndex()], worldIn, pos, context);
            // If the frozen block isn't air or liquid then return the VoxelShape of the frozen block
            else if (frozen.getMaterial() != Material.AIR && !frozen.getMaterial().isLiquid())
                return ((FrozenTileEntity)worldIn.getTileEntity(pos)).getFrozenBlock().getBlock().getShape(frozen, worldIn, pos, context);
        }

        // Return a normal block VoxelShape
        return BLOCK;
    }


        @Override
    public BlockRenderType getRenderType(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }
}
