package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class CultivatorStatsStorage implements Capability.IStorage<ICultivatorStats>
{
    @Override
    public INBT writeNBT(Capability<ICultivatorStats> capability, ICultivatorStats instance, Direction side)
    {

        CompoundNBT nbt = new CompoundNBT();
        nbt.putDouble("FIS", instance.getFlyingItemSpeed());
        nbt.putDouble("FITS", instance.getFlyingItemTurnSpeed());
        nbt.putDouble("FIMS", instance.getFlyingItemMaxSpeed());
        nbt.putDouble("FICR", instance.getFlyingControlRange());

        return nbt;
    }

    @Override
    public void readNBT(Capability<ICultivatorStats> capability, ICultivatorStats instance, Direction side, INBT nbt)
    {
        if (!(instance instanceof ICultivatorStats))
            throw new IllegalArgumentException("Tried to read Cultivator Stats from non CultivatorStats instance");

        instance.setFlyingItemSpeed(((CompoundNBT)nbt).getDouble("FIS"));
        instance.setFlyingItemTurnSpeed(((CompoundNBT)nbt).getDouble("FITS"));
        instance.setFlyingItemMaxSpeed(((CompoundNBT)nbt).getDouble("FIMS"));
        instance.setFlyingControlRange(((CompoundNBT)nbt).getDouble("FICR"));
    }
}
