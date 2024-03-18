package DaoOfModding.Cultivationcraft.Common.Blocks;

import DaoOfModding.Cultivationcraft.Common.Blocks.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrozenBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public FrozenBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    /* BLOCK ENTITY */
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockRegister.FROZEN_BLOCK_ENTITY.get().create(pos, state);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FrozenBlockEntity frozenBlockEntity) {
                frozenBlockEntity.freezeBlock(pos);
                player.sendSystemMessage(Component.literal("FrozenBlockEntity has been used"));
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return super.use(state, level, pos, player, hand, blockHitResult);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
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
