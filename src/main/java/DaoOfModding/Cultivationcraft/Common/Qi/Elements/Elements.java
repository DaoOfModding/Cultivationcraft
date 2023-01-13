package DaoOfModding.Cultivationcraft.Common.Qi.Elements;


import java.awt.*;
import java.util.ArrayList;
import net.minecraft.network.chat.Component;

public class Elements
{
    protected static ArrayList<Element> Elements = new ArrayList<Element>();

    public static int noElementID;
    public static int fireElementID;
    public static int earthElementID;
    public static int woodElementID;
    public static int waterElementID;
    public static int iceElementID;
    public static int lightningElementID;

    public static void init()
    {
        createElements();
        createElementRelationships();
    }

    protected static void createElements()
    {
        noElementID = addElement(Component.translatable("cultivationcraft.elements.none").getString(), new Color(1f, 1f, 1f));
        fireElementID = addElement(Component.translatable("cultivationcraft.elements.fire").getString(), new Color(1f, 0, 0));
        earthElementID = addElement(Component.translatable("cultivationcraft.elements.earth").getString(), new Color(1f, 0.5f, 0));
        woodElementID = addElement(Component.translatable("cultivationcraft.elements.wood").getString(), new Color(0, 1f, 0));
        waterElementID = addElement(Component.translatable("cultivationcraft.elements.water").getString(), new Color(0, 0, 1f));
        iceElementID = addElement(Component.translatable("cultivationcraft.elements.ice").getString(), new Color(0, 1f, 1f));
        lightningElementID = addElement(Component.translatable("cultivationcraft.elements.lightning").getString(), new Color(1f, 1f, 0));
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

    // Adds a new elements of the specified name to the Elements list
    // Returns the elements ID
    public static int addElement(String name, Color color)
    {
        // As elements should NEVER be removed from the Elements list, the size before adding the element should always be the elements ID
        int id = Elements.size();
        Elements.add(new Element(id, name, color));

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
