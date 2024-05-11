package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class LightningModifier extends TechniqueModifier
{
    public LightningModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.lightning");
        CATEGORY = new ResourceLocation(Cultivationcraft.MODID, "concept.category.elemental");

        Element = Elements.lightningElement;
    }
}
