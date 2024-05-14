package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class FireModifier extends TechniqueModifier
{
    public FireModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.fire");
        CATEGORY = ELEMENTAL_CATEGORY;

        Element = Elements.fireElement;
        coreTexture = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/orbfillingfire.png"), 32);

        unlockQuest = new Quest(Quest.QI_SOURCE_MEDITATION, 1, Element);
        stabiliseQuest = new Quest(Quest.DAMAGE_DEALT, 1000, Element);
    }
}
