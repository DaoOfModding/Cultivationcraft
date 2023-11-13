package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FlyingSwordBind implements IFlyingSwordBind
{
    protected long bindTimeMax = TimeUnit.SECONDS.toNanos(10);

    protected boolean bound = false;

    protected UUID owner = null;
    protected UUID bindingPlayer = null;
    protected long time = 0;

    public boolean isBound()
    {
        return bound;
    }

    public void setBound(boolean value)
    {
        bound = value;
    }

    public long getBindTime()
    {
        return time;
    }

    public void setBindTime(long newTime)
    {
        time = newTime;
    }

    public void setOwner(UUID newOwner)
    {
        owner = newOwner;
    }

    public UUID getOwner()
    {
        return owner;
    }

    public void setBindingPlayer(UUID newPlayer)
    {
        bindingPlayer = newPlayer;
    }

    public UUID getBindingPlayer()
    {
        return bindingPlayer;
    }

    public long getBindTimeMax()
    {
        return bindTimeMax;
    }

    public void setBindTimeMax(long newTime)
    {
        bindTimeMax = newTime;
    }

    // Return a specified item's FlyingSwordBind
    public static IFlyingSwordBind getFlyingSwordBind(ItemStack item)
    {
        return item.getCapability(FlyingSwordBindCapability.INSTANCE).orElse(null);
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        nbt.putBoolean("Bound", isBound());
        nbt.putLong("Time", getBindTime());
        nbt.putLong("TimeMax", getBindTimeMax());

        if (getOwner() != null)
            nbt.putUUID("Owner", getOwner());

        if (getBindingPlayer() != null)
            nbt.putUUID("BindingPlayer", getBindingPlayer());

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        setBound(nbt.getBoolean("Bound"));
        setBindTime(nbt.getLong("Time"));
        setBindTimeMax(nbt.getLong("TimeMax"));

        if (nbt.contains("Owner"))
            setOwner(nbt.getUUID("Owner"));
        else
            setOwner(null);

        if (nbt.contains("BindingPlayer"))
            setBindingPlayer(nbt.getUUID("BindingPlayer"));
        else
            setBindingPlayer(null);
    }
}
