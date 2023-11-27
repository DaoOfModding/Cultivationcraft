package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class TechniqueStatModification
{
    public final ResourceLocation statModification;
    protected HashMap<ResourceLocation, Double> statsPerLevel = new HashMap<ResourceLocation, Double>();
    protected HashMap<ResourceLocation, Double> minStat = new HashMap<ResourceLocation, Double>();
    protected HashMap<ResourceLocation, Double> maxStat = new HashMap<ResourceLocation, Double>();

    public TechniqueStatModification(ResourceLocation stat)
    {
        statModification = stat;
    }

    public void addStatChange(ResourceLocation stat, double change)
    {
        statsPerLevel.put(stat, change);
    }

    public void addMinStatChange(ResourceLocation stat, double change)
    {
        minStat.put(stat, change);
    }

    public void addMaxStatChange(ResourceLocation stat, double change)
    {
        maxStat.put(stat, change);
    }

    public HashMap<ResourceLocation, Double> getStatChanges()
    {
        return statsPerLevel;
    }

    public double getStatChange(ResourceLocation stat)
    {
        if (statsPerLevel.containsKey(stat))
        {
            double statValue = statsPerLevel.get(stat);

            if (minStat.containsKey(stat) && minStat.get(stat) > statValue)
                return minStat.get(stat);

            if (maxStat.containsKey(stat) && maxStat.get(stat) < statValue)
                return maxStat.get(stat);

            return statValue;
        }

        return 0;
    }
}
