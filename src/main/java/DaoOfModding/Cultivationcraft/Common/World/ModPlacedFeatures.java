package DaoOfModding.Cultivationcraft.Common.World;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Cultivationcraft.MODID);

    public static final RegistryObject<PlacedFeature> SPIRITUAL_PLANT_PLACED = PLACED_FEATURES.register("spiritual_plant_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.SPIRITUAL_PLANT.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(16),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));

    public static void init(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}
