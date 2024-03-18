package DaoOfModding.Cultivationcraft.Common.Items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab CC_DEBUG_TAB = new CreativeModeTab("cultivationcrafttab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemHandler.FREEZE_TEST_ITEM.get());
        }
    };
}
