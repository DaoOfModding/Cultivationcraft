package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BodyPartStatControl
{
    protected static HashMap<UUID, PlayerStatControl> stats = new HashMap<UUID, PlayerStatControl>();

    public static void addStats(UUID playerID, PlayerStatModifications statsToAdd)
    {
        getPlayerStatControl(playerID).getStats().combine(statsToAdd);
    }

    public static PlayerStatControl getPlayerStatControl(UUID playerID)
    {
        if (!stats.containsKey(playerID))
            stats.put(playerID, new PlayerStatControl());

        return stats.get(playerID);
    }

    public static void applyCaps(Player player)
    {
        PlayerStatModifications stats = getPlayerStatControl(player.getUUID()).getStats();
        Blood blood = PlayerHealthManager.getBlood(player);

        for (Map.Entry<ResourceLocation, Float> eStat : stats.elementalStats.get(StatIDs.resistanceModifier).entrySet())
            if (eStat.getValue() > 100 && !blood.canHeal(eStat.getKey()))
                stats.setElementalStat(StatIDs.resistanceModifier, eStat.getKey(), 100);
    }

    public static PlayerStatModifications getStats(UUID playerID)
    {
        return getPlayerStatControl(playerID).getStats();
    }

    public static void updateStats(Player player)
    {
        getPlayerStatControl(player.getUUID()).updateStats(player);
    }
}
