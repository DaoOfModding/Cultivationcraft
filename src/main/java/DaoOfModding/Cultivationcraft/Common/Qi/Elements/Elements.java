package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;

public class Elements
{
    private static ArrayList<Element> Elements = new ArrayList<Element>();

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

    private static void createElements()
    {
        noElementID = addElement(new TranslationTextComponent("cultivationcraft.elements.none").getString());
        fireElementID = addElement(new TranslationTextComponent("cultivationcraft.elements.fire").getString());
        earthElementID = addElement(new TranslationTextComponent("cultivationcraft.elements.earth").getString());
        woodElementID = addElement(new TranslationTextComponent("cultivationcraft.elements.wood").getString());
        waterElementID = addElement(new TranslationTextComponent("cultivationcraft.elements.water").getString());
        iceElementID = addElement(new TranslationTextComponent("cultivationcraft.elements.ice").getString());
        lightningElementID = addElement(new TranslationTextComponent("cultivationcraft.elements.lightning").getString());
    }

    private static void createElementRelationships()
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
    public static int addElement(String name)
    {
        // As elements should NEVER be removed from the Elements list, the size before adding the element should always be the elements ID
        int id = Elements.size();
        Elements.add(new Element(id, name));

        return id;
    }

    // Returns the element of the supplied id
    public static Element getElement(int ID)
    {
        return Elements.get(ID);
    }
}
