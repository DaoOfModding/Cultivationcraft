package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind;

import java.util.UUID;

public interface IFlyingSwordBind
{
    public boolean isBound();
    public void setBound(boolean value);

    public long getBindTime();
    public void setBindTime(long newTime);
    public void setOwner(UUID newOwner);
    public UUID getOwner();

    public void setBindingPlayer(UUID newPlayer);
    public UUID getBindingPlayer();

    public long getBindTimeMax();
    public void setBindTimeMax(long newTime);
}
