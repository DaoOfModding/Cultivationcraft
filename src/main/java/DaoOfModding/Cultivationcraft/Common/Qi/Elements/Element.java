package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import java.awt.*;
import java.util.ArrayList;

public class Element
{
    public final String name;
    public final int ID;
    public final Color color;

    private ArrayList<ElementRelationship> relationships = new ArrayList<ElementRelationship>();

    public Element (int elementID, String elementName, Color elementColor)
    {
        ID = elementID;
        name = elementName;
        color = elementColor;
    }

    // Adds an attack modifier of the specified value for the specified element
    public void setAttackModifier(int ElementID, double value)
    {
        relationships.add(new ElementRelationship(ElementID, value));
    }

    // Return the attack modifier against the specified element
    public double getAttackModifier(int ElementID)
    {
        for (ElementRelationship relationship : relationships)
            if (relationship.defendingElementID == ElementID)
                return relationship.modifier;

        // If there is no modifier for the specified element in the relationship list return a modifier of 1
        return 1;
    }

    // TODO: getIcon()
}