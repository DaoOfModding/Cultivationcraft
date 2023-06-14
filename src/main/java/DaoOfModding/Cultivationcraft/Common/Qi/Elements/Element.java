package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
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

    public void applyStatusEffect(Entity target, float damageAmount)
    {
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
