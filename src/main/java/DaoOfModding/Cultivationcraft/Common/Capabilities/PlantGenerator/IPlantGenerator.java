package DaoOfModding.Cultivationcraft.Common.Capabilities.PlantGenerator;

import DaoOfModding.Cultivationcraft.Common.Blocks.custom.SpiritualPlant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public interface IPlantGenerator {
    public ResourceLocation getDimension();

    public void setDimension(ResourceLocation dim);

    void setSpiritualPlant(SpiritualPlant plant);

    SpiritualPlant getSpiritualPlant();

    void generatePlant(Level level);

    CompoundTag writeNBT();

    void readNBT(CompoundTag nbt);
}
