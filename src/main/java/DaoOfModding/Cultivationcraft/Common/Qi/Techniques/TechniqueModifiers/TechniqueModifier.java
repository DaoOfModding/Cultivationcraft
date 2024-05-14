package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TechniqueModifier
{
    public static final ResourceLocation ELEMENTAL_CATEGORY = new ResourceLocation(Cultivationcraft.MODID, "concept.category.elemental");
    public static final ResourceLocation MODIFIER_CATEGORY = new ResourceLocation(Cultivationcraft.MODID, "concept.category.modifier");

    public ResourceLocation ID;
    public ResourceLocation CATEGORY;

    animatedTexture coreTexture = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/cores/blank.png"));
    ResourceLocation Element = null;
    float power = 1;
    boolean allowSameCategory = false;
    Quest unlockQuest;
    Quest stabiliseQuest;

    public TechniqueModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.example");
        CATEGORY = new ResourceLocation(Cultivationcraft.MODID, "concept.category.example");
    }

    public ResourceLocation getElement()
    {
        return Element;
    }

    public animatedTexture getCoreTexture()
    {
        return coreTexture;
    }

    public Boolean hasElement()
    {
        if (getElement() != null)
            return true;

        return false;
    }

    public void tick(Player owner, Vec3 position, ResourceLocation element)
    {

    }

    public Quest getUnlockQuest()
    {
        return unlockQuest;
    }

    public Quest getStabaliseQuest()
    {
        return stabiliseQuest;
    }

    public boolean canUse(Player player)
    {
        // This modifier cannot be used if allowSameCategory is false and a technique of the same category is already active
        if (!allowSameCategory)
            for (TechniqueModifier modifier : CultivatorStats.getCultivatorStats(player).getCultivation().getModifiers())
                if (modifier.CATEGORY.compareTo(CATEGORY) == 0)
                    return false;

        return true;
    }

    public boolean canLearn(Player player)
    {
        if (unlockQuest == null)
            return false;

        return true;
    }

    public boolean hasLearnt(Player player)
    {
        if (!canLearn(player))
            return false;

        double progress = CultivatorStats.getCultivatorStats(player).getConceptProgress(ID);

        if (progress >= unlockQuest.complete)
            return true;

        return false;
    }
}
