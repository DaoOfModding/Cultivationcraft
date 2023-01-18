package DaoOfModding.Cultivationcraft.Common.Qi.Elements;


import java.awt.*;
import java.util.ArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Material;

public class Elements
{
    protected static ArrayList<Element> Elements = new ArrayList<Element>();

    public static int noElementID;
    public static int fireElementID;
    public static int earthElementID;
    public static int woodElementID;
    public static int windElementID;
    public static int waterElementID;
    public static int iceElementID;

    public static int lightningElementID;

    public static void init()
    {
        createElements();
        createMaterialElements();

        // TODO: Is this even necessary?
        createElementRelationships();
    }

    protected static void createElements()
    {
        noElementID = addElement(Component.translatable("cultivationcraft.elements.none").getString(), new Color(1f, 1f, 1f));
        fireElementID = addElement(Component.translatable("cultivationcraft.elements.fire").getString(), new Color(1f, 0, 0));
        earthElementID = addElement(Component.translatable("cultivationcraft.elements.earth").getString(), new Color(1f, 0.5f, 0));
        woodElementID = addElement(Component.translatable("cultivationcraft.elements.wood").getString(), new Color(0, 0.5f, 0));
        windElementID = addElement(Component.translatable("cultivationcraft.elements.wood").getString(), new Color(0, 1f, 0.5f));
        waterElementID = addElement(Component.translatable("cultivationcraft.elements.water").getString(), new Color(0, 0, 1f));
        iceElementID = addElement(Component.translatable("cultivationcraft.elements.ice").getString(), new Color(0, 1f, 1f));

        lightningElementID = addVariant(windElementID, Component.translatable("cultivationcraft.elements.lightning").getString(), new Color(1f, 1f, 0), 0.02);
    }

    protected static void createMaterialElements()
    {
        BlockElements.addMaterial(Material.AIR, windElementID);

        BlockElements.addMaterial(Material.BUBBLE_COLUMN, waterElementID);
        BlockElements.addMaterial(Material.WATER_PLANT, waterElementID);
        BlockElements.addMaterial(Material.REPLACEABLE_WATER_PLANT , waterElementID);
        BlockElements.addMaterial(Material.WATER , waterElementID);
        BlockElements.addMaterial(Material.SPONGE , waterElementID);

        BlockElements.addMaterial(Material.ICE, iceElementID);
        BlockElements.addMaterial(Material.ICE_SOLID, iceElementID);
        BlockElements.addMaterial(Material.TOP_SNOW, iceElementID);
        BlockElements.addMaterial(Material.POWDER_SNOW, iceElementID);
        BlockElements.addMaterial(Material.SNOW, iceElementID);

        BlockElements.addMaterial(Material.BAMBOO, woodElementID);
        BlockElements.addMaterial(Material.BAMBOO_SAPLING, woodElementID);
        BlockElements.addMaterial(Material.PLANT, woodElementID);
        BlockElements.addMaterial(Material.REPLACEABLE_PLANT, woodElementID);
        BlockElements.addMaterial(Material.REPLACEABLE_FIREPROOF_PLANT, woodElementID);
        BlockElements.addMaterial(Material.CACTUS, woodElementID);
        BlockElements.addMaterial(Material.WOOD, woodElementID);
        BlockElements.addMaterial(Material.NETHER_WOOD, woodElementID);
        BlockElements.addMaterial(Material.LEAVES, woodElementID);
        BlockElements.addMaterial(Material.MOSS, woodElementID);
        BlockElements.addMaterial(Material.VEGETABLE, woodElementID);

        BlockElements.addMaterial(Material.CLAY, earthElementID);
        BlockElements.addMaterial(Material.DIRT, earthElementID);
        BlockElements.addMaterial(Material.GRASS, earthElementID);
        BlockElements.addMaterial(Material.SAND, earthElementID);
        BlockElements.addMaterial(Material.STONE, earthElementID);
        BlockElements.addMaterial(Material.METAL, earthElementID);
        BlockElements.addMaterial(Material.HEAVY_METAL, earthElementID);

        BlockElements.addMaterial(Material.EXPLOSIVE, fireElementID);
        BlockElements.addMaterial(Material.FIRE, fireElementID);
        BlockElements.addMaterial(Material.LAVA, fireElementID);
    }

    protected static void createElementRelationships()
    {
        // Temp modifiers for now, may look at later
        getElement(fireElementID).setAttackModifier(waterElementID, 0.5);
        getElement(fireElementID).setAttackModifier(earthElementID, 0.5);
        getElement(fireElementID).setAttackModifier(iceElementID, 2);
        getElement(fireElementID).setAttackModifier(woodElementID, 2);

        getElement(earthElementID).setAttackModifier(woodElementID, 0.5);
        getElement(earthElementID).setAttackModifier(fireElementID, 2);

        getElement(woodElementID).setAttackModifier(fireElementID, 0.5);
        getElement(woodElementID).setAttackModifier(iceElementID, 0.5);
        getElement(woodElementID).setAttackModifier(waterElementID, 2);
        getElement(woodElementID).setAttackModifier(earthElementID, 2);

        getElement(waterElementID).setAttackModifier(woodElementID, 0.5);
        getElement(waterElementID).setAttackModifier(fireElementID, 2);

        getElement(iceElementID).setAttackModifier(fireElementID, 0.5);
        getElement(iceElementID).setAttackModifier(woodElementID, 2);


        // Lightning is strong against everything, but is a lot harder to cultivate and has much less QI available to it
        getElement(noElementID).setAttackModifier(lightningElementID, 0.75);
        getElement(fireElementID).setAttackModifier(lightningElementID, 0.75);
        getElement(earthElementID).setAttackModifier(lightningElementID, 0.75);
        getElement(woodElementID).setAttackModifier(lightningElementID, 0.75);
        getElement(waterElementID).setAttackModifier(lightningElementID, 0.75);
        getElement(iceElementID).setAttackModifier(lightningElementID, 0.75);

        getElement(lightningElementID).setAttackModifier(noElementID, 1.5);
        getElement(lightningElementID).setAttackModifier(fireElementID, 1.5);
        getElement(lightningElementID).setAttackModifier(earthElementID, 1.5);
        getElement(lightningElementID).setAttackModifier(woodElementID, 1.5);
        getElement(lightningElementID).setAttackModifier(waterElementID, 1.5);
        getElement(lightningElementID).setAttackModifier(iceElementID, 1.5);
    }

    // Adds a new element of the specified name to the Elements list
    // Returns the elements ID
    public static int addElement(String name, Color color)
    {
        // As elements should NEVER be removed from the Elements list, the size before adding the element should always be the elements ID
        int id = Elements.size();
        Elements.add(new Element(id, name, color));

        return id;
    }

    // Adds a new variant of the specified name to the Elements list
    // Returns the elements ID
    public static int addVariant(int ElementID, String name, Color color, double chance)
    {
        // As elements should NEVER be removed from the Elements list, the size before adding the element should always be the elements ID
        int id = Elements.size();
        ElementVariant variant = new ElementVariant(id, name, color, chance);

        Elements.add(variant);
        getElement(ElementID).addVariant(variant);

        return id;
    }

    public static ArrayList<Element> getElements()
    {
        return Elements;
    }

    // Returns the element of the supplied id
    public static Element getElement(int ID)
    {
        return Elements.get(ID);
    }
}
