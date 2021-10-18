package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public class BodyPartStatControl
{
    private static HashMap<UUID, PlayerStatModifications> stats = new HashMap<UUID, PlayerStatModifications>();

    // Setup players with the default player stats
    private static void setupStats(UUID playerID)
    {
        PlayerStatModifications defaultPlayerStats = new PlayerStatModifications();
        defaultPlayerStats.setStat(StatIDs.jumpHeight, 1);


        stats.put(playerID, defaultPlayerStats);
    }

    public static void addStats(UUID playerID, PlayerStatModifications statsToAdd)
    {
        if (!stats.containsKey(playerID))
            setupStats(playerID);

        stats.get(playerID).combine(statsToAdd);
    }

    public static PlayerStatModifications getStats(UUID playerID)
    {
        if (!stats.containsKey(playerID))
            setupStats(playerID);

        return stats.get(playerID);
    }

    public static void updateStats(PlayerEntity player)
    {
        // Clear the existing stats
        setupStats(player.getUUID());

        // Add all existing body part stats to the players stats
        for (BodyPart part : BodyModifications.getBodyModifications(player).getModifications().values())
        {
            part.onLoad(player.getUUID());
            BodyPartStatControl.addStats(player.getUUID(), part.getStatChanges());
        }

        // Add all existing body part option stats to the players stats
        for (HashMap<String, BodyPartOption> options : BodyModifications.getBodyModifications(player).getModificationOptions().values())
        for (BodyPartOption part : options.values())
            {
                // Only do bodyPart onLoads on client
                if (player.isLocalPlayer())
                    part.onLoad(player.getUUID());

                BodyPartStatControl.addStats(player.getUUID(), part.getStatChanges());
            }

        // Add all existing stat modifiers on active techniques to the player stats
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
        {
            Technique tech = techs.getTechnique(i);
            if (tech != null && tech.isActive() && tech.getStats() != null)
                BodyPartStatControl.addStats(player.getUUID(), tech.getStats());
        }
    }
}
