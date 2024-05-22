package DaoOfModding.Cultivationcraft.Common.Plants;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cultivationcraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldGenEventHandler {

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        // Check if the world is being loaded
        if (event.getLevel().isClientSide()) {
            System.out.println("World loaded on client side :\n" +
                    "World loaded on client side :\n" +
                    "World loaded on client side :\n");

            PlantGenerator.generatePlant();
        }
    }

    @SubscribeEvent
    public static void onWorldCreateSpawnPosition(LevelEvent.CreateSpawnPosition event) {
        // Check if the world is being loaded for the first time (new world)
        long seed = event.getLevel().getServer().getWorldData().worldGenSettings().seed();
        System.out.println("World Chose spawn position ! seed : " + seed + "\n" +
                "World Chose spawn position ! seed : " + seed + "\n" +
                "World Chose spawn position ! seed : " + seed + "\n");
        PlantGenerator.generatePlant();
    }
}
