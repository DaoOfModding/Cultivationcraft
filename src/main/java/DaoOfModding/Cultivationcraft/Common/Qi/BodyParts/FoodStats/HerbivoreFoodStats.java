package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;

public class HerbivoreFoodStats extends QiFoodStats
{
    @Override
    public void eat(int p_75122_1_, float p_75122_2_)
    {
        // Food gives double nutrition value
        super.eat(p_75122_1_ * 2, p_75122_2_);
    }

    @Override
    public int getNutrition(BlockState block)
    {
        if (block.getMaterial() == Material.GRASS)
            return 1;

        if (block.getMaterial() == Material.LEAVES)
            return 1;

        if (block.getMaterial() == Material.REPLACEABLE_PLANT)
            return 1;

        if (block.getMaterial() == Material.REPLACEABLE_FIREPROOF_PLANT)
            return 1;

        if (block.getMaterial() == Material.VEGETABLE)
            return 1;

        return 0;
    }

    @Override
    public boolean canEatMeat()
    {
        return false;
    }

}
