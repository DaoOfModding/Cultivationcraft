package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack;

import net.minecraft.nbt.CompoundTag;

public interface IFlyingSwordContainerItemStack
{
    public FlyingSwordContainerItemHandler getItemStackHandler();
    public void setItemStackHandler(FlyingSwordContainerItemHandler newItem);

    public CompoundTag writeNBT();
    public void readNBT(CompoundTag NBT);
}
