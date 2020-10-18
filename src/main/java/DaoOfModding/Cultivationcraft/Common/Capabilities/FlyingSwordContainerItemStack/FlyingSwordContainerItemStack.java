package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class FlyingSwordContainerItemStack implements IFlyingSwordContainerItemStack
{
    private ItemStack item = ItemStack.EMPTY;

    public ItemStack getItem()
    {
        return item;
    }

    public void setItem(ItemStack newItem)
    {
        item = newItem;
    }

    // Return a specified players CultivatorStats
    public static IFlyingSwordContainerItemStack getCapability(PlayerEntity player)
    {
        return player.getCapability(FlyingSwordContainerItemStackCapability.FSC_ITEM_STACK_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("getting Flying Sword Containter Item Stack"));
    }
}
