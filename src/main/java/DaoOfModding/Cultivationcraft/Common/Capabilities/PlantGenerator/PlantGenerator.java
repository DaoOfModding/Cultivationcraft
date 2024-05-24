package DaoOfModding.Cultivationcraft.Common.Capabilities.PlantGenerator;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.custom.SpiritualPlant;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.World.PlantGeneration.PlantNames;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Random;

public class PlantGenerator implements IPlantGenerator {
    SpiritualPlant plant;
    ResourceLocation dimension;


    @Override
    public ResourceLocation getDimension() {
        return dimension;
    }

    @Override
    public void setDimension(ResourceLocation dim) {
        this.dimension = dim;
        System.out.println("Dimension set to: " + dim); // Debug logging
    }

    @Override
    public void setSpiritualPlant(SpiritualPlant plant) {
        this.plant = plant;
        System.out.println("SpiritualPlant set to: " + plant); // Debug logging
    }

    @Override
    public SpiritualPlant getSpiritualPlant() {
        return plant;
    }

    @Override
    public void generatePlant(Level level) {
        if (BlockRegister.SPIRITUAL_PLANT.get() == null) {
            System.err.println("BlockRegister.SPIRITUAL_PLANT is null!");
            return;
        }
        setSpiritualPlant(BlockRegister.SPIRITUAL_PLANT.get());

        if (PlantNames.getPlantNames().size() > 0) {
            randomSelectPlantName(plant);
        } else {
            System.err.println("No plant names available to select from.");
        }

        if (Elements.getElements().size() > 0) {
            randomSelectElement(plant);
        } else {
            System.err.println("No elements available to select from.");
        }
    }

    public static void randomSelectElement(SpiritualPlant plant) {
        int size = Elements.getElements().size();
        System.out.println("Element size " + size);
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for (ResourceLocation element : Elements.getElements()) {
            if (i == item) {
                plant.setElement(element);
                System.out.println("Selected plant name: " + element); // Debug logging
                break;
            }
            i++;
        }
    }

    public static void randomSelectPlantName(SpiritualPlant plant) {
        int size = PlantNames.getPlantNames().size();
        System.out.println("PlantName size " + size);
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for (ResourceLocation plantName : PlantNames.getPlantNames()) {
            if (i == item) {
                plant.setPlantName(plantName);
                System.out.println("Selected plant name: " + plantName); // Debug logging
                break;
            }
            i++;
        }
    }

    @Override
    public CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (dimension != null) {
            nbt.putString("dimension", dimension.toString());
        } else {
            System.err.println("Dimension is null!"); // Debug logging
        }
        if (plant != null) {
            nbt.put("plant", plant.SerializeNBT());
        } else {
            System.err.println("Plant is null!"); // Debug logging
        }
        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if (nbt.contains("dimension")) {
            dimension = new ResourceLocation(nbt.getString("dimension"));
            System.out.println("Deserialized dimension: " + dimension); // Debug logging
        } else {
            System.err.println("Dimension not found in NBT!"); // Debug logging
        }

        if (nbt.contains("plant")) {
            if (plant == null) {
                plant = BlockRegister.SPIRITUAL_PLANT.get();
            }
            plant.DeserializeNBT(nbt.getCompound("plant"));
            System.out.println("Deserialized plant: " + plant); // Debug logging
        } else {
            System.err.println("Plant not found in NBT!"); // Debug logging
        }
    }

    public static IPlantGenerator getPlantGenerator(Level level) {
        IPlantGenerator plantGenerator = level.getCapability(PlantGeneratorCapability.INSTANCE).orElse(null);
        if (plantGenerator == null) {
            throw new RuntimeException("Plant Generator Capability not found!");
        }
        System.out.println("PlantGenerator fetched: " + plantGenerator); // Debug logging
        return plantGenerator;
    }


}
