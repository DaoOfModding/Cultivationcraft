package DaoOfModding.Cultivationcraft.Common.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
/*
public class FrozenBlock extends Block
{
    protected static final VoxelShape BLOCK = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    BlockState[] stairStates = new BlockState[4];
    StairBlock[] stair = new StairBlock[4];

    public FrozenBlock()
    {
        super(Block.Properties.of(Material.ICE).strength(-1.0F, 3600000.0F).friction(0.989f).noLootTable().noOcclusion());

        // Create appropriate block states for each stair direction
        stairStates[Direction.EAST.get2DDataValue()] = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST).setValue(StairBlock.HALF, Half.BOTTOM).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairBlock.WATERLOGGED, false);
        stairStates[Direction.WEST.get2DDataValue()] = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST).setValue(StairBlock.HALF, Half.BOTTOM).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairBlock.WATERLOGGED, false);
        stairStates[Direction.SOUTH.get2DDataValue()] = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH).setValue(StairBlock.HALF, Half.BOTTOM).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairBlock.WATERLOGGED, false);
        stairStates[Direction.NORTH.get2DDataValue()] = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH).setValue(StairBlock.HALF, Half.BOTTOM).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairBlock.WATERLOGGED, false);

        for (int i = 0; i < 4; i++)
            stair[i] = new StairBlock(stairStates[i], this.properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        BlockEntity BlockEntityntity = worldIn.getBlockEntity(pos);

        if (worldIn != null && BlockEntityntity != null)
        {
            Direction dir = ((FrozenBlockEntity)BlockEntityntity).getRamp();
            BlockState frozen = ((FrozenBlockEntity)BlockEntityntity).getFrozenBlock();

            // If the frozen block is a stair return the voxel shape of an appropriately rotated stair
            if (dir != Direction.DOWN)
                return stair[dir.get2DDataValue()].getShape(stairStates[dir.get2DDataValue()], worldIn, pos, context);
            // If the frozen block isn't air or liquid then return the VoxelShape of the frozen block
            else if (frozen.getMaterial() != Material.AIR && !frozen.getMaterial().isLiquid())
                return ((FrozenBlockEntity)worldIn.getBlockEntity(pos)).getFrozenBlock().getBlock().getShape(frozen, worldIn, pos, context);
        }

        // Return a normal block VoxelShape
        return BLOCK;
    }


    @Override
    public RenderShape getRenderShape(BlockState iBlockState) {
        return RenderShape.MODEL;
    }
}*/
