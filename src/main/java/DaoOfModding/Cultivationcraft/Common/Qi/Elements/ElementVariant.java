package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import java.awt.*;

public class ElementVariant extends Element
{
    // The chance for the base element to mutate into this one
    double mutateChance;

    public ElementVariant(int elementID, String elementName, Color elementColor, double chance)
    {
        super(elementID, elementName, elementColor, 0);

        mutateChance = chance;
    }

    // Attempt to mutate into this element
    public boolean tryMutate()
    {
        if (Math.random() > mutateChance)
            return false;

        return true;
    }
}
