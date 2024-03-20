package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FreezeTestItem extends Item {
    public FreezeTestItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide() && pContext.getHand() == InteractionHand.MAIN_HAND) {
            BlockPos blockPos = pContext.getClickedPos();
            BlockState oldState = pContext.getLevel().getBlockState(blockPos);
            BlockEntity oldBlockEntity = null;
            CompoundTag oldBlockEntityData = null;
            String msg = "Block " + oldState + " at " + blockPos + " Is getting Frozen!";
            if (oldState.hasBlockEntity()) {
                oldBlockEntity = pContext.getLevel().getBlockEntity(blockPos);
                oldBlockEntityData = oldBlockEntity.serializeNBT();

                msg += "\nEntity: " + oldBlockEntity + "\nEntity Data: " + oldBlockEntityData;
            }
            System.out.println(msg);
            // Create a new FrozenBlockEntity
            BlockState FrozenBlockState = setFrozenBlock(oldState, oldBlockEntity, oldBlockEntityData);

            if (oldBlockEntity != null) {
                pContext.getLevel().removeBlockEntity(blockPos);
            }
            handleMultiBlock(pContext, oldState, blockPos, FrozenBlockState);

            return InteractionResult.SUCCESS;
        }
        return super.useOn(pContext);
    }

    public static BlockState setFrozenBlock(BlockState blockState, @Nullable BlockEntity blockEntity, @Nullable CompoundTag blockEntityData) {
        BlockState FrozenBlock = BlockRegister.FROZEN_BLOCK.get().defaultBlockState();
        ((FrozenBlock) FrozenBlock.getBlock()).setOldBlockFields(blockState, blockEntity, blockEntityData);

        return FrozenBlock;
    }

    public static boolean handleMultiBlock(UseOnContext pContext, BlockState blockState, BlockPos blockPos, BlockState FrozenBlockState) {
        return pContext.getLevel().setBlockAndUpdate(pContext.getClickedPos(), FrozenBlockState);
    }
}
