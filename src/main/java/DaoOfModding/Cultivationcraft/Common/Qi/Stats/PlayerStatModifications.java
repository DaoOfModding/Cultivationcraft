package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatModifications
{
    protected HashMap<ResourceLocation, Float> stats = new HashMap<>();
    protected HashMap<ResourceLocation, HashMap<ResourceLocation, Float>> elementalStats = new HashMap<>();

    public float getStat(ResourceLocation ID)
    {
        float value = 0;

        if (stats.containsKey(ID))
            value = stats.get(ID);


        return value;
    }

    public float getElementalStat(ResourceLocation ID, ResourceLocation element)
    {
        float value = 0;

        if (elementalStats.containsKey(ID))
            if (elementalStats.get(ID).containsKey(element))
                value = elementalStats.get(ID).get(element);

        return value;
    }

    public void setStat(ResourceLocation ID, float value)
    {
        stats.put(ID, value);
    }

    public void setElementalStat(ResourceLocation ID, ResourceLocation element, float value)
    {
        if (!elementalStats.containsKey(ID))
            elementalStats.put(ID, new HashMap<>());

        elementalStats.get(ID).put(element, value);
    }

    public HashMap<ResourceLocation, Float> getStats()
    {
        return stats;
    }


    public HashMap<ResourceLocation, HashMap<ResourceLocation, Float>> getElementalStats()
    {
        return elementalStats;
    }

    // Combine the stats of the specified stat modifier with this stat modifier
    public void combine(PlayerStatModifications newStats)
    {
        for (Map.Entry<ResourceLocation, Float> stat : newStats.getStats().entrySet())
            setStat(stat.getKey(), stat.getValue() + getStat(stat.getKey()));

        for (ResourceLocation eStats : newStats.getElementalStats().keySet())
            for (Map.Entry<ResourceLocation, Float> eStat : newStats.getElementalStats().get(eStats).entrySet())
                setElementalStat(eStats, eStat.getKey(), eStat.getValue() + getElementalStat(eStats, eStat.getKey()));
    }

    public String toString()
    {
        String statString = "";

        for (Map.Entry<ResourceLocation, Float> stat : stats.entrySet())
        {
            statString += "\n" + Component.translatable(stat.getKey().getPath()).getString() + ": ";

            if (stat.getValue() % 1f == 0)
                statString += stat.getValue().intValue();
            else
                statString += stat.getValue();
        }


        for (ResourceLocation eStats : getElementalStats().keySet())
            for (Map.Entry<ResourceLocation, Float> eStat : getElementalStats().get(eStats).entrySet())
            {
                statString += "\n" + Elements.getElement(eStat.getKey()).getName() + " " + Component.translatable(eStats.getPath()).getString() + ": ";

                if (eStat.getValue() % 1f == 0)
                    statString += eStat.getValue().intValue();
                else
                    statString += eStat.getValue();
            }

        return statString;
    }
}
