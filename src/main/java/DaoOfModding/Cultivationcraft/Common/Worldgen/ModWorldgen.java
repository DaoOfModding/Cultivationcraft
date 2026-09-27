package DaoOfModding.Cultivationcraft.Common.Worldgen;

import java.util.List;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModWorldgen {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Cultivationcraft.MODID);

    public static final DeferredRegister<PlacedFeature> PLACED =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Cultivationcraft.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> CF_PROC_PLANT_PATCH =
            CONFIGURED.register("procedural_plant_patch",
                    () -> new ConfiguredFeature<>(Register.PROCEDURAL_PLANT_PATCH.get(),
                            NoneFeatureConfiguration.INSTANCE));

    public static final RegistryObject<PlacedFeature> PF_PROC_PLANT_PATCH =
            PLACED.register("procedural_plant_patch",
                    () -> new PlacedFeature(
                            Holder.hackyErase(CF_PROC_PLANT_PATCH.getHolder().orElseThrow()),
                            List.of(
                                RarityFilter.onAverageOnceEvery(8), // ~1/8 chunks
                                InSquarePlacement.spread(),         // spread across the chunk
                                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                                BiomeFilter.biome()
                            )
                    ));

    // Register bundled worldgen as built-ins, like the plant patch. In 1.19.2,
    // loading the same entries from JSON makes their registry lifecycle experimental.
    // Keep these IDs stable for existing saves and datapack overrides.
    public static final RegistryObject<ConfiguredFeature<?, ?>> CF_JADE_ORE =
            CONFIGURED.register("jade_ore", () -> new ConfiguredFeature<>(Register.JADE_ORE.get(),
                    NoneFeatureConfiguration.INSTANCE));
    public static final RegistryObject<ConfiguredFeature<?, ?>> CF_SPIRIT_STONE_SMALL = spiritStone("spirit_stone_ore_small", 4, .5f);
    public static final RegistryObject<ConfiguredFeature<?, ?>> CF_SPIRIT_STONE_BURIED = spiritStone("spirit_stone_ore_buried", 8, 1f);
    public static final RegistryObject<ConfiguredFeature<?, ?>> CF_SPIRIT_STONE_LARGE = spiritStone("spirit_stone_ore_large", 12, .7f);

    // Iron's underground distributions; the jade feature only replaces deepslate below Y=0.
    public static final RegistryObject<PlacedFeature> PF_JADE_ORE = orePlacement("jade_ore", CF_JADE_ORE,
            CountPlacement.of(10), HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72)));
    public static final RegistryObject<PlacedFeature> PF_JADE_ORE_MIDDLE = orePlacement("jade_ore_middle", CF_JADE_ORE,
            CountPlacement.of(10), HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)));
    // Match vanilla diamond frequency, vein sizes, depth and air-exposure settings.
    public static final RegistryObject<PlacedFeature> PF_SPIRIT_STONE_SMALL = orePlacement("spirit_stone_ore_small", CF_SPIRIT_STONE_SMALL,
            CountPlacement.of(7), diamondHeight());
    public static final RegistryObject<PlacedFeature> PF_SPIRIT_STONE_BURIED = orePlacement("spirit_stone_ore_buried", CF_SPIRIT_STONE_BURIED,
            CountPlacement.of(4), diamondHeight());
    public static final RegistryObject<PlacedFeature> PF_SPIRIT_STONE_LARGE = orePlacement("spirit_stone_ore_large", CF_SPIRIT_STONE_LARGE,
            RarityFilter.onAverageOnceEvery(9), diamondHeight());

    private static RegistryObject<ConfiguredFeature<?, ?>> spiritStone(String id, int size, float discardOnAirExposure) {
        return CONFIGURED.register(id, () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
                List.of(OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,
                        BlockRegister.SPIRIT_STONE_ORE.get().defaultBlockState())), size, discardOnAirExposure)));
    }

    private static HeightRangePlacement diamondHeight() {
        return HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80));
    }

    private static RegistryObject<PlacedFeature> orePlacement(String id, RegistryObject<ConfiguredFeature<?, ?>> feature,
                                                               PlacementModifier frequency, HeightRangePlacement height) {
        return PLACED.register(id, () -> new PlacedFeature(Holder.hackyErase(feature.getHolder().orElseThrow()),
                List.of(frequency, InSquarePlacement.spread(), height, BiomeFilter.biome())));
    }

    public static void init() {
        CONFIGURED.register(FMLJavaModLoadingContext.get().getModEventBus());
        PLACED.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
