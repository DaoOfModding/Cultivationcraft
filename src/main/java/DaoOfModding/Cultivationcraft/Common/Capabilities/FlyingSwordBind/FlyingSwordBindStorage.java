package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class FlyingSwordBindStorage implements Capability.IStorage<IFlyingSwordBind>
{
    @Override
    public INBT writeNBT(Capability<IFlyingSwordBind> capability, IFlyingSwordBind instance, Direction side)
    {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putBoolean("Bound", instance.isBound());
        nbt.putLong("Time", instance.getBindTime());
        nbt.putLong("TimeMax", instance.getBindTimeMax());

        if (instance.getOwner() != null)
            nbt.putUniqueId("Owner", instance.getOwner());

        if (instance.getBindingPlayer() != null)
            nbt.putUniqueId("BindingPlayer", instance.getBindingPlayer());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IFlyingSwordBind> capability, IFlyingSwordBind instance, Direction side, INBT nbt)
    {
        if (!(instance instanceof IFlyingSwordBind))
            throw new IllegalArgumentException("Tried to read FlyingSwordBind from non FlyingSwordBind instance");

        instance.setBound(((CompoundNBT) nbt).getBoolean("Bound"));
        instance.setBindTime(((CompoundNBT) nbt).getLong("Time"));
        instance.setBindTimeMax(((CompoundNBT) nbt).getLong("TimeMax"));

        if (((CompoundNBT) nbt).contains("Owner"))
            instance.setOwner(((CompoundNBT) nbt).getUniqueId("Owner"));
        else
            instance.setOwner(null);

        if (((CompoundNBT) nbt).contains("BindingPlayer"))
            instance.setBindingPlayer(((CompoundNBT) nbt).getUniqueId("BindingPlayer"));
        else
            instance.setBindingPlayer(null);
    }
}
