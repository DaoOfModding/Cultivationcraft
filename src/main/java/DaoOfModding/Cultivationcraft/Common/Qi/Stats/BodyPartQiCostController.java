package DaoOfModding.Cultivationcraft.Common.Qi.Stats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSourceConfig;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class BodyPartQiCostController
{
    protected static int getModifications(Player player)
    {
        int modifications = BodyModifications.getBodyModifications(player).getModifications().size();

        for (HashMap<String, BodyPartOption> option : BodyModifications.getBodyModifications(player).getModificationOptions().values())
            modifications += option.size();

        return modifications;
    }

    // Qi Absorb is based on the number of body modifications made by the player
    public static int calculateQiAbsorb(Player player)
    {
        double p = 1 - Math.pow(0.5, 0.1 * (double)(getModifications(player) + 1));

        return (int) (Math.pow(p, 5) * (float)QiSourceConfig.MaxStorage / 100.0);
    }

    // Qi Cost is based on the number of body modifications made by the player
    // It will take 100 ticks of full QiAbsorb to match the cost
    public static int calculateQiCost(Player player)
    {
        double p = 1 - Math.pow(0.5, 0.1 * (double)(getModifications(player) + 1));

        return (int) (Math.pow(p, 5) * QiSourceConfig.MaxStorage);
    }
}
