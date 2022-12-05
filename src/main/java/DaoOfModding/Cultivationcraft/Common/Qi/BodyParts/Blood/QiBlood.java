package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;

public class QiBlood extends Blood
{
    @Override
    public void regen(Player player)
    {
        boolean flag = player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        Difficulty difficulty = player.level.getDifficulty();

        QiFoodStats food = (QiFoodStats)player.getFoodData();

        // If the player has stamina and is hurt, then heal
        if (flag && food.getFoodLevel() > 0 && player.isHurt())
        {
            // Get player regen, divided by 20 to convert seconds into ticks
            float regen = BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.healthRegen) / 20;

            player.heal(regen);

            // Exhaust the player by the amount regenerated multiplied by their healthStaminaConversion modifier
            food.addExhaustion(regen * BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.healthStaminaConversion) * 4);
        }
        else if (food.getFoodLevel() <= 0)
        {
            ++food.tickTimer;
            if (food.tickTimer >= 80)
            {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)
                    player.hurt(DamageSource.STARVE, 1.0F);

                food.tickTimer = 0;
            }
        } else
            food.tickTimer = 0;
    }
}
