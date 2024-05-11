package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class WaterModifier extends TechniqueModifier
{
    public WaterModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.water");
        CATEGORY = new ResourceLocation(Cultivationcraft.MODID, "concept.category.elemental");

        Element = Elements.waterElement;
    }
}
