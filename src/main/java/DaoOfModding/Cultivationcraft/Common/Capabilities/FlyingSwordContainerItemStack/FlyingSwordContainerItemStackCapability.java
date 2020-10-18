package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FlyingSwordContainerItemStackCapability implements ICapabilitySerializable<CompoundNBT>
{
    @CapabilityInject(IFlyingSwordContainerItemStack.class)
    public static final Capability<IFlyingSwordContainerItemStack> FSC_ITEM_STACK_CAPABILITY = null;
    private LazyOptional<IFlyingSwordContainerItemStack> instance = LazyOptional.of(FSC_ITEM_STACK_CAPABILITY::getDefaultInstance);

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IFlyingSwordContainerItemStack.class, new FlyingSwordContainerItemStackStorage(), FlyingSwordContainerItemStack::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return FSC_ITEM_STACK_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) FSC_ITEM_STACK_CAPABILITY.getStorage().writeNBT(FSC_ITEM_STACK_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        FSC_ITEM_STACK_CAPABILITY.getStorage().readNBT(FSC_ITEM_STACK_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }
}
