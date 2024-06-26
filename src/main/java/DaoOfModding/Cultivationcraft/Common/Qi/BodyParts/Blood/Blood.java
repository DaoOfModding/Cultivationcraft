package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class Blood
{
    protected Vector3f colour = new Vector3f(1, 0 ,0);
    protected animatedTexture orbFilling = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/orbfilling.png"));

    // How many ticks blood particles remain for
    public int life = 20*60;
    protected float staminaHealingModifier = 6;

    public Vector3f getColour()
    {
        return colour;
    }

    public animatedTexture getOrbFilling()
    {
        return orbFilling;
    }

    public boolean canHeal(ResourceLocation element, @Nullable Player player)
    {
        return false;
    }

    // Handle passive player regen here
    public void regen(Player player)
    {
        if (!staminaHealing(player))
            naturalHealing(player);
    }

    public boolean staminaHealing(Player player)
    {
        boolean flag = player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        QiFoodStats food = (QiFoodStats)player.getFoodData();

        if (flag && food.getSaturationLevel() > 0.0F && player.isHurt() && food.getFoodLevel() >= food.getMaxFood())
        {
            ++food.tickTimer;
            if (food.tickTimer >= 10)
            {
                float f = Math.min(food.getSaturationLevel(), 6);

                if (!player.level.isClientSide)
                    QuestHandler.progressQuest(player, Quest.HEAL, f / staminaHealingModifier);

                player.heal(f / staminaHealingModifier);
                food.addExhaustion(f);
                food.tickTimer = 0;
            }

            return true;
        }

        return false;
    }

    public void naturalHealing(Player player)
    {
        boolean flag = player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        QiFoodStats food = (QiFoodStats)player.getFoodData();

        if (flag && food.getFoodLevel() >= food.getMaxFood() * 0.9 && player.isHurt())
        {
            ++food.tickTimer;
            if (food.tickTimer >= 80) {
                if (!player.level.isClientSide)
                    QuestHandler.progressQuest(player, Quest.HEAL, 1.0F);

                player.heal(1.0F);
                food.addExhaustion(6.0F);
                food.tickTimer = 0;
            }
        }
    }

    // Each droplet of blood will call this function every tick
    // If true is returned the particle with be removed
    public boolean externalTick(Level level, double x, double y, double z, boolean onGround)
    {
        return false;
    }

    public void onHit(Player player, Vec3 source, double amount)
    {

    }

    // Is called when meditating
    // Is supplied the amount of Qi the player can absorb during this turn, and returns any modifications to that amount
    public double meditation(double QiRemaining, List<QiSource> sources, Player player)
    {
        return QiRemaining;
    }
}
