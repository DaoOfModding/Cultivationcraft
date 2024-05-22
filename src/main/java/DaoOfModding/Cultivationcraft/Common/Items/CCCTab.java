package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CCCTab extends CreativeModeTab {
    public CCCTab(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.DIAMOND_SWORD);
    }

    public static final CCCTab instance = new CCCTab(Cultivationcraft.MODID);
}
