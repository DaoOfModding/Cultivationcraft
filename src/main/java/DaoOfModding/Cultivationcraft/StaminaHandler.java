package DaoOfModding.Cultivationcraft;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.world.entity.player.Player;

public class StaminaHandler
{
    public static void updateStamina(Player player, float stamina)
    {
        ((QiFoodStats) player.getFoodData()).setFoodLevel(stamina);
    }

    public static boolean consumeStamina(Player player, float stamina)
    {
        if (player.isCreative())
            return true;

        // Multiply stamina use by weight
        stamina = stamina * BodyPartStatControl.getStats(player).getStat(StatIDs.weight);

        if (((QiFoodStats)player.getFoodData()).getTrueFoodLevel() < stamina)
            return false;

        if (!player.level.isClientSide)
            QuestHandler.progressQuest(player, Quest.DRAIN_STAMINA, stamina);

        // Update stamina on server here
        /*if (PlayerUtils.isClientPlayerCharacter(player))
            ClientPacketHandler.consumeStaminaOnServer(stamina);*/

        float saturation = player.getFoodData().getSaturationLevel();

        if (saturation > 0)
            if (saturation > stamina)
            {
                player.getFoodData().setSaturation(saturation - stamina);
                return true;
            }
            else
            {
                stamina = stamina - saturation;
                player.getFoodData().setSaturation(0);
            }

        ((QiFoodStats) player.getFoodData()).setFoodLevel(((QiFoodStats) player.getFoodData()).getTrueFoodLevel() - stamina);

        return true;
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
