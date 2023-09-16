package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Misc;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.DefaultCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CultivatorStats implements ICultivatorStats
{
    protected int cultivationType = CultivationTypes.NO_CULTIVATION;
    protected CultivationType cultivation = new DefaultCultivation();

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

    public void setDisconnected(boolean value) {
        disconnected = value;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void reset()
    {
        cultivationType = CultivationTypes.NO_CULTIVATION;
        cultivation = new DefaultCultivation();
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("CULTIVATIONID", cultivation.ID.toString());
        nbt.put("CULTIVATION", cultivation.writeNBT());

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        setCultivationType(nbt.getInt("TYPE"));

        CultivationType newCultivation = ExternalCultivationHandler.getCultivation(new ResourceLocation(nbt.getString("CULTIVATIONID")));
        newCultivation.readNBT(nbt.getCompound("CULTIVATION"));

        cultivation = newCultivation;
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


