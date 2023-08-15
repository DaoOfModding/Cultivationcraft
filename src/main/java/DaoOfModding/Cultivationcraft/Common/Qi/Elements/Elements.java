package DaoOfModding.Cultivationcraft.Common.Qi.Elements;


import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;

public class Elements
{
    protected static HashMap<ResourceLocation, Element> Elements = new HashMap<>();

    public static final ResourceLocation noElement = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.elements.none");
    public static final ResourceLocation fireElement = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.elements.fire");
    public static final ResourceLocation earthElement = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.elements.earth");
    public static final ResourceLocation woodElement = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.elements.wood");
    public static final ResourceLocation windElement = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.elements.wind");
    public static final ResourceLocation waterElement = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.elements.water");

    public static final ResourceLocation iceElement = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.elements.ice");
    public static final ResourceLocation lightningElement = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.elements.lightning");

    protected static ArrayList<ResourceLocation> defaultElements = new ArrayList<>();
    protected static HashMap<ResourceKey<Level>, ArrayList<ResourceLocation>> dimensionElements = new HashMap<>();

    public static void init()
    {
        createElements();
        createMaterialElements();

        createDimensionRules();
    }

    protected static void createDimensionRules()
    {
        addDefaultDimensionRule(noElement);
        addDefaultDimensionRule(fireElement);
        addDefaultDimensionRule(earthElement);
        addDefaultDimensionRule(woodElement);
        addDefaultDimensionRule(windElement);
        addDefaultDimensionRule(waterElement);
        addDefaultDimensionRule(iceElement);
        addDefaultDimensionRule(lightningElement);

        addDimensionRule(Level.NETHER, fireElement);
    }

    public static void addDefaultDimensionRule(ResourceLocation element)
    {
        defaultElements.add(element);
    }

    public static void addDimensionRule(ResourceKey<Level> dimension, ResourceLocation element)
    {
        if (!dimensionElements.containsKey(dimension))
            dimensionElements.put(dimension, new ArrayList<ResourceLocation>());

        dimensionElements.get(dimension).add(element);
    }

    public static ArrayList<ResourceLocation> getDimensionRules(ResourceKey<Level> dimension)
    {
        if (dimensionElements.containsKey(dimension))
            return dimensionElements.get(dimension);

        return defaultElements;
    }

    protected static void createElements()
    {
        addElement(new Element(noElement, new Color(1f, 1f, 1f), 0));
        addElement(new FireElement(fireElement, new Color(1f, 0, 0), 0.2));
        addElement(new Element(earthElement, new Color(1f, 0.5f, 0), 0.75));
        addElement(new Element(woodElement, new Color(0, 0.5f, 0), 0.05));
        addElement(new Element(windElement, new Color(0, 1f, 0.25f), 0.8));
        addElement(new WaterElement(waterElement, new Color(0, 0, 1f), 0.2));

        addVariant(waterElement, new IceElement(iceElement, new Color(0, 1f, 1f), 0.5, 0.02));
        addVariant(windElement, new LightningElement(lightningElement, new Color(1f, 1f, 0), 0, 0.02));
    }

    protected static void createMaterialElements()
    {
        BlockElements.addMaterial(Material.AIR, windElement);

        BlockElements.addMaterial(Material.BUBBLE_COLUMN, waterElement);
        BlockElements.addMaterial(Material.WATER_PLANT, waterElement);
        BlockElements.addMaterial(Material.REPLACEABLE_WATER_PLANT , waterElement);
        BlockElements.addMaterial(Material.WATER , waterElement);
        BlockElements.addMaterial(Material.SPONGE , waterElement);

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
    }

    // Adds a new element of the specified resourceLocation to the Elements list
    public static void addElement(Element element)
    {
        Elements.put(element.getResourceLocation(), element);
    }

    // Adds a new variant of the specified name to the Elements list
    public static void addVariant(ResourceLocation elementLocation, ElementVariant variant)
    {
        Elements.put(variant.getResourceLocation(), variant);

        getElement(elementLocation).addVariant(variant);
    }

    public static Set<ResourceLocation> getElements()
    {
        return Elements.keySet();
    }

    // Returns the element of the supplied id
    public static Element getElement(ResourceLocation element)
    {
        return Elements.get(element);
    }
}
