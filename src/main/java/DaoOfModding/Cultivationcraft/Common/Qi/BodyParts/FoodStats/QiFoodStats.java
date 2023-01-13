package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.block.state.BlockState;

public class QiFoodStats extends FoodData
{
    protected int maxFood = 20;
    protected float exhaustionLevel = 0;
    protected float foodLevel = 20;
    public int tickTimer = 0;

    public void setMaxFood(int newMaxFood)
    {
        maxFood = newMaxFood;
    }

    public int getMaxFood()
    {
        return maxFood;
    }

    @Override
    public void eat(int p_75122_1_, float p_75122_2_)
    {
        setFoodLevel(Math.min(p_75122_1_ + getTrueFoodLevel(), maxFood));

        // Disable saturation
        //setSaturation(Math.min(getSaturationLevel() + (float)p_75122_1_ * p_75122_2_ * 2.0F, getTrueFoodLevel()));
    }

    @Override
    public void tick(Player p_75118_1_)
    {
        // Do nothing is player is dead
        if (!p_75118_1_.isAlive())
            return;

        // Handle stomach food drain here
        drainFood(p_75118_1_);

        // Handle passive stamina drain
        // Divided by 20 to convert seconds into ticks
        setFoodLevel(Math.min(Math.max(getTrueFoodLevel() + BodyPartStatControl.getStats(p_75118_1_.getUUID()).getStat(StatIDs.staminaDrain) / 20, 0), getMaxFood()));

        // Get the player's blood and let it handle passive regen
        PlayerHealthManager.getBlood(p_75118_1_).regen(p_75118_1_);
    }

    protected void drainFood(Player player)
    {
        float staminaUse = PlayerHealthManager.getStaminaUse(player);

        // Vanilla minecraft stamina handling
        /*
        Difficulty difficulty = player.level.getDifficulty();
        if (this.exhaustionLevel > 4.0F)
        {
            this.exhaustionLevel -= 4.0F;

            if (getSaturationLevel() > 0.0F)
                setSaturation(Math.max(getSaturationLevel() - staminaUse, 0.0F));
            else if (difficulty != Difficulty.PEACEFUL)
                setFoodLevel((float)Math.max(getTrueFoodLevel() - staminaUse, 0));
        }*/
        // Reduce stamina immediately rather than in units of 1
        Difficulty difficulty = player.level.getDifficulty();
        if (exhaustionLevel > 0F)
        {
            float change = (exhaustionLevel / 4F) * staminaUse;

            exhaustionLevel = 0F;

            if (getSaturationLevel() > 0.0F)
            {
                if (player.level.isClientSide)
                    setSaturation(Math.max(getSaturationLevel() - change, 0.0F));
            }
            else if (difficulty != Difficulty.PEACEFUL)
                setFoodLevel(Math.max(getTrueFoodLevel() - change, 0));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_75112_1_)
    {
        if (p_75112_1_.contains("foodLevel", 99))
        {
            setFoodLevel(p_75112_1_.getFloat("foodLevel"));
            //setSaturation(p_75112_1_.getFloat("foodSaturationLevel"));
            //setExhaustion(p_75112_1_.getFloat("foodExhaustionLevel"));
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_75117_1_)
    {
        p_75117_1_.putFloat("foodLevel", getTrueFoodLevel());
        //p_75117_1_.putFloat("foodSaturationLevel", getSaturationLevel());
        //p_75117_1_.putFloat("foodExhaustionLevel", getExhaustion());
    }

    @Override
    public void addExhaustion(float p_75113_1_)
    {
        setExhaustion(getExhaustion() + p_75113_1_);
    }

    public float getExhaustion()
    {
        return exhaustionLevel;
    }

    public void setExhaustion(float newLevel)
    {
        exhaustionLevel = newLevel;
    }

    @Override
    public boolean needsFood()
    {
        return getFoodLevel() < getMaxFood();
    }

    @Override
    public int getFoodLevel()
    {
        return (int)foodLevel;
    }

    public float getTrueFoodLevel()
    {
        return foodLevel;
    }

    @Override
    public void setFoodLevel(int p_75114_1_)
    {
        foodLevel = p_75114_1_ + (foodLevel - (int)foodLevel);
    }

    public void setFoodLevel(float p_75114_1_)
    {
        foodLevel = p_75114_1_;
    }

    public boolean isEdible(ItemStack item)
    {
        if (item.getItem().getFoodProperties() == null)
            return false;

        if (!item.getItem().getFoodProperties().isMeat())
            return true;

        if (item.getItem().getFoodProperties().isMeat() && canEatMeat())
            return true;

        return false;
    }

    public boolean canEatMeat()
    {
        return true;
    }
    
    public int getNutrition(BlockState block)
    {
        return 0;
    }
}
