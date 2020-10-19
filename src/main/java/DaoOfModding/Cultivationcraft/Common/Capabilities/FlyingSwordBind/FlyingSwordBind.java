package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind;

import net.minecraft.item.ItemStack;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FlyingSwordBind implements IFlyingSwordBind
{
    private long bindTimeMax = TimeUnit.SECONDS.toNanos(10);

    private boolean bound = false;

    private UUID owner = null;
    private UUID bindingPlayer = null;
    private long time = 0;

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
    public static IFlyingSwordBind getFlyingSwordBind(ItemStack item) {
        return item.getCapability(FlyingSwordBindCapability.FLYING_SWORD_BIND_CAPABILITY_CAPABILITY).orElse(null);
    }
}
