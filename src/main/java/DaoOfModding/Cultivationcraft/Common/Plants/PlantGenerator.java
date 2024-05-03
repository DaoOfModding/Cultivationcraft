package DaoOfModding.Cultivationcraft.Common.Plants;

import DaoOfModding.Cultivationcraft.Common.Blocks.custom.SpiritualPlant;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class PlantGenerator {
    public SpiritualPlant plant;

    public void generatePlant() {
        try (Stream<Path> files = Files.list(Path.of("src/main/resources/assets/cultivationcraft/textures/blocks"))) {
            long count = files.count();
            System.out.println("Number of files: " + count);
            System.out.println("Files: " + files.toString());
        } catch (Exception e) {
            System.out.println("Error reading files");
            e.printStackTrace();
        }
        // Generate plant
    }

    @SubscribeEvent
    public void onWorldLoad(net.minecraftforge.event.level.ChunkEvent event) {
        // World load event
    }


}
