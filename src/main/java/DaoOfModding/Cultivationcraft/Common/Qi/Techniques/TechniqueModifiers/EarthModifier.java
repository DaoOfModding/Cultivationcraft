package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class EarthModifier extends TechniqueModifier
{
    public EarthModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.earth");
        CATEGORY = new ResourceLocation(Cultivationcraft.MODID, "concept.category.elemental");

        Element = Elements.earthElement;

        unlockQuest = new Quest(Quest.QI_SOURCE_MEDITATION, 1, Elements.earthElement);
    }
}
