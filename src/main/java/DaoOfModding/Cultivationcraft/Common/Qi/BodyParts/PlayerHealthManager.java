package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.CultivatorBlood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.BloodPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.StomachPart;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class PlayerHealthManager
{
    static Blood defaultBlood = new Blood();

    public static float getStaminaUse(Player player)
    {
        float staminaUse = BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.staminaUse);
        float weight = BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.weight);

        return staminaUse * weight;
    }

    public static Blood getBlood(Player player)
    {
        // If the player is not a body cultivator or has not forged their blood, then return the default blood type
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return defaultBlood;

        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        if (!modifications.hasOption(BodyPartNames.bodyPosition, BodyPartNames.bloodSubPosition))
            return defaultBlood;

        // Return the blood type of the players forged blood
        return ((BloodPart)modifications.getOption(BodyPartNames.bodyPosition, BodyPartNames.bloodSubPosition)).getBloodType();
    }

    public static void updateFoodStats(Player player)
    {
        // do nothing if the player is not a body cultivator
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return;

        QiFoodStats food = new QiFoodStats();


        // Update the players food stats if they have cultivated their stomach
        // Otherwise use the default food handler
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        if (modifications.hasOption(BodyPartNames.bodyPosition, BodyPartNames.stomachSubPosition))
            food = ((StomachPart)modifications.getOption(BodyPartNames.bodyPosition, BodyPartNames.stomachSubPosition)).getFoodStats().clone();

        // Set the players max stamina
        food.setMaxFood((int)BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.maxStamina));

        // Update the players food stat variable
        setFoodStats(player, food);
    }

    protected static void setFoodStats(Player player, QiFoodStats newFood)
    {
        FoodData oldFood = player.getFoodData();

        if (oldFood instanceof QiFoodStats)
            updateQiFoodStats((QiFoodStats) oldFood, newFood);
        else
            updateOldFoodStats(oldFood, newFood);

        Reflection.setFoodStats(player, newFood);
    }

    protected static void updateQiFoodStats(QiFoodStats oldFoodStats, QiFoodStats newFoodStats)
    {
        newFoodStats.setFoodLevel(oldFoodStats.getTrueFoodLevel());
        newFoodStats.setExhaustion(oldFoodStats.getExhaustionLevel());
        newFoodStats.setSaturation(oldFoodStats.getSaturationLevel());
    }

    protected static void updateOldFoodStats(FoodData oldFoodStats, QiFoodStats newFoodStats)
    {
        newFoodStats.setFoodLevel(oldFoodStats.getFoodLevel());
        newFoodStats.setSaturation(oldFoodStats.getSaturationLevel());
    }
}
