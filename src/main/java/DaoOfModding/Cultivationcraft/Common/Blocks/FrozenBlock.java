package DaoOfModding.Cultivationcraft.Common.Blocks;

import DaoOfModding.Cultivationcraft.Common.Blocks.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrozenBlock extends AbstractGlassBlock implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    BlockState oldBlockState = null;
    BlockEntity oldBlockEntity = null;
    CompoundTag oldBlockEntityData = null;

    public FrozenBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH));
    }

    public void setOldBlockFields(BlockState oldBlockState, BlockEntity oldBlockEntity, CompoundTag oldBlockEntityData) {
        this.oldBlockState = oldBlockState;
        this.oldBlockEntity = oldBlockEntity;
        this.oldBlockEntityData = oldBlockEntityData;

        System.out.println("Frozen Block Fields set");
        System.out.println(this.oldBlockState + " " + this.oldBlockEntity + " " + this.oldBlockEntityData);
    }

    public BlockState getOldBlockState() {
        return oldBlockState;
    }

    public BlockEntity getOldBlockEntity() {
        return oldBlockEntity;
    }

    public CompoundTag getOldBlockEntityData() {
        return oldBlockEntityData;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }
/*    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldBlockState, boolean trigger) {
        super.onPlace(state, level, pos, oldBlockState, trigger);
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FrozenBlockEntity) {
                ((FrozenBlockEntity) blockEntity).setFrozenBlock(this.oldBlockState != null ? this.oldBlockState : oldBlockState, pos, this.oldBlockEntity, this.oldBlockEntityData);
                System.out.println("Frozen Block Entity set");
                System.out.println(this.oldBlockState + " " + pos + " " + this.oldBlockEntity + " " + this.oldBlockEntityData);
            }
        }
    }*/

    /* BLOCK ENTITY */
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new FrozenBlockEntity(pos, state, oldBlockState, oldBlockEntity, oldBlockEntityData);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return TickableBlockEntity.getTickerHelper(level);
    }
}



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
        BlockEntity BlockEntity = worldIn.getBlockEntity(pos);

        if (worldIn != null && BlockEntity != null)
        {
            Direction dir = ((FrozenBlockEntity)BlockEntity).getRamp();
            BlockState frozen = ((FrozenBlockEntity)BlockEntity).getFrozenBlock();

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
