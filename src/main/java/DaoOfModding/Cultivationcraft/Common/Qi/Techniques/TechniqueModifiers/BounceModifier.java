package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class BounceModifier extends TechniqueModifier
{
    public BounceModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.bounce");
        CATEGORY = MODIFIER_CATEGORY;

        unlockQuest = new Quest(Quest.BOUNCE, 1);
        stabiliseQuest = new Quest(Quest.BOUNCE, 1000);

        allowSameCategory = true;
    }

    // TODO: Qi consumption reduced on damage taken
    // TODO: Qi Emissions bounce off blocks

    public void onHitTaken(Player owner, float damage, ResourceLocation defensiveElement, QiDamageSource source)
    {
        if (source.isInternal() || damage <= 0)
            return;

        // Do nothing if the player does not have enough Qi
        if (!CultivatorStats.getCultivatorStats(owner).getCultivation().consumeQi(owner, damage))
            return;

        if (source.msgId.compareTo(DamageSource.FALL.getMsgId()) == 0)
            Physics.Bounce(owner, 0.75f);
    }
}
