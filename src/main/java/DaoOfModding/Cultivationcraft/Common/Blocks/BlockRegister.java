package DaoOfModding.Cultivationcraft.Common.Blocks;

import DaoOfModding.Cultivationcraft.Common.Items.ModCreativeModeTab;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegister {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Cultivationcraft.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Cultivationcraft.MODID);
/*
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Cultivationcraft.MODID);
*/

    public static final RegistryObject<FrozenBlock> FROZEN_BLOCK = registerBlock("frozen_block",
            () -> new FrozenBlock(BlockBehaviour.Properties.copy(Blocks.ICE).strength(-1.0F, 3600000.0F).noOcclusion()),
            ModCreativeModeTab.CC_DEBUG_TAB);
    public static RegistryObject<BlockEntityType<FrozenBlockEntity>> FROZEN_BLOCK_ENTITY = BLOCK_ENTITIES.register("frozen_block_entity",
            () -> BlockEntityType.Builder.of(
                    FrozenBlockEntity::new,
                    FROZEN_BLOCK.get()
            ).build(null)
    );

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return Register.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerRenderers(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(FROZEN_BLOCK.get(), RenderType.translucent());
        }
    }
}
