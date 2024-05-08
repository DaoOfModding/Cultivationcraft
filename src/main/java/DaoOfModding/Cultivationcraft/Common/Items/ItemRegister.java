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

    // Advancement Items
    public static final RegistryObject<Item> EXTERNAL_PATH_ADV = ITEMS.register("external_path_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLYING_SWORD_ADV = ITEMS.register("flying_sword_advancement", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INTERNAL_PATH_ADV = ITEMS.register("internal_path_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EVOLVED_LIMB_ADV = ITEMS.register("evolved_limb_advancement", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> QI_SIGHT = ITEMS.register("qi_sight_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEDITATE = ITEMS.register("meditate_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TECH_MASTERY = ITEMS.register("tech_mastery_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BREAKTHROUGH = ITEMS.register("breakthrough_advancement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TRIBULATION = ITEMS.register("tribulation_advancement", () -> new Item(new Item.Properties()));
}
