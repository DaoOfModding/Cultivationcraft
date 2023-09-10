package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class TechniqueStatModification
{
    public final ResourceLocation statModification;
    protected HashMap<ResourceLocation, Double> statsPerLevel = new HashMap<ResourceLocation, Double>();

    public TechniqueStatModification(ResourceLocation stat)
    {
        statModification = stat;
    }

    public void addStatChange(ResourceLocation stat, double change)
    {
        statsPerLevel.put(stat, change);
    }

    public HashMap<ResourceLocation, Double> getStatChanges()
    {
        return statsPerLevel;
    }

    public double getStatChange(ResourceLocation stat)
    {
        if (statsPerLevel.containsKey(stat))
            return statsPerLevel.get(stat);

        return 0;
    }
}
