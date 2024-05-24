package DaoOfModding.Cultivationcraft.Common.Capabilities.PlantGenerator;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PlantGeneratorCapability implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<IPlantGenerator> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });

    protected final IPlantGenerator backend = new PlantGenerator();
    protected final LazyOptional<IPlantGenerator> optionalData = LazyOptional.of(() -> backend);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return INSTANCE.orEmpty(cap, this.optionalData);
    }

    void invalidate() {
        this.optionalData.invalidate();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.backend.writeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.backend.readNBT(nbt);
    }

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IPlantGenerator.class);
    }
}
