package DaoOfModding.Cultivationcraft;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import net.minecraft.entity.player.PlayerEntity;

public class StaminaHandler
{
    public static void updateStamina(PlayerEntity player, float stamina)
    {
        ((QiFoodStats) player.getFoodData()).setFoodLevel(stamina);
    }

    public static float getStamina(PlayerEntity player)
    {
        return ((QiFoodStats)player.getFoodData()).getTrueFoodLevel();
    }

    public static float getMaxStamina(PlayerEntity player)
    {
        return ((QiFoodStats)player.getFoodData()).getMaxFood();
    }
}
