package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats;

import net.minecraft.item.ItemStack;

public class CarnivoreFoodStats extends QiFoodStats
{
    @Override
    public void eat(int p_75122_1_, float p_75122_2_)
    {
        // Food gives double nutrition value
        super.eat(p_75122_1_ * 2, p_75122_2_);
    }

    @Override
    public boolean isEdible(ItemStack item)
    {
        if (item.getItem().getFoodProperties() == null)
            return false;

        // Can only eat meat
        if (!item.getItem().getFoodProperties().isMeat())
            return false;

        return super.isEdible(item);
    }
}
