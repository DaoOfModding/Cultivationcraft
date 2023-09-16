package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CultivationType
{
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.cultivation.default");

    protected int maxQi = 1000;
    protected int Qi = 0;

    public boolean canCultivate(ResourceLocation element)
    {
        return true;
    };

    public int progressCultivation(Player player, int Qi, ResourceLocation element)
    {
        if (!canCultivate(element))
            return Qi;

        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        int max = getMaxQi();
        int currentQi = getQi();

        // If the current Qi is at the max, do nothing
        if (currentQi >= max)
            return Qi;

        int remains = currentQi + Qi - max;

        // If cultivating will go over the max Qi, set Qi to max and return the remaining amount
        if (remains > 0)
        {
            setQi(max);
            return remains;
        }

        // Add the cultivated Qi into the current Qi and return
        setQi(currentQi + Qi);
        return 0;
    }

    public int getMaxQi()
    {
        return maxQi;
    }

    public int getQi()
    {
        return Qi;
    }

    public void setQi(int newQi)
    {
        Qi = newQi;
    }

    public int getAbsorbRange(Player player)
    {
        return 1;
    }

    public int getAbsorbSpeed(Player player)
    {
        return 10;
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("QI", Qi);

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        Qi = nbt.getInt("QI");
    }
}
