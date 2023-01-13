package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CultivatorStatsCapability implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    public static final Capability<ICultivatorStats> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    protected final ICultivatorStats backend = new CultivatorStats();
    protected final LazyOptional<ICultivatorStats> optionalData = LazyOptional.of(() -> backend);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return INSTANCE.orEmpty(cap, this.optionalData);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.backend.writeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.backend.readNBT(nbt);
    }

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(ICultivatorStats.class);
    }
}
