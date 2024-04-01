package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import net.minecraft.resources.ResourceLocation;

public class TechniqueModifier
{
    ResourceLocation Element = null;

    public ResourceLocation getElement()
    {
        return Element;
    }

    public Boolean hasElement()
    {
        if (Element != null)
            return true;

        return false;
    }
}
