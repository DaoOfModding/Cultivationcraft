package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import com.mojang.math.Vector3f;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;

public class Blood
{
    protected Vector3f colour = new Vector3f(1, 0 ,0);

    public Vector3f getColour()
    {
        return colour;
    }

    // Handle passive player regen here
    public void regen(Player player)
    {
        // Vanilla health regen
        boolean flag = player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        Difficulty difficulty = player.level.getDifficulty();

        QiFoodStats food = (QiFoodStats)player.getFoodData();

        if (flag && food.getSaturationLevel() > 0.0F && player.isHurt() && food.getFoodLevel() >= food.getMaxFood())
        {
            ++food.tickTimer;
            if (food.tickTimer >= 10) {
                float f = Math.min(food.getSaturationLevel(), 6.0F);
                player.heal(f / 6.0F);
                food.addExhaustion(f);
                food.tickTimer = 0;
            }
        }
        else if (flag && food.getFoodLevel() >= food.getMaxFood() * 0.9 && player.isHurt())
        {
            ++food.tickTimer;
            if (food.tickTimer >= 80) {
                player.heal(1.0F);
                food.addExhaustion(6.0F);
                food.tickTimer = 0;
            }
        }
        else if (food.getFoodLevel() <= 0)
        {
            ++food.tickTimer;
            if (food.tickTimer >= 80) {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL)
                {
                    player.hurt(DamageSource.STARVE, 1.0F);
                }

                food.tickTimer = 0;
            }
        } else {
            food.tickTimer = 0;
        }
    }
}
