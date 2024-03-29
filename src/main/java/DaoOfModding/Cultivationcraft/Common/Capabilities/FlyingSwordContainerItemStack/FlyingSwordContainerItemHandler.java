package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.items.ItemStackHandler;

public class FlyingSwordContainerItemHandler extends ItemStackHandler
{
    public FlyingSwordContainerItemHandler()
    {
        super(1);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        if (slot < 0 || slot >= 1) {
            throw new IllegalArgumentException("Invalid slot number:"+slot);
        }

        if (stack.isEmpty()) return false;

        if (!(stack.getItem() instanceof SwordItem)) return false;

        return true;
    }
}
