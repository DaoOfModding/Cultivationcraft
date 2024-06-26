package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.ArrayList;

public class Element
{
    protected final ResourceLocation name;
    public final Color color;
    public final double density;

    protected double effectTickChance;

    protected ArrayList<ElementVariant> variant = new ArrayList<>();

    public Element (ResourceLocation resourcelocation, Color elementColor, double newDensity)
    {
        name = resourcelocation;
        color = elementColor;
        density = newDensity;
        effectTickChance = 0;
    }

    public String getName()
    {
        return Component.translatable(name.getPath()).getString();
    }

    public ResourceLocation getResourceLocation()
    {
        return name;
    }

    public boolean shouldDoBlockEffect()
    {
        if (Math.random() < effectTickChance)
            return true;

        return false;
    }

    public void applyStatusEffect(QiDamageSource source, Entity target, float damageAmount)
    {
        if (damageAmount > 0 && effectTickChance > 0)
        {
            if (target instanceof Player)
            {
                QuestHandler.progressQuest((Player) target, Quest.ELEMENTALY_EFFECTED, 1);
                QuestHandler.progressQuest((Player) target, Quest.ELEMENTALY_EFFECTED, 1, name.toString());
            }

            if (source.getEntity() instanceof Player)
            {;
                QuestHandler.progressQuest((Player) source.getEntity(), Quest.ELEMENTAL_EFFECT_APPLIED, 1);
                QuestHandler.progressQuest((Player) source.getEntity(), Quest.ELEMENTAL_EFFECT_APPLIED, 1, name.toString());
            }
        }
    }

    public void effectBlock(Level level, BlockPos pos)
    {
    }

    public float resistanceModifier(Player player)
    {
        return 0;
    }

    public void addVariant(ElementVariant element)
    {
        variant.add(element);
    }

    public Element getMutation()
    {
        for (ElementVariant variant : variant)
            if (variant.tryMutate())
                return variant;

        return this;
    }
}
