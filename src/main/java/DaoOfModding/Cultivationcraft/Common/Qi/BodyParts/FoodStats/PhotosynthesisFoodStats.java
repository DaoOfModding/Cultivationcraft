package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PhotosynthesisFoodStats extends QiFoodStats
{

    // Server side only
    @Override
    public void tick(Player player)
    {
        // Do nothing is player is dead
        if (!player.isAlive())
            return;

        if (player.level.canSeeSky(player.blockPosition()))
            if (player.level.getSkyDarken() < 3)
                increaseFoodLevel(0.01f - (player.level.getSkyDarken() / 4f) * 0.01f, 0.8f);

        super.tick(player);
    }

    // Do not eat
    @Override
    public void eat(int p_75122_1_, float p_75122_2_)
    {
    }

    public void increaseFoodLevel(float amount, float saturationModifier)
    {
        setFoodLevel(Math.min(amount + getTrueFoodLevel(), maxFood));
        setSaturation(Math.min(getSaturationLevel() + amount * saturationModifier * 2.0F, getTrueFoodLevel()));
    }

    @Override
    public boolean canEatMeat()
    {
        return false;
    }

    @Override
    public boolean isEdible(ItemStack item)
    {
        return false;
    }

    @Override
    public PhotosynthesisFoodStats clone()
    {
        PhotosynthesisFoodStats clone = new PhotosynthesisFoodStats();
        clone.maxFood = maxFood;
        clone.exhaustionLevel = exhaustionLevel;
        clone.foodLevel = foodLevel;
        clone.tickTimer = tickTimer;

        return clone;
    }
}
