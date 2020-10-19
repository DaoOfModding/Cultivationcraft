package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind;


import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FlyingSwordBindCapability implements ICapabilitySerializable<CompoundNBT>
{
    @CapabilityInject(IFlyingSwordBind.class)
    public static final Capability<IFlyingSwordBind> FLYING_SWORD_BIND_CAPABILITY_CAPABILITY = null;
    private LazyOptional<IFlyingSwordBind> instance = LazyOptional.of(FLYING_SWORD_BIND_CAPABILITY_CAPABILITY::getDefaultInstance);

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IFlyingSwordBind.class, new FlyingSwordBindStorage(), FlyingSwordBind::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return FLYING_SWORD_BIND_CAPABILITY_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) FLYING_SWORD_BIND_CAPABILITY_CAPABILITY.getStorage().writeNBT(FLYING_SWORD_BIND_CAPABILITY_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        FLYING_SWORD_BIND_CAPABILITY_CAPABILITY.getStorage().readNBT(FLYING_SWORD_BIND_CAPABILITY_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }
}