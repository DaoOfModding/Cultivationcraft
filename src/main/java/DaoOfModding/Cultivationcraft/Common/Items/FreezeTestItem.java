package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FreezeTestItem extends Item {
    public FreezeTestItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide() && pContext.getHand() == InteractionHand.MAIN_HAND) {
            BlockPos blockPos = pContext.getClickedPos();
            BlockState state = pContext.getLevel().getBlockState(blockPos);
            BlockEntity blockEntity = null;
            if (state.hasBlockEntity()) {
                blockEntity = pContext.getLevel().getBlockEntity(blockPos);
                System.out.println("Block entity: " + blockEntity);
                System.out.println("Block entity data: " + blockEntity.getPersistentData());
            }
            System.out.println("Block state: " + state);
            // Create a new FrozenBlockEntity
            pContext.getLevel().setBlockAndUpdate(pContext.getClickedPos(), BlockRegister.FROZEN_BLOCK.get().defaultBlockState());

            return InteractionResult.SUCCESS;
        }
        return super.useOn(pContext);
    }

    private void FreezeBlock() {

    }
}
