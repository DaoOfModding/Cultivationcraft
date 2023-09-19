package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.List;

public class QiFoodStats extends FoodData
{
    protected int maxFood = 20;
    protected float exhaustionLevel = 0;
    protected float foodLevel = 20;
    public int tickTimer = 0;
    protected float lastFoodLevel = 20;

    protected float lastSendFood = 0;

    protected animatedTexture outerfilling = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/outerfilling.png"));
    protected Vector3f staminaColor = new Vector3f(1.0F, 0.5F, 0.0F);
    protected Vector3f saturationColor = new Vector3f(1.0F, 0.9F, 0.2F);

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

        setSaturation(Math.min(getSaturationLevel() + (float)p_75122_1_ * p_75122_2_ * 2.0F, getTrueFoodLevel()));
    }

    public animatedTexture getOrb()
    {
        return outerfilling;
    }

    public Vector3f getColor()
    {
        return staminaColor;
    }

    public Vector3f getSaturationColor()
    {
        return saturationColor;
    }

    // Server side only
    @Override
    public void tick(Player player)
    {
        // Do nothing is player is dead
        if (!player.isAlive())
            return;

        // Handle stomach food drain here
        drainFood(player);

        // Handle passive stamina drain
        // Divided by 20 to convert seconds into ticks
        StaminaHandler.consumeStamina(player,BodyPartStatControl.getStats(player).getStat(StatIDs.staminaDrain) / 20.0f);

        // Get the player's blood and let it handle passive regen
        PlayerHealthManager.getBlood(player).regen(player);

        // Ensure that health and stamina don't go above max values
        if (player.getHealth() > player.getMaxHealth())
            player.setHealth(player.getMaxHealth());

        if (getFoodLevel() > getMaxFood())
            setFoodLevel(getMaxFood());

        // Check starve conditions
        if (this.foodLevel <= 0)
        {
            ++this.tickTimer;
            if (this.tickTimer >= 80)
            {
                if (player.getHealth() > 10.0F || player.level.getDifficulty() == Difficulty.HARD || player.getHealth() > 1.0F && player.level.getDifficulty() == Difficulty.NORMAL)
                    player.hurt(DamageSource.STARVE, 1.0F);

                this.tickTimer = 0;
            }
        }
    }

    public boolean shouldUpdate()
    {
        if (foodLevel == lastFoodLevel)
            return false;

        lastFoodLevel = foodLevel;

        return true;
    }

    protected void drainFood(Player player)
    {
        lastFoodLevel = this.foodLevel;

        float staminaUse = PlayerHealthManager.getStaminaUse(player);

/*
        // Vanilla minecraft stamina handling
        if (this.exhaustionLevel > 4.0F)
        {
            this.exhaustionLevel -= 4.0F;

            if (getSaturationLevel() > 0.0F)
                setSaturation(Math.max(getSaturationLevel() - staminaUse, 0.0F));
            else if (player.level.getDifficulty() != Difficulty.PEACEFUL)
                setFoodLevel(Math.max(getTrueFoodLevel() - staminaUse, 0));
        }*/

        // Reduce stamina immediately rather than in units of 1
        Difficulty difficulty = player.level.getDifficulty();
        if (exhaustionLevel > 0F)
        {
            float change = (exhaustionLevel / 4F) * staminaUse;
            exhaustionLevel = 0F;

            if (getSaturationLevel() > 0.0F)
                setSaturation(Math.max(getSaturationLevel() - change, 0.0F));
            else if (difficulty != Difficulty.PEACEFUL)
            {
                QuestHandler.progressQuest(player, Quest.DRAIN_STAMINA, change);
                setFoodLevel(Math.max(getTrueFoodLevel() - change, 0));
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_75112_1_)
    {
        if (p_75112_1_.contains("foodLevel", 99))
        {
            setFoodLevel(p_75112_1_.getFloat("foodLevel"));
            setSaturation(p_75112_1_.getFloat("foodSaturationLevel"));
            setExhaustion(p_75112_1_.getFloat("foodExhaustionLevel"));
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_75117_1_)
    {
        p_75117_1_.putFloat("foodLevel", getTrueFoodLevel());
        p_75117_1_.putFloat("foodSaturationLevel", getSaturationLevel());
        p_75117_1_.putFloat("foodExhaustionLevel", getExhaustionLevel());
    }

    @Override
    public void addExhaustion(float p_75113_1_)
    {
        setExhaustion(getExhaustionLevel() + p_75113_1_);
    }

    @Override
    public float getExhaustionLevel()
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
    public int getLastFoodLevel()
    {
        return (int)lastFoodLevel;
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

    // Is called when meditating
    // Is supplied the amount of Qi the player can absorb during this turn, and returns any modifications to that amount
    public int meditation(int QiRemaining, List<QiSource> sources, Player player)
    {
        return QiRemaining;
    }

    public QiFoodStats clone()
    {
        QiFoodStats clone = new QiFoodStats();
        clone.maxFood = maxFood;
        clone.exhaustionLevel = exhaustionLevel;
        clone.foodLevel = foodLevel;
        clone.tickTimer = tickTimer;

        return clone;
    }
}
