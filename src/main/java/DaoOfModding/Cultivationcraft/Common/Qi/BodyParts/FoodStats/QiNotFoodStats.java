package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultCultivationStatIDs;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class QiNotFoodStats extends QiFoodStats
{
    public QiNotFoodStats()
    {
        super();

        staminaColor = new Vector3f(0.2f, 0.8f, 0.8f);
    }

    // Server side only
    @Override
    public void tick(Player player)
    {
        // Do nothing is player is dead
        if (!player.isAlive())
            return;

        // Update the max food to equal the maxQi
        setMaxFood((int) CultivatorStats.getCultivatorStats(player).getCultivation().getCultivationStat(player, DefaultCultivationStatIDs.maxQi));

        // Qi doesn't have saturation
        setSaturation(0);

        // Handle stomach food drain here
        drainFood(player);

        // Get the player's blood and let it handle passive regen
        PlayerHealthManager.getBlood(player).regen(player);

        // Ensure that health and Qi don't go above max values
        if (player.getHealth() > player.getMaxHealth())
            player.setHealth(player.getMaxHealth());

        if (getFoodLevel() > getMaxFood())
            setFoodLevel(getMaxFood());
    }

    public boolean isEdible(ItemStack item)
    {
        return false;
    }

    public boolean canEatMeat()
    {
        return false;
    }

    @Override
    public int meditation(int QiRemaining, Player player)
    {
        float toAbsorb = getMaxFood() - getTrueFoodLevel();

        if (toAbsorb <= 0)
            return QiRemaining;

        CultivationType cultivation = CultivatorStats.getCultivatorStats(player).getCultivation();

        if (QiRemaining < toAbsorb)
            toAbsorb = QiRemaining;

        float absorb = cultivation.absorbFromQiSource((int)toAbsorb, player);

        setFoodLevel(getTrueFoodLevel() + absorb);

        if (getTrueFoodLevel() > getMaxFood())
            setFoodLevel(getMaxFood());

        return (int)(QiRemaining - absorb);
    }

    public QiNotFoodStats clone()
    {
        QiNotFoodStats clone = new QiNotFoodStats();
        clone.maxFood = maxFood;
        clone.exhaustionLevel = exhaustionLevel;
        clone.foodLevel = foodLevel;
        clone.tickTimer = tickTimer;

        return clone;
    }
}
