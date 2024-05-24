package DaoOfModding.Cultivationcraft.Common.World.PlantGeneration;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PlantNames {

    protected static HashSet<ResourceLocation> PlantNames = new HashSet<>();

    public static final ResourceLocation SPIRITUAL_GRASS = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.spiritual_grass");
    public static final ResourceLocation SPIRITUAL_FLOWER = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.spiritual_flower");
    public static final ResourceLocation SPIRITUAL_ROOT = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.spiritual_root");
    public static final ResourceLocation JADE_LOTUS = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.jade_lotus");
    public static final ResourceLocation CELESTIAL_ORCHID = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.celestial_orchid");
    public static final ResourceLocation AZURE_DRAGON_VINE = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.azure_dragon_vine");
    public static final ResourceLocation PHOENIX_FEATHER_HERB = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.phoenix_feather_herb");
    public static final ResourceLocation ETHEREAL_MIST_FLOWER = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.ethereal_mist_flower");
    public static final ResourceLocation MOONSHADOW_LILY = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.moonshadow_lily");
    public static final ResourceLocation GOLDEN_SUNFLOWER = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.golden_sunflower");
    public static final ResourceLocation SILVERLEAF_SAGE = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.silverleaf_sage");
    public static final ResourceLocation LIGHTDROP_BLOSSOM = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.lightdrop_blossom");
    public static final ResourceLocation CRIMSOM_BLOODROOT = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.crimson_bloodroot");
    public static final ResourceLocation SERENE_WATER_LOTUS = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.serene_water_lotus");
    public static final ResourceLocation STORMCLOUD_IVY = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.stormcloud_ivy");
    public static final ResourceLocation VERDANT_HEARTWOOD = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.verdant_heartwood");
    public static final ResourceLocation MYSTIC_MOON_ORCHID = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.mystic_moon_orchid");
    public static final ResourceLocation DRAGON_BREATH_FERN = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.dragon_breath_fern");

    public static final ResourceLocation anyName = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.plantnames.any");

    protected static ArrayList<ResourceLocation> defaultPlantNames = new ArrayList<>();
    protected static HashMap<ResourceKey<Level>, ArrayList<ResourceLocation>> dimensionPlantNames = new HashMap<>();

    public static void init() {
        createPlantNames();
        //createMaterialPlantNames();

        createDimensionRules();
    }

    protected static void createDimensionRules() {
        addDefaultDimensionRule(SPIRITUAL_GRASS);
        addDefaultDimensionRule(SPIRITUAL_FLOWER);
        addDefaultDimensionRule(SPIRITUAL_ROOT);
        addDefaultDimensionRule(JADE_LOTUS);
        addDefaultDimensionRule(CELESTIAL_ORCHID);
        addDefaultDimensionRule(AZURE_DRAGON_VINE);
        addDefaultDimensionRule(PHOENIX_FEATHER_HERB);
        addDefaultDimensionRule(ETHEREAL_MIST_FLOWER);
        addDefaultDimensionRule(MOONSHADOW_LILY);
        addDefaultDimensionRule(GOLDEN_SUNFLOWER);
        addDefaultDimensionRule(SILVERLEAF_SAGE);
        addDefaultDimensionRule(LIGHTDROP_BLOSSOM);
        addDefaultDimensionRule(CRIMSOM_BLOODROOT);
        addDefaultDimensionRule(SERENE_WATER_LOTUS);
        addDefaultDimensionRule(STORMCLOUD_IVY);
        addDefaultDimensionRule(VERDANT_HEARTWOOD);
        addDefaultDimensionRule(MYSTIC_MOON_ORCHID);
        addDefaultDimensionRule(DRAGON_BREATH_FERN);

        //addDimensionRule(Level.NETHER, CELESTIAL_ORCHID);
    }

    public static void addDefaultDimensionRule(ResourceLocation plantName) {
        defaultPlantNames.add(plantName);
    }

/*    public static void addDimensionRule(ResourceKey<Level> dimension, ResourceLocation plantName) {
        if (!dimensionPlantNames.containsKey(dimension))
            dimensionPlantNames.put(dimension, new ArrayList<ResourceLocation>());

        dimensionPlantNames.get(dimension).add(plantName);
    }

    public static ArrayList<ResourceLocation> getDimensionRules(ResourceKey<Level> dimension) {
        if (dimensionPlantNames.containsKey(dimension))
            return dimensionPlantNames.get(dimension);

        return defaultPlantNames;
    }*/

    protected static void createPlantNames() {
        addPlantName(SPIRITUAL_GRASS);
        addPlantName(SPIRITUAL_FLOWER);
        addPlantName(SPIRITUAL_ROOT);
        addPlantName(JADE_LOTUS);
        addPlantName(CELESTIAL_ORCHID);
        addPlantName(AZURE_DRAGON_VINE);
        addPlantName(PHOENIX_FEATHER_HERB);
        addPlantName(ETHEREAL_MIST_FLOWER);
        addPlantName(MOONSHADOW_LILY);
        addPlantName(GOLDEN_SUNFLOWER);
        addPlantName(SILVERLEAF_SAGE);
        addPlantName(LIGHTDROP_BLOSSOM);
        addPlantName(CRIMSOM_BLOODROOT);
        addPlantName(SERENE_WATER_LOTUS);
        addPlantName(STORMCLOUD_IVY);
        addPlantName(VERDANT_HEARTWOOD);
        addPlantName(MYSTIC_MOON_ORCHID);
        addPlantName(DRAGON_BREATH_FERN);
    }

   /* protected static void createMaterialPlantNames() {
        BlockElements.addMaterial(Material.AIR, windElement);

        BlockElements.addMaterial(Material.BUBBLE_COLUMN, waterElement);
        BlockElements.addMaterial(Material.WATER_PLANT, waterElement);
        BlockElements.addMaterial(Material.REPLACEABLE_WATER_PLANT, waterElement);
        BlockElements.addMaterial(Material.WATER, waterElement);
        BlockElements.addMaterial(Material.SPONGE, waterElement);

        BlockElements.addMaterial(Material.ICE, iceElement);
        BlockElements.addMaterial(Material.ICE_SOLID, iceElement);
        BlockElements.addMaterial(Material.TOP_SNOW, iceElement);
        BlockElements.addMaterial(Material.POWDER_SNOW, iceElement);
        BlockElements.addMaterial(Material.SNOW, iceElement);

        BlockElements.addMaterial(Material.BAMBOO, woodElement);
        BlockElements.addMaterial(Material.BAMBOO_SAPLING, woodElement);
        BlockElements.addMaterial(Material.PLANT, woodElement);
        BlockElements.addMaterial(Material.REPLACEABLE_PLANT, woodElement);
        BlockElements.addMaterial(Material.REPLACEABLE_FIREPROOF_PLANT, woodElement);
        BlockElements.addMaterial(Material.CACTUS, woodElement);
        BlockElements.addMaterial(Material.WOOD, woodElement);
        BlockElements.addMaterial(Material.NETHER_WOOD, woodElement);
        BlockElements.addMaterial(Material.LEAVES, woodElement);
        BlockElements.addMaterial(Material.MOSS, woodElement);
        BlockElements.addMaterial(Material.VEGETABLE, woodElement);

        BlockElements.addMaterial(Material.CLAY, earthElement);
        BlockElements.addMaterial(Material.DIRT, earthElement);
        BlockElements.addMaterial(Material.GRASS, earthElement);
        BlockElements.addMaterial(Material.SAND, earthElement);
        BlockElements.addMaterial(Material.STONE, earthElement);
        BlockElements.addMaterial(Material.METAL, earthElement);
        BlockElements.addMaterial(Material.HEAVY_METAL, earthElement);

        BlockElements.addMaterial(Material.EXPLOSIVE, fireElement);
        BlockElements.addMaterial(Material.FIRE, fireElement);
        BlockElements.addMaterial(Material.LAVA, fireElement);
    }*/

    // Adds a new element of the specified resourceLocation to the PlantName list
    public static void addPlantName(ResourceLocation plantName) {
        PlantNames.add(plantName);
    }

/*    // Adds a new variant of the specified name to the PlantName list
    public static void addVariant(ResourceLocation elementLocation, ElementVariant variant) {
        PlantNames.put(variant.getResourceLocation(), variant);

        getElement(elementLocation).addVariant(variant);
    }*/

    public static Set<ResourceLocation> getPlantNames() {
        return PlantNames;
    }
}
