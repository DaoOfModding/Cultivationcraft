package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FreezeTestItem extends Item {
    public FreezeTestItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        System.out.println("Used stick");
        if (!pContext.getLevel().isClientSide() && pContext.getHand() == InteractionHand.MAIN_HAND) {
            BlockPos blockPos = pContext.getClickedPos();
            BlockState state = pContext.getLevel().getBlockState(blockPos);
            System.out.println("Block pos: " + blockPos);
            System.out.println("Block state: " + state);
            // Create a new FrozenBlockEntity

            pContext.getLevel().setBlock(pContext.getClickedPos(), BlockRegister.FROZEN_BLOCK.get().defaultBlockState(), 3);

            return InteractionResult.SUCCESS;
        }
        return super.useOn(pContext);
    }

    private void FreezeBlock() {

    }
}
