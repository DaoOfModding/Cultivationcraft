package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class QiModifier extends TechniqueModifier
{
    public QiModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.pure");
        CATEGORY = new ResourceLocation(Cultivationcraft.MODID, "concept.category.elemental");

    }
}

