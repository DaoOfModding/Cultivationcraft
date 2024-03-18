package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemHandler {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Cultivationcraft.MODID);

    public static final RegistryObject<Item> FREEZE_TEST_ITEM = ITEMS.register("freeze_test_item",
            () -> new Item(
                    new Item.Properties()
                            .tab(ModCreativeModeTab.CC_DEBUG_TAB)
            )
    );

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
