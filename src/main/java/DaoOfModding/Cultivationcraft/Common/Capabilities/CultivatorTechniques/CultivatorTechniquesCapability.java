package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CultivatorTechniquesCapability implements ICapabilitySerializable<CompoundNBT>
{
    @CapabilityInject(ICultivatorTechniques.class)
    public static final Capability<ICultivatorTechniques> CULTIVATOR_TECHINQUES_CAPABILITY_CAPABILITY = null;
    private LazyOptional<ICultivatorTechniques> instance = LazyOptional.of(CULTIVATOR_TECHINQUES_CAPABILITY_CAPABILITY::getDefaultInstance);

    public static void register()
    {
        CapabilityManager.INSTANCE.register(ICultivatorTechniques.class, new CultivatorTechinquesStorage(), CultivatorTechniques::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return CULTIVATOR_TECHINQUES_CAPABILITY_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) CULTIVATOR_TECHINQUES_CAPABILITY_CAPABILITY.getStorage().writeNBT(CULTIVATOR_TECHINQUES_CAPABILITY_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CULTIVATOR_TECHINQUES_CAPABILITY_CAPABILITY.getStorage().readNBT(CULTIVATOR_TECHINQUES_CAPABILITY_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }
}
