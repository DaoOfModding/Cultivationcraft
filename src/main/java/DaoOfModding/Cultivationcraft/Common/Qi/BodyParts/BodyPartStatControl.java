package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;

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

    public static void updateStats(PlayerEntity player)
    {
        getPlayerStatControl(player.getUUID()).updateStats(player);
    }
}
