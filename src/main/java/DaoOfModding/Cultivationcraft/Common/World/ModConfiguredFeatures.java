package DaoOfModding.Cultivationcraft.Common.World;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.custom.SpiritualPlant;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.ThreadLocalRandom;

public class ModConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Cultivationcraft.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> SPIRITUAL_PLANT = CONFIGURED_FEATURES.register("spiritual_plant",
            () -> new ConfiguredFeature<>(Feature.FLOWER,
                    new RandomPatchConfiguration(32, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                            new SimpleBlockConfiguration(BlockStateProvider.simple(
                                    BlockRegister.SPIRITUAL_PLANT.get().defaultBlockState()
                                            .setValue(SpiritualPlant.QI, ThreadLocalRandom.current().nextInt(1, 1000))
                            ))))));


    public static void init(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}
