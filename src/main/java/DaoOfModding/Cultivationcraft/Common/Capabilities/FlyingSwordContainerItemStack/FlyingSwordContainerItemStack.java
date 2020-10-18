package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack;

import net.minecraft.entity.player.PlayerEntity;

public class FlyingSwordContainerItemStack implements IFlyingSwordContainerItemStack
{
    private FlyingSwordContainerItemHandler item = new FlyingSwordContainerItemHandler();

    public FlyingSwordContainerItemHandler getItemStackHandler()
    {
        return item;
    }

    public void setItemStackHandler(FlyingSwordContainerItemHandler newItem)
    {
        item = newItem;
    }

    // Return a specified players CultivatorStats
    public static IFlyingSwordContainerItemStack getCapability(PlayerEntity player)
    {
        return player.getCapability(FlyingSwordContainerItemStackCapability.FSC_ITEM_STACK_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("getting Flying Sword Container Item Stack"));
    }
}
