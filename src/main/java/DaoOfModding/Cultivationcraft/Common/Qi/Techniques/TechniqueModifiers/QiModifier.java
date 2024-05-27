package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class QiModifier extends TechniqueModifier
{
    public static final ResourceLocation QI_ID = new ResourceLocation(Cultivationcraft.MODID, "concept.pure");

    public QiModifier()
    {
        ID = QI_ID;
        CATEGORY = ELEMENTAL_CATEGORY;

        stabiliseQuest = new Quest(Quest.DAMAGE_DEALT, 1000, Elements.noElement.toString());
        unlockQuest = new Quest(Quest.QI_SOURCE_MEDITATION, 1, Elements.noElement.toString());
    }
}

