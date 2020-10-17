package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainer;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FlyingSwordContainerCapability implements ICapabilitySerializable<INBT>
{
    FlyingSwordContainerItemStackHandler stackHandler = new FlyingSwordContainerItemStackHandler();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == capability)
            return (LazyOptional<T>) (lazyInitialisionSupplier);
        return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(stackHandler, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(stackHandler, null, nbt);
    }

    private FlyingSwordContainerItemStackHandler getInventory()
    {
        return stackHandler;
    }

    private final LazyOptional<IItemHandler> lazyInitialisionSupplier = LazyOptional.of(this::getInventory);
}
