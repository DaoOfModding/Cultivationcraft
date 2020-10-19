package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack;

import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBind;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
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

        if (FlyingSwordBind.getFlyingSwordBind(stack) != null)
            return true;

        return false;
    }
}
