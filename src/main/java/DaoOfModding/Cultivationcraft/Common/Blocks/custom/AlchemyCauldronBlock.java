package DaoOfModding.Cultivationcraft.Common.Blocks.custom;

import DaoOfModding.Cultivationcraft.Common.Blocks.entity.AlchemyCauldronBlockEntity;
import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class AlchemyCauldronBlock extends BaseEntityBlock {
    public AlchemyCauldronBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Blocks.CAULDRON.defaultBlockState().getShape(level, pos, context);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyCauldronBlockEntity(pos, state);
    }

    // Used by recipe difficulty scaling; higher-tier cauldrons can override this.
    public int getAlchemyTier(BlockState state) {
        return 1;
    }

    // Future higher-tier cauldrons can increase this interval to retain Qi longer.
    public int getQiDecayIntervalTicks(BlockState state) {
        return 100;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, BlockRegister.ALCHEMY_CAULDRON_ENTITY.get(),
                AlchemyCauldronBlockEntity::serverTick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer
                && level.getBlockEntity(pos) instanceof AlchemyCauldronBlockEntity cauldron) {
            NetworkHooks.openScreen(serverPlayer, cauldron, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof AlchemyCauldronBlockEntity cauldron) {
                Containers.dropContents(level, pos, cauldron);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
