package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BodyModificationsCapability implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    public static final Capability<IBodyModifications> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    private final IBodyModifications backend = new BodyModifications();
    private final LazyOptional<IBodyModifications> optionalData = LazyOptional.of(() -> backend);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return INSTANCE.orEmpty(cap, this.optionalData);
    }

    void invalidate() {
        this.optionalData.invalidate();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.backend.write();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.backend.read(nbt);
    }

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IBodyModifications.class);
    }

    /*
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return BODY_MODIFICATIONS_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public CompoundTag serializeNBT() {
        return (CompoundTag) BODY_MODIFICATIONS_CAPABILITY.getStorage().writeNBT(BODY_MODIFICATIONS_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        BODY_MODIFICATIONS_CAPABILITY.getStorage().readNBT(BODY_MODIFICATIONS_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }*/
}
