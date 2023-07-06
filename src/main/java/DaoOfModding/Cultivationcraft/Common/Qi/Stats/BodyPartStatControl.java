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
    protected static HashMap<UUID, PlayerStatControl> statsServer = new HashMap<UUID, PlayerStatControl>();

    public static void addStats(Player player, PlayerStatModifications statsToAdd)
    {
        getPlayerStatControl(player).getStats().combine(statsToAdd);
    }

    public static PlayerStatControl getPlayerStatControl(Player player)
    {
        UUID playerID = player.getUUID();

        if (player.level.isClientSide)
        {
            if (!stats.containsKey(playerID))
                stats.put(playerID, new PlayerStatControl());

            return stats.get(playerID);
        }
        else
        {
            if (!statsServer.containsKey(playerID))
                statsServer.put(playerID, new PlayerStatControl());

            return statsServer.get(playerID);
        }
    }

    public static void applyCaps(Player player)
    {
        PlayerStatModifications stats = getPlayerStatControl(player).getStats();

        Blood blood = PlayerHealthManager.getBlood(player);

        if (stats.elementalStats.get(StatIDs.resistanceModifier) != null)
            for (Map.Entry<ResourceLocation, Float> eStat : stats.elementalStats.get(StatIDs.resistanceModifier).entrySet())
                if (eStat.getValue() > 100 && !blood.canHeal(eStat.getKey(), null))
                    stats.setElementalStat(StatIDs.resistanceModifier, eStat.getKey(), 100);
    }

    public static PlayerStatModifications getStats(Player player)
    {
        return getPlayerStatControl(player).getStats();
    }

    public static void updateStats(Player player)
    {
        getPlayerStatControl(player).updateStats(player);
    }
}
