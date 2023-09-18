package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class CultivationType
{
    public static final ResourceLocation ID = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.cultivation.default");

    protected int maxQi = 1000;
    protected int Qi = 0;

    protected int techLevel = 100;

    protected CultivationType previousCultivation = null;

    protected HashMap<String, HashMap<ResourceLocation, Double>> statLevels = new HashMap<>();

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

    public CultivationType getPreviousCultivation()
    {
        return previousCultivation;
    }

    public Double getStatLevel(Class tech, ResourceLocation stat)
    {
        Double level = 0.0;

        if (statLevels.containsKey(tech.toString()) && statLevels.get(tech.toString()).containsKey(stat))
            level += statLevels.get(tech.toString()).get(stat);

        if (previousCultivation != null)
            level += previousCultivation.getStatLevel(tech, stat);

        return level;
    }

    public int getMaxTechLevel()
    {
        int level = techLevel;

        if (previousCultivation != null)
            level += getPreviousCultivation().getMaxTechLevel();

        return level;
    }

    public int getTechLevelProgress(Class tech)
    {
        int progress = 0;

        if (statLevels.containsKey(tech.toString()))
        {
            for (double value : statLevels.get(tech.toString()).values())
                progress += (int)value;
        }

        if (previousCultivation != null)
            progress += previousCultivation.getTechLevelProgress(tech);

        return progress;
    }

    public int getMaxQi()
    {
        int returnMax = maxQi;

        if (previousCultivation != null)
            returnMax += previousCultivation.getMaxQi();

        return returnMax;
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

    public void levelTech(Technique tech, double amount, Player player)
    {
        int max = getMaxTechLevel();
        int current = getTechLevelProgress(tech.getClass());

        if (max == current)
            return;

        if (current + (int)amount >= max)
        {
            amount -= max - (current + (int) amount);
            amount = (int)amount;
        }

        ResourceLocation toLevel = CultivatorStats.getCultivatorStats(player).getTechniqueFocus(tech.getClass());

        if (toLevel == null)
            toLevel = (ResourceLocation) tech.getTechniqueStats().toArray()[0];

        double currentLevel = amount;

        if (!statLevels.containsKey(tech.getClass().toString()))
            statLevels.put(tech.getClass().toString(), new HashMap<>());

        if (statLevels.get(tech.getClass().toString()).containsKey(toLevel))
            currentLevel += statLevels.get(tech.getClass().toString()).get(toLevel);

        statLevels.get(tech.getClass().toString()).put(toLevel, currentLevel);

        PacketHandler.sendCultivatorStatsToClient(player);
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("QI", Qi);

        if (getPreviousCultivation() != null)
        {
            nbt.putString("PREVIOUSCULTNAME", getPreviousCultivation().ID.toString());
            nbt.put("PREVIOUSCULT", getPreviousCultivation().writeNBT());
        }

        int i = 0;

        for (Map.Entry<String, HashMap<ResourceLocation, Double>> tech : statLevels.entrySet())
        {
            nbt.putString("TECH"+i+"CLASS", tech.getKey());

            int j = 0;

            for (Map.Entry<ResourceLocation, Double> techEntry : tech.getValue().entrySet())
            {
                nbt.putString("TECH"+i+"ENTRY"+j+"NAME", techEntry.getKey().toString());
                nbt.putDouble("TECH"+i+"ENTRY"+j+"VALUE", techEntry.getValue());

                j++;
            }

            i++;
        }

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        Qi = nbt.getInt("QI");

        if (nbt.contains("PREVIOUSCULTNAME"))
        {
            CultivationType newCultivation = ExternalCultivationHandler.getCultivation(new ResourceLocation(nbt.getString("PREVIOUSCULTNAME")));
            newCultivation.readNBT(nbt.getCompound("PREVIOUSCULT"));

            previousCultivation = newCultivation;
        }

        HashMap<String, HashMap<ResourceLocation, Double>> newTechLevels = new HashMap<>();

        int i = 0;

        while (nbt.contains("TECH"+i+"CLASS"))
        {
            int j = 0;

            HashMap<ResourceLocation, Double> values = new HashMap<>();

            while (nbt.contains("TECH"+i+"ENTRY"+j+"NAME"))
            {
                values.put(new ResourceLocation(nbt.getString("TECH"+i+"ENTRY"+j+"NAME")), nbt.getDouble("TECH"+i+"ENTRY"+j+"VALUE"));
                j++;
            }

            newTechLevels.put(nbt.getString("TECH"+i+"CLASS"), values);

            i++;
        }

        statLevels = newTechLevels;
    }
}
