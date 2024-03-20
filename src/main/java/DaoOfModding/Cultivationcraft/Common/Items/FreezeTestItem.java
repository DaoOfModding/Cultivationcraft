package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
            String msg = "Block " + oldState + " at " + blockPos + " Is getting Frozen!";
            if (oldState.hasBlockEntity()) {
                oldBlockEntity = pContext.getLevel().getBlockEntity(blockPos);
                msg += "\nEntity: " + oldBlockEntity + "\nEntity Data: " + oldBlockEntity.serializeNBT();
            }
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(msg));
            // Create a new FrozenBlockEntity
            BlockState FrozenBlockState = setFrozenBlock(oldState, oldBlockEntity, oldBlockEntity.serializeNBT());

            pContext.getLevel().setBlockAndUpdate(pContext.getClickedPos(), FrozenBlockState);

            return InteractionResult.SUCCESS;
        }
        return super.useOn(pContext);
    }

    public static BlockState setFrozenBlock(BlockState blockState, @Nullable BlockEntity blockEntity, @Nullable CompoundTag blockEntityData) {
        BlockState FrozenBlock = BlockRegister.FROZEN_BLOCK.get().defaultBlockState();
        ((FrozenBlock) FrozenBlock.getBlock()).setOldBlockFields(blockState, blockEntity, blockEntityData);

        return FrozenBlock;
    }
}
