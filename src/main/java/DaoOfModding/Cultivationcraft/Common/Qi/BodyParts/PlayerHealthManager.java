package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.BloodPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.LungPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.StomachPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiNotFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.Lung;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lungs;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultCultivationStatIDs;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerHealthManager
{
    static Blood defaultBlood = new Blood();
    protected static HashMap<UUID, Lungs> lungs = new HashMap<UUID, Lungs>();

    public static float getStaminaUse(Player player)
    {
        float staminaUse = BodyPartStatControl.getStats(player).getStat(StatIDs.staminaUse);
        float weight = BodyPartStatControl.getStats(player).getStat(StatIDs.weight);

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

    public static void updateLungs(Player player)
    {
        Lungs lung = new Lungs();

        lung = lung.copy(player);

        // If the player is not a body cultivator or has not forged their blood, then return the default blood type
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
        {
            IBodyModifications modifications = BodyModifications.getBodyModifications(player);

            if (modifications.hasOption(BodyPartNames.bodyPosition, BodyPartNames.lungSubPosition))
            {
                LungPart part = ((LungPart) modifications.getOption(BodyPartNames.bodyPosition, BodyPartNames.lungSubPosition));
                lung = part.getLungType();

                ArrayList<BodyPart> lungParts = modifications.getBodyPartsOfType(LungPart.class);

                // Loop through each lung connection and check if a lung type has been assigned
                for (int i = 0; i < lung.getLungAmount(); i++)
                {
                    ResourceLocation location = lung.getConnection(i).getLocation();

                    for (BodyPart testLung : lungParts)
                    {
                        Lung lungConnection = ((LungPart)testLung).getLung(location);

                        if (lungConnection != null)
                            lung.setLung(i, lungConnection);
                    }
                }

                lung = lung.copy(player);
            }
        }

        lungs.put(player.getUUID(), lung);
    }

    public static Lungs getLungs(Player player)
    {
        if (!lungs.containsKey(player.getUUID()))
            lungs.put(player.getUUID(), new Lungs());

        return lungs.get(player.getUUID());
    }

    public static void updateFoodStats(Player player)
    {
        // If the player is a Qi Condenser then set food to be Qi
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.QI_CONDENSER)
        {
            QiNotFoodStats food = new QiNotFoodStats();
            food.setMaxFood((int) CultivatorStats.getCultivatorStats(player).getCultivation().getCultivationStat(player, DefaultCultivationStatIDs.maxQi));

            // Update the players food stat variable
            setFoodStats(player, food);
        }
        else if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
        {

            QiFoodStats food = new QiFoodStats();


            // Update the players food stats if they have cultivated their stomach
            // Otherwise use the default food handler
            IBodyModifications modifications = BodyModifications.getBodyModifications(player);


            ArrayList<BodyPart> stomach = modifications.getBodyPartsOfType(StomachPart.class);
            if (stomach.size() > 0)
                food = ((StomachPart) stomach.get(0)).getFoodStats().clone();

            // Set the players max stamina
            food.setMaxFood((int) BodyPartStatControl.getStats(player).getStat(StatIDs.maxStamina));

            // Update the players food stat variable
            setFoodStats(player, food);
        }
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
