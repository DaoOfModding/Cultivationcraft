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

    protected boolean disconnected = false;

    protected HashMap<String, StatModifier> modifiers = new HashMap<String, StatModifier>();

    public int getCultivationType()
    {
        return cultivationType;
    }

    public void setCultivationType(int newType)
    {
        cultivationType = newType;
    }

    public StatModifier getModifier(String id)
    {
        if (!modifiers.containsKey(id))
            modifiers.put(id, new StatModifier(id));

        return modifiers.get(id);
    }

    public HashMap<String, StatModifier> getModifiers()
    {
        return modifiers;
    }

    public void setDisconnected(boolean value) {
        disconnected = value;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("TYPE", getCultivationType());

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        setCultivationType(nbt.getInt("TYPE"));
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


