package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.CultivationTypeScreen;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultCultivationStatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CultivationType
{
    protected String ID = "cultivationcraft.cultivation.default";

    protected int techLevel = 0;
    protected int maxedTechsToBreakthrough = 0;

    protected int stage = 1;
    protected int maxStage = 1;

    protected CultivationType previousCultivation = null;
    protected Tribulation tribulation = new Tribulation(maxStage, 100, 1);

    protected boolean isTribulating = false;
    protected boolean hasTribulated = false;

    protected HashMap<String, HashMap<ResourceLocation, Double>> statLevels = new HashMap<>();

    protected PassiveTechnique passive = new PassiveTechnique();

    protected CultivationTypeScreen screen = new CultivationTypeScreen();

    protected ArrayList<CultivationType> advancements = new ArrayList<>();

    public CultivationType(int currentStage)
    {
        stage = currentStage;
        stageCalculations();
    }

    // Put anything here that changes based on the stage
    public void stageCalculations()
    {

    }

    public String getName()
    {
        return Component.translatable(ID).getString();
    }

    public String getID()
    {
        return ID;
    }

    public boolean canCultivate(ResourceLocation element)
    {
        return true;
    }

    public boolean consumeQi(Player player, double qiToConsume)
    {
        return StaminaHandler.consumeStamina(player, (float)qiToConsume);
    }

    public boolean canBreakthrough(Player player)
    {
        if (getTechLevelProgressWithoutPrevious(passive.getClass().toString()) >= techLevel)
            if (getMaxedTechs() >= maxedTechsToBreakthrough)
                return true;

        return false;
    }

    public ArrayList<CultivationType> getAdvancements()
    {
        return advancements;
    }

    public void advance(Player player, String advancement)
    {
        CultivationType advanceTo = null;

        for (CultivationType advance : advancements)
            if (advance.getID().compareTo(advancement) == 0)
                advanceTo = advance;

        if (advanceTo == null)
        {
            Cultivationcraft.LOGGER.error("Tried to advance to unavailable cultivation type " + advancement);
            return;
        }

        advanceTo.setPreviousCultivation(this);
        CultivatorStats.getCultivatorStats(player).setCultivation(advanceTo);

        tribulation.reset();
    }

    public boolean hasTribulated()
    {
        return hasTribulated;
    }

    public boolean hasTribulation(Player player)
    {
        if (stage == maxStage && canBreakthrough(player))
            return true;

        return false;
    }

    public void tick(Player player)
    {
        if (player.level.isClientSide)
            return;

        if (isTribulating() && player.isAlive())
            if (tribulation.tick(player))
            {
                isTribulating = false;
                tribulationComplete(player);
                PacketHandler.sendCultivatorStatsToClient(player);
            }
    }

    public int getStage()
    {
        return stage;
    }

    public int getMaxStage()
    {
        return maxStage;
    }

    public void startTribulation()
    {
        isTribulating = true;
    }

    public boolean isTribulating()
    {
        return isTribulating;
    }

    public void breakthrough(Player player)
    {
    }

    public void tribulationComplete(Player player)
    {
        hasTribulated = true;
    }

    public CultivationTypeScreen getScreen()
    {
        return screen;
    }

    public String breakthroughProgress(Player player)
    {
        String progress = Component.translatable("cultivationcraft.gui.cultivationprogress").getString() + ": " + getTechLevelProgressWithoutPrevious(passive.getClass().toString()) + "/" + techLevel;

        if (maxedTechsToBreakthrough > 0)
            progress += "\n" + Component.translatable("cultivationcraft.gui.completedtech").getString() + ": " + getMaxedTechs() + "/" + maxedTechsToBreakthrough;

        if (hasTribulation(player))
            progress += "\n" + Component.translatable("cultivationcraft.gui.tribulationpending").getString();

        return progress;
    }

    public int getMaxedTechs()
    {
        int i = 0;

        for (String tech : statLevels.keySet())
            if (passive.getClass().toString().compareTo(tech) != 0 && getTechLevelProgress(tech) >= getMaxTechLevel())
                i++;

        return i;
    }

    public PassiveTechnique getPassive()
    {
        return passive;
    }

    public double getCultivationStat(Player player, ResourceLocation stat)
    {
        double amount = 0;

        if (getPassive().hasTechniqueStat(stat))
            amount += getPassive().getTechniqueStat(stat, player);

        return amount;
    }

    public void reset(Player player)
    {
        statLevels = new HashMap<>();
    }

    public float absorbFromQiSource(int amount, Player player)
    {
        List<QiSource> sources = ChunkQiSources.getQiSourcesInRange(player.level, player.position(), (int)getCultivationStat(player, DefaultCultivationStatIDs.qiAbsorbRange));

        int remaining = amount;
        float toAbsorb = 0;

        // Draw Qi from each Qi source available
        for (QiSource source : sources)
        {
            // Only absorb from QiSources of the correct element
            if (canCultivate(source.getElement()))
                if (remaining > 0)
                {
                    int absorbed = source.absorbQi(remaining, player);
                    remaining -= absorbed;

                    toAbsorb += absorbed;
                }
        }

        if (toAbsorb > amount)
            return amount;

        if (toAbsorb < 0)
            return 0;

        return toAbsorb;
    }

    public float absorbFromQiSource(int amount, Player player, QiSource source)
    {
        float toAbsorb = 0;

        toAbsorb = 0;

        // Only absorb from QiSources of the correct element
        if (canCultivate(source.getElement()))
            toAbsorb = source.absorbQi(amount, player);

        if (toAbsorb > amount)
            return amount;

        if (toAbsorb < 0)
            return 0;

        return toAbsorb;
    }

    public float progressCultivation(Player player, float Qi, ResourceLocation element)
    {
        if (!canCultivate(element))
            return Qi;

        if (Qi < 0)
            return 0;

        int currentLevel = getTechLevelProgressWithoutPrevious(getPassive().getClass().toString());

        // Don't cultivate if already at the max
        if (currentLevel >= techLevel)
            return Qi;

        float remains = (currentLevel + Qi) - techLevel;

        // If cultivating will go over the max, cultivate to the max instead
        if (remains > 0)
        {
            getPassive().levelUp(player, remains);
            return Qi - remains;
        }

        // Cultivate all the supplied Qi
        getPassive().levelUp(player, Qi);
        return 0;
    }

    public CultivationType getPreviousCultivation()
    {
        return previousCultivation;
    }

    public Double getStatLevelWithoutPrevious(Class tech, ResourceLocation stat)
    {
        Double level = 0.0;

        if (statLevels.containsKey(tech.toString()) && statLevels.get(tech.toString()).containsKey(stat))
            level += statLevels.get(tech.toString()).get(stat);

        return level;
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

    public int getMaxTechLevelWithoutPrevious()
    {
        return techLevel;
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
        return getTechLevelProgress(tech.toString());
    }

    public int getTechLevelProgressWithoutPrevious(String tech)
    {
        int progress = 0;

        if (statLevels.containsKey(tech))
        {
            for (double value : statLevels.get(tech).values())
                progress += (int)value;
        }

        return progress;
    }

    public int getTechLevelProgress(String tech)
    {
        int progress = 0;

        if (statLevels.containsKey(tech))
        {
            for (double value : statLevels.get(tech).values())
                progress += (int)value;
        }

        if (previousCultivation != null)
            progress += previousCultivation.getTechLevelProgress(tech);

        return progress;
    }

    public void levelTech(Technique tech, double amount, Player player)
    {
        int max = getMaxTechLevel();
        int current = getTechLevelProgress(tech.getClass());

        if (max == current)
            return;

        double amountToLevel = max - current;

        if (amount > amountToLevel)
            amount = amountToLevel;

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

        if (getPreviousCultivation() != null)
        {
            nbt.putString("PREVIOUSCULTNAME", getPreviousCultivation().getClass().toString());
            nbt.put("PREVIOUSCULT", getPreviousCultivation().writeNBT());
        }

        nbt.putInt("STAGE", getStage());
        nbt.putBoolean("TRIBULATING", isTribulating);
        nbt.putBoolean("TRIBULATED", hasTribulated);

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

    public void setPreviousCultivation(CultivationType previous)
    {
        previousCultivation = previous;
    }


    public void readNBT(CompoundTag nbt)
    {
        if (nbt.contains("PREVIOUSCULTNAME"))
        {
            CultivationType newCultivation = ExternalCultivationHandler.getCultivation(nbt.getString("PREVIOUSCULTNAME"));
            newCultivation.readNBT(nbt.getCompound("PREVIOUSCULT"));

            previousCultivation = newCultivation;
        }

        stage = nbt.getInt("STAGE");
        stageCalculations();

        isTribulating = nbt.getBoolean("TRIBULATING");
        hasTribulated = nbt.getBoolean("TRIBULATED");

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
