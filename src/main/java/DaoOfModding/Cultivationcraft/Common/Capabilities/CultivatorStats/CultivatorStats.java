package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Misc;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
    protected int cultivationLevel = 0;
    protected int cultivationStage = 0;
    protected int qi = 0;

    protected boolean disconnected = false;

    public int getCultivationType()
    {
        return cultivationType;
    }

    public void setCultivationType(int newType)
    {
        cultivationType = newType;
    }

    public int getCultivationLevel()
    {
        return cultivationLevel;
    }

    public void setCultivationLevel(int newLevel)
    {
        cultivationLevel = newLevel;
    }

    public int getCultivationStage()
    {
        return cultivationStage;
    }

    public void setCultivationStage(int newStage)
    {
        cultivationStage = newStage;
    }

    public int getQi()
    {
        return qi;
    }

    public void setQi(int newQi)
    {
        qi = newQi;
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
        cultivationLevel = 0;
        cultivationStage = 0;
        qi = 0;    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("TYPE", getCultivationType());
        nbt.putInt("LEVEL", getCultivationLevel());
        nbt.putInt("STAGE", getCultivationStage());
        nbt.putInt("QI", getQi());

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        setCultivationType(nbt.getInt("TYPE"));
        setCultivationLevel(nbt.getInt("LEVEL"));
        setCultivationStage(nbt.getInt("STAGE"));
        setQi(nbt.getInt("QI"));
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


