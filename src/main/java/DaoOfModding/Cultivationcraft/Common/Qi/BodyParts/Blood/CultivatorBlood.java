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
    @Override
    public void regen(Player player)
    {
        boolean flag = player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);

        QiFoodStats food = (QiFoodStats)player.getFoodData();

        if (flag && food.getSaturationLevel() > 0.0F && player.isHurt() && food.getFoodLevel() >= food.getMaxFood())
        {
            ++food.tickTimer;
            if (food.tickTimer >= 10)
            {
                float f = Math.min(food.getSaturationLevel(), 4.0F);

                if (!player.level.isClientSide)
                    QuestHandler.progressQuest(player, Quest.HEAL, f / 4.0F);

                player.heal(f / 4.0F);
                food.addExhaustion(f);
                food.tickTimer = 0;
            }
        }
        // If the player has stamina and is hurt, then heal
        else if (flag && food.getFoodLevel() > 0 && player.isHurt())
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
