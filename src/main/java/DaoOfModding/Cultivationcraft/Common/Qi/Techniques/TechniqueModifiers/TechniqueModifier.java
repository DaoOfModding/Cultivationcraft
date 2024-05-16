package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
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

    public ResourceLocation getID()
    {
        return ID;
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
        CultivationType cultivation = CultivatorStats.getCultivatorStats(player).getCultivation();

        // This modifier cannot be used if allowSameCategory is false and a technique of the same category is already active
        if (!allowSameCategory)
            for (TechniqueModifier modifier : cultivation.getModifiers())
                if (modifier.CATEGORY.compareTo(CATEGORY) == 0)
                    return false;

        return true;
    }

    public boolean canLearn(Player player)
    {
        if (unlockQuest == null)
            return false;

        CultivationType cultivation = CultivatorStats.getCultivatorStats(player).getCultivation();

        if (cultivation instanceof CoreFormingCultivation)
            return true;

        return false;
    }

    public boolean hasLearnt(Player player)
    {
        double progress = CultivatorStats.getCultivatorStats(player).getConceptProgress(ID);

        if (unlockQuest != null && progress >= unlockQuest.complete)
            return true;

        return false;
    }

    // Instantly learn this modifier
    public void learn(Player player)
    {
        CultivatorStats.getCultivatorStats(player).setConceptProgress(ID, unlockQuest.complete);
        PacketHandler.sendCultivatorStatsToClient(player);
    }
}
