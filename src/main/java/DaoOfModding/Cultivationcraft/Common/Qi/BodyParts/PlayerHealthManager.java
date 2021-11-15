package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.BloodPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.StomachPart;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class PlayerHealthManager
{
    static Blood defaultBlood = new Blood();

    static Field foodStatField;

    public static void setup()
    {
        foodStatField = ObfuscationReflectionHelper.findField(PlayerEntity.class,"field_71100_bB");
    }

    public static Blood getBlood(PlayerEntity player)
    {
        // If the player is not a body cultivator or has not forged their blood, then return the defauly bloody type
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return defaultBlood;

        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        if (!modifications.hasOption(BodyPartNames.bodyPosition, BodyPartNames.bloodSubPosition))
            return defaultBlood;

        // Return the blood type of the players forged blood
        return ((BloodPart)modifications.getOption(BodyPartNames.bodyPosition, BodyPartNames.bloodSubPosition)).getBloodType();
    }

    public static void updateFoodStats(PlayerEntity player)
    {
        QiFoodStats food = new QiFoodStats();

        // Update the players food stats if they have cultivated their stomach
        // Otherwise use the default food handler
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
        {
            IBodyModifications modifications = BodyModifications.getBodyModifications(player);

            if (modifications.hasOption(BodyPartNames.bodyPosition, BodyPartNames.stomachSubPosition))
                food = ((StomachPart)modifications.getOption(BodyPartNames.bodyPosition, BodyPartNames.bloodSubPosition)).getFoodStats();
        }

        // Set the players max stamina
        food.setMaxFood((int)BodyPartStatControl.getStats(player.getUUID()).getStat(StatIDs.maxStamina));

        // Update the players food stat variable
        setFoodStats(player, food);
    }

    private static void setFoodStats(PlayerEntity player, QiFoodStats newFood)
    {
        FoodStats oldFood = player.getFoodData();

        if (oldFood instanceof QiFoodStats)
            updateQiFoodStats((QiFoodStats) oldFood, newFood);
        else
            updateOldFoodStats(oldFood, newFood);


        try { foodStatField.set(player, newFood); }
        catch (Exception e) { Cultivationcraft.LOGGER.error("Error setting food stats: " + e); }
    }

    private static void updateQiFoodStats(QiFoodStats oldFoodStats, QiFoodStats newFoodStats)
    {
        newFoodStats.setFoodLevel(oldFoodStats.getTrueFoodLevel());
        newFoodStats.setSaturation(oldFoodStats.getSaturationLevel());
        newFoodStats.setExhaustion(oldFoodStats.getExhaustion());
    }

    private static void updateOldFoodStats(FoodStats oldFoodStats, QiFoodStats newFoodStats)
    {
        newFoodStats.setFoodLevel(oldFoodStats.getFoodLevel());
        newFoodStats.setSaturation(oldFoodStats.getSaturationLevel());
    }
}
