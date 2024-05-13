package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.FoundationEstablishmentCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.NoCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class CultivatorStats implements ICultivatorStats
{
    protected int cultivationType = CultivationTypes.NO_CULTIVATION;
    protected CultivationType cultivation = new NoCultivation();
    protected HashMap<String, ResourceLocation> techFocus = new HashMap<>();
    protected HashMap<ResourceLocation, Double> conceptProgress = new HashMap<>();

    protected boolean disconnected = false;

    public int getCultivationType()
    {
        return cultivationType;
    }

    public void setCultivationType(int newType)
    {
        cultivationType = newType;
    }

    public CultivationType getCultivation()
    {
        return cultivation;
    }

    public void setCultivation(CultivationType newCultivation)
    {
        cultivation = newCultivation;
    }

    public ResourceLocation getTechniqueFocus(Class tech)
    {
        return techFocus.get(tech.toString());
    }

    public void setTechniqueFocus(String tech, ResourceLocation focus)
    {
        techFocus.put(tech, focus);
    }

    public double getConceptProgress(ResourceLocation ID)
    {
        if (!conceptProgress.containsKey(ID))
            conceptProgress.put(ID, 0.0);

        return conceptProgress.get(ID);
    }

    public void setConceptProgress(ResourceLocation ID, double amount)
    {
        conceptProgress.put(ID, amount);
    }

    public void setDisconnected(boolean value) {
        disconnected = value;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void reset()
    {
        cultivationType = CultivationTypes.NO_CULTIVATION;
        cultivation = new NoCultivation();
        techFocus = new HashMap<>();
        conceptProgress = new HashMap<>();
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("TYPE", cultivationType);
        nbt.putString("CULTIVATIONID", cultivation.getClass().toString());
        nbt.put("CULTIVATION", cultivation.writeNBT());

        int i = 0;

        for (Map.Entry<String, ResourceLocation> techEntry : techFocus.entrySet())
        {
            nbt.putString("TECH"+i+"NAME", techEntry.getKey());
            nbt.putString("TECH"+i+"VALUE", techEntry.getValue().toString());

            i++;
        }

        i = 0;

        for (Map.Entry<ResourceLocation, Double> entry : conceptProgress.entrySet())
        {
            nbt.putString("CONCEPT"+i+"ID", entry.getKey().toString());
            nbt.putDouble("CONCEPT"+i+"VALUE", entry.getValue());

            i++;
        }


        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        setCultivationType(nbt.getInt("TYPE"));

        CultivationType newCultivation = ExternalCultivationHandler.getCultivation(nbt.getString("CULTIVATIONID"));
        newCultivation.readNBT(nbt.getCompound("CULTIVATION"));

        HashMap<String, ResourceLocation> newTechFocus = new HashMap<>();

        int i = 0;
        while (nbt.contains("TECH"+i+"NAME"))
        {
            newTechFocus.put(nbt.getString("TECH"+i+"NAME"), new ResourceLocation(nbt.getString("TECH"+i+"VALUE")));
            i++;
        }

        HashMap<ResourceLocation, Double> newConceptFocus = new HashMap<>();

        i = 0;
        while (nbt.contains("CONCEPT"+i+"ID"))
        {
            newConceptFocus.put(new ResourceLocation(nbt.getString("CONCEPT"+i+"ID")), nbt.getDouble("CONCEPT"+i+"VALUE"));
            i++;
        }

        techFocus = newTechFocus;
        cultivation = newCultivation;
        conceptProgress = newConceptFocus;
    }

    public static boolean isCultivator(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        // Do nothing if not a cultivator
        if (stats.getCultivationType() == CultivationTypes.NO_CULTIVATION)
            return false;

        return true;
    }

    // Return a specified players CultivatorStats
    public static ICultivatorStats getCultivatorStats(Player player) {
        return player.getCapability(CultivatorStatsCapability.INSTANCE).orElseThrow(() -> new IllegalArgumentException("getting cultivator stats"));
    }
}


