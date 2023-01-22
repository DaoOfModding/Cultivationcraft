package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class ElementVariant extends Element
{
    // The chance for the base element to mutate into this one
    double mutateChance;

    public ElementVariant(ResourceLocation Element, Color elementColor, double newDensity, double chance)
    {
        super(Element, elementColor, newDensity);

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
