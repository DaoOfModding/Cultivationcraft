package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Cultivationcraft.MODID);

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }

    //region Advancement Items
    public static final RegistryObject<Item> EXTERNAL_PATH_ADV = ITEMS.register("external_path_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLYING_SWORD_ADV = ITEMS.register("flying_sword_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRST_BREAKTHROUGH_ADV = ITEMS.register("first_breakthrough_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> QI_CONDENSING_ADV = ITEMS.register("qi_condensing_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CORE_FORMING_ADV = ITEMS.register("core_forming_advancement", () -> new Item(new Item.Properties()));
    //region Elemental Breakthroughs Advancements
    public static final RegistryObject<Item> FIRE_FORMING_ADV = ITEMS.register("fire_forming_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EARTH_FORMING_ADV = ITEMS.register("earth_forming_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WIND_FORMING_ADV = ITEMS.register("wind_forming_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOOD_FORMING_ADV = ITEMS.register("wood_forming_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATER_FORMING_ADV = ITEMS.register("water_forming_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ICE_FORMING_ADV = ITEMS.register("ice_forming_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIGHTNING_FORMING_ADV = ITEMS.register("lightning_forming_advancement", () -> new Item(new Item.Properties()));
    //endregion
    public static final RegistryObject<Item> INTERNAL_PATH_ADV = ITEMS.register("internal_path_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EVOLVED_LIMB_ADV = ITEMS.register("evolved_limb_advancement", () -> new Item(new Item.Properties()));

    //endregion
}
