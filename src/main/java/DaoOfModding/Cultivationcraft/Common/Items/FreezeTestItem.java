package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FreezeTestItem extends Item {
    public FreezeTestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        System.out.println("Used stick");
        BlockPos blockPos = player.blockPosition().relative(player.getDirection());
        BlockState state = level.getBlockState(blockPos);
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            if (state.getBlock() instanceof Block) {
                // Create a new FrozenBlockEntity
                level.setBlockEntity(new FrozenBlockEntity(blockPos, state));
                System.out.println("Created FrozenBlockEntity");
                return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
            }
        }

        return super.use(level, player, hand);
    }

    private void FreezeBlock() {

    }
}
