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
    public double getFlyingItemSpeed();
    public void setFlyingItemSpeed(double newSpeed);

    public double getFlyingItemTurnSpeed();
    public void setFlyingItemTurnSpeed(double newSpeed);

    public double getFlyingItemMaxSpeed();
    public void setFlyingItemMaxSpeed(double newSpeed);

    public double getFlyingControlRange();
    public void setFlyingControlRange(double newRange);

    public int getCultivationType();
    public void setCultivationType(int newType);

    public Vec3 getTarget();
    public HitResult.Type getTargetType();
    public UUID getTargetID();
    public boolean hasTarget(Level world);
    public void setTarget(Vec3 pos, HitResult.Type type, Level targetWorld, UUID targetID);

    public StatModifier getModifier(String id);
    public HashMap<String, StatModifier> getModifiers();

    public boolean getRecall();
    public void setRecall(boolean recall);

    public void setDisconnected(boolean value);
    public boolean isDisconnected();

    public CompoundTag writeNBT();
    public void readNBT(CompoundTag nbt);
}
