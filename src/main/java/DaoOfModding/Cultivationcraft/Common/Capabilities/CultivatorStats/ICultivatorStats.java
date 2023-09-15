package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.HashMap;
import java.util.UUID;

public interface ICultivatorStats
{
    public int getCultivationType();
    public void setCultivationType(int newType);
    public int getCultivationLevel();
    public void setCultivationLevel(int newLevel);
    public int getCultivationStage();
    public void setCultivationStage(int newStage);
    public int getQi();
    public void setQi(int newQi);

    public void reset();

    public void setDisconnected(boolean value);
    public boolean isDisconnected();

    public CompoundTag writeNBT();
    public void readNBT(CompoundTag nbt);
}
