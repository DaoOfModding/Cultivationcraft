package DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChunkQiSourcesCapability implements ICapabilitySerializable<CompoundNBT>
{
    @CapabilityInject(IChunkQiSources.class)
    public static final Capability<IChunkQiSources> CULTIVATOR_STATS_CAPABILITY = null;
    private LazyOptional<IChunkQiSources> instance = LazyOptional.of(CULTIVATOR_STATS_CAPABILITY::getDefaultInstance);

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IChunkQiSources.class, new ChunkQiSourcesStorage(), ChunkQiSources::new);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return CULTIVATOR_STATS_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) CULTIVATOR_STATS_CAPABILITY.getStorage().writeNBT(CULTIVATOR_STATS_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CULTIVATOR_STATS_CAPABILITY.getStorage().readNBT(CULTIVATOR_STATS_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }
}
