package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
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
    public int meditation(int QiRemaining, List<QiSource> sources, Player player)
    {
        float toAbsorb = getMaxFood() - getTrueFoodLevel();

        if (toAbsorb == 0)
            return QiRemaining;

        float absorb = 0;
        CultivationType cultivation = CultivatorStats.getCultivatorStats(player).getCultivation();

        // Draw Qi from each Qi source available
        for (QiSource source : sources)
        {
            // Only absorb from QiSources of the correct element
            if (cultivation.canCultivate(source.getElement()))
                if (QiRemaining > 0 && absorb < toAbsorb)
                {
                    int absorbed = source.absorbQi(QiRemaining, player);
                    QiRemaining -= absorbed;

                    absorb += (float)absorbed;
                }
        }

        if (absorb > toAbsorb)
            absorb = toAbsorb;

        setFoodLevel(getTrueFoodLevel() + absorb);

        return QiRemaining;
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
