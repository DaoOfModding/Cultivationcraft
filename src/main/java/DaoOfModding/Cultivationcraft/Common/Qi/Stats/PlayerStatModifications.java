package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatModifications
{
    private HashMap<String, Float> stats = new HashMap<String, Float>();

    public float getStat(String ID)
    {
        if (stats.containsKey(ID))
            return stats.get(ID);

        return 0;
    }

    public void setStat(String ID, float value)
    {
        stats.put(ID, value);
    }

    public HashMap<String, Float> getStats()
    {
        return stats;
    }

    // Combine the stats of the specified stat modifier with this stat modifier
    public void combine(PlayerStatModifications newStats)
    {
        for (Map.Entry<String, Float> stat : newStats.getStats().entrySet())
            setStat(stat.getKey(), stat.getValue() + getStat(stat.getKey()));
    }
}
