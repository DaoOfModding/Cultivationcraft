package DaoOfModding.Cultivationcraft;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import net.minecraft.world.entity.player.Player;

public class StaminaHandler
{
    public static void updateStamina(Player player, float stamina)
    {
        ((QiFoodStats) player.getFoodData()).setFoodLevel(stamina);
    }

    public static float getStamina(Player player)
    {
        return ((QiFoodStats)player.getFoodData()).getTrueFoodLevel();
    }

    public static float getMaxStamina(Player player)
    {
        return ((QiFoodStats)player.getFoodData()).getMaxFood();
    }
}
