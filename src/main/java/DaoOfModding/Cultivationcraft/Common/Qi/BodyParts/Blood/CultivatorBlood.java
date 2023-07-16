package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Client.Particles.Blood.BloodParticleData;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;

public class CultivatorBlood extends Blood
{
    public CultivatorBlood()
    {
        staminaHealingModifier = 4;
    }

    @Override
    public void naturalHealing(Player player)
    {
        boolean flag = player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        QiFoodStats food = (QiFoodStats)player.getFoodData();

        // If the player has stamina and is hurt, then heal
        if (flag && food.getFoodLevel() > 0 && player.isHurt())
        {
            // Get player regen, divided by 20 to convert seconds into ticks
            float regen = BodyPartStatControl.getStats(player).getStat(StatIDs.healthRegen) / 20;

            if (!player.level.isClientSide)
                QuestHandler.progressQuest(player, Quest.HEAL, regen);

            player.heal(regen);

            // Exhaust the player by the amount regenerated multiplied by their healthStaminaConversion modifier
            food.addExhaustion(regen * BodyPartStatControl.getStats(player).getStat(StatIDs.healthStaminaConversion) * 4);
        }
    }

    public ParticleOptions getParticle(Player player)
    {
        return new BloodParticleData(player);
    }
}
