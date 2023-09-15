package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CultivationType
{
    public boolean canCultivate(ResourceLocation element)
    {
        return true;
    }

    public int progressCultivation(Player player, int Qi, ResourceLocation element)
    {
        if (!canCultivate(element))
            return Qi;

        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        int max = getMaxQi(player);
        int currentQi = stats.getQi();

        System.out.println(currentQi);

        // If the current Qi is at the max, do nothing
        if (currentQi >= max)
            return Qi;

        int remains = currentQi + Qi - max;

        // If cultivating will go over the max Qi, set Qi to max and return the remaining amount
        if (remains > 0)
        {
            stats.setQi(max);
            return remains;
        }

        // Add the cultivated Qi into the current Qi and return
        stats.setQi(currentQi + Qi);
        return 0;
    }

    public int getMaxQi(Player player)
    {
        // TODO: This
        return 1000;
    }

    public int getAbsorbRange(Player player)
    {
        return 1;
    }

    public int getAbsorbSpeed(Player player)
    {
        return 10;
    }
}
