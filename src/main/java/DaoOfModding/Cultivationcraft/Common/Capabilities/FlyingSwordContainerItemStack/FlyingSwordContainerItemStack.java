package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;

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
    public static IFlyingSwordContainerItemStack getCapability(Player player)
    {
        return player.getCapability(FlyingSwordContainerItemStackCapability.INSTANCE).orElseThrow(() -> new IllegalArgumentException("getting Flying Sword Container Item Stack"));
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = getItemStackHandler().serializeNBT();

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        getItemStackHandler().deserializeNBT((CompoundTag)nbt);
    }
}
