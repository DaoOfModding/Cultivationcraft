package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class FlyingSwordContainerItemStackStorage implements Capability.IStorage<IFlyingSwordContainerItemStack>
{
    @Override
    public INBT writeNBT(Capability<IFlyingSwordContainerItemStack> capability, IFlyingSwordContainerItemStack instance, Direction side)
    {

        CompoundNBT nbt = instance.getItem().serializeNBT();

        return nbt;
    }

    @Override
    public void readNBT(Capability<IFlyingSwordContainerItemStack> capability, IFlyingSwordContainerItemStack instance, Direction side, INBT nbt)
    {
        if (!(instance instanceof IFlyingSwordContainerItemStack))
            throw new IllegalArgumentException("Tried to read Cultivator Stats from non CultivatorStats instance");

        ItemStack newItem = ItemStack.EMPTY;
        newItem.deserializeNBT((CompoundNBT)nbt);
        instance.setItem(newItem);
    }
}