package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class FlyingSwordContainerItemStackHandler extends ItemStackHandler
{
    private static final int size = 1;

    public FlyingSwordContainerItemStackHandler()
    {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        if (slot < 0 || slot > size)
        {
            throw new IllegalArgumentException("Invalid slot number:"+slot);
        }

        if (stack.isEmpty()) return false;

        Item item = stack.getItem();

        // TODO: Only allow weapons to become flying swords
        return true;
    }
}
