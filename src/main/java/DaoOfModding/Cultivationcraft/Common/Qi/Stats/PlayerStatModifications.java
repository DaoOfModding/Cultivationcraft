package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatModifications
{
    protected HashMap<String, Float> stats = new HashMap<String, Float>();
    protected HashMap<String, HashMap<ResourceLocation, Float>> elementalStats = new HashMap<>();

    public float getStat(String ID)
    {
        if (stats.containsKey(ID))
            return stats.get(ID);

        return 0;
    }

    public float getElementalStat(String ID, ResourceLocation element)
    {
        if (elementalStats.containsKey(ID))
            if (elementalStats.get(ID).containsKey(element))
                return elementalStats.get(ID).get(element);

        return 0;
    }

    public void setStat(String ID, float value)
    {
        stats.put(ID, value);
    }

    public void setElementalStat(String ID, ResourceLocation element, float value)
    {
        if (!elementalStats.containsKey(ID))
            elementalStats.put(ID, new HashMap<>());

        elementalStats.get(ID).put(element, value);
    }

    public HashMap<String, Float> getStats()
    {
        return stats;
    }


    public HashMap<String, HashMap<ResourceLocation, Float>> getElementalStats()
    {
        return elementalStats;
    }

    // Combine the stats of the specified stat modifier with this stat modifier
    public void combine(PlayerStatModifications newStats)
    {
        for (Map.Entry<String, Float> stat : newStats.getStats().entrySet())
            setStat(stat.getKey(), stat.getValue() + getStat(stat.getKey()));

        for (String eStats : newStats.getElementalStats().keySet())
            for (Map.Entry<ResourceLocation, Float> eStat : newStats.getElementalStats().get(eStats).entrySet())
                setElementalStat(eStats, eStat.getKey(), eStat.getValue());
    }

    public String toString()
    {
        String statString = "";

        for (Map.Entry<String, Float> stat : stats.entrySet())
            statString += "\n" + stat.getKey() + ": " + stat.getValue();


        for (String eStats : getElementalStats().keySet())
            for (Map.Entry<ResourceLocation, Float> eStat : getElementalStats().get(eStats).entrySet())
                statString += "\n" + eStats + " " + Elements.getElement(eStat.getKey()).getName() + ": " + eStat.getValue();

        return statString;
    }
}
