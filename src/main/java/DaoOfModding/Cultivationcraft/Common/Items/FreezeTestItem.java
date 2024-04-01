package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.custom.FrozenBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
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
            BlockPos secondBlockPos = getMultiblockPos(blockPos, pContext.getLevel().getBlockState(blockPos));
            if (secondBlockPos != null) {
                createFrozenBlock(pContext, secondBlockPos);
            }
            createFrozenBlock(pContext, blockPos);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(pContext);
    }

    public static void createFrozenBlock(UseOnContext pContext, BlockPos blockPos) {
        BlockState oldState = pContext.getLevel().getBlockState(blockPos);
        BlockEntity oldBlockEntity = oldState.hasBlockEntity() ? pContext.getLevel().getBlockEntity(blockPos) : null;
        CompoundTag oldBlockEntityData = oldState.hasBlockEntity() ? oldBlockEntity.serializeNBT() : null;
        if (oldBlockEntity != null) {
            pContext.getLevel().removeBlockEntity(blockPos);
        }
        BlockState FrozenBlock = setFrozenBlock(oldState, oldBlockEntity, oldBlockEntityData);
        pContext.getLevel().setBlockAndUpdate(blockPos, FrozenBlock);
    }

    public static BlockPos getMultiblockPos(BlockPos blockPos, BlockState blockState) {
        String name = blockState.getBlock().getName().getString();

        if (name.contains("Door")) {
            Property<?> half = blockState.getBlock().getStateDefinition().getProperty("half");
            return blockState.getValue(half).toString().equals("upper") ? blockPos.below() : blockPos.above();
        } else if (name.contains("Piston")) {
            Property<?> facing = blockState.getBlock().getStateDefinition().getProperty("facing");
            if (name.equals("Piston Head")) {
                return blockPos.relative(((Direction) blockState.getValue(facing)).getOpposite());
            } else {
                if (blockState.getValue((Property<Boolean>) blockState.getBlock().getStateDefinition().getProperty("extended"))) {
                    return blockPos.relative((Direction) blockState.getValue(facing));
                }
            }
        } else if (name.contains("Bed")) {
            Property<?> facing = blockState.getBlock().getStateDefinition().getProperty("facing");
            Property<?> part = blockState.getBlock().getStateDefinition().getProperty("part");
            if (blockState.getValue(part).toString().equals("foot")) {
                return blockPos.relative((Direction) blockState.getValue(facing));
            } else {
                return blockPos.relative(((Direction) blockState.getValue(facing)).getOpposite());
            }
        }

        return null;
    }

    public static BlockState setFrozenBlock(BlockState blockState, @Nullable BlockEntity blockEntity, @Nullable CompoundTag blockEntityData) {
        BlockState frozenBlock = BlockRegister.FROZEN_BLOCK.get().defaultBlockState();
        ((FrozenBlock) frozenBlock.getBlock()).setOldBlockFields(blockState, blockEntity, blockEntityData);

        return frozenBlock;
    }
}
