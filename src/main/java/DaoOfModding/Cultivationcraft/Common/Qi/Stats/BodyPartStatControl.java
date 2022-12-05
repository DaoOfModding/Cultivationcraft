package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

public class BodyPartStatControl
{
    private static HashMap<UUID, PlayerStatControl> stats = new HashMap<UUID, PlayerStatControl>();

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

    public static PlayerStatModifications getStats(UUID playerID)
    {
        return getPlayerStatControl(playerID).getStats();
    }

    public static void updateStats(Player player)
    {
        getPlayerStatControl(player.getUUID()).updateStats(player);
    }
}
