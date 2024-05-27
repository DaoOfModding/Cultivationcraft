package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;

public class IceModifier extends TechniqueModifier
{
    public IceModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.ice");
        CATEGORY = ELEMENTAL_CATEGORY;

        Element = Elements.iceElement;

        unlockQuest = new Quest(Quest.QI_SOURCE_MEDITATION, 1, Element.toString());
        stabiliseQuest = new Quest(Quest.DAMAGE_DEALT, 1000, Element.toString());

        coreTexture = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/cores/ice.png"));
    }
}
