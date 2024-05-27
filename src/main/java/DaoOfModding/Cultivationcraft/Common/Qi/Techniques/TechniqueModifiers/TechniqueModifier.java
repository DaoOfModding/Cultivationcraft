package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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

    float damageMult = 1;

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

    public float getDamageMultiplier(Technique tech)
    {
        return damageMult;
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

    public void onHitEntity(Player owner, Entity hit, float damage, ResourceLocation element, Vec3 pos)
    {
        onHitAll(owner, pos, damage, element);
    }

    public void onHitBlock(Player owner, BlockPos blockPos, float damage, ResourceLocation element, Vec3 pos)
    {
        onHitAll(owner, pos, damage, element);
    }

    public void onHitAll(Player owner, Vec3 pos, float damage, ResourceLocation element)
    {
    }

    public void onHitTaken(Player owner, float damage, ResourceLocation defensiveElement)
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

        for (TechniqueModifier modifier : cultivation.getModifiers())
        {
            // Cannot have multiple copies of the same modifier
            if (modifier.ID.compareTo(this.ID) == 0)
                return false;

            // Cannot be the same category as another active modifier if allowSameCategory is false
            if (!allowSameCategory && modifier.CATEGORY.compareTo(CATEGORY) == 0)
                return false;
        }

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

    public Vec3 modifyMovement(Vec3 move)
    {
        return move;
    }

    // Instantly learn this modifier
    public void learn(Player player)
    {
        CultivatorStats.getCultivatorStats(player).setConceptProgress(ID, unlockQuest.complete);
        PacketHandler.sendCultivatorStatsToClient(player);
    }
}
