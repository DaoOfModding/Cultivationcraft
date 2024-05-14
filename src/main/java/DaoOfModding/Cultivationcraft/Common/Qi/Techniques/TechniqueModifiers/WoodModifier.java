package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class WoodModifier extends TechniqueModifier
{
    public WoodModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.wood");
        CATEGORY = ELEMENTAL_CATEGORY;

        Element = Elements.woodElement;

        unlockQuest = new Quest(Quest.QI_SOURCE_MEDITATION, 1, Element);
        stabiliseQuest = new Quest(Quest.DAMAGE_DEALT, 1000, Element);
    }
}
