package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

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

    public Vector3d getTarget();
    public RayTraceResult.Type getTargetType();
    public UUID getTargetID();
    public boolean hasTarget(World world);
    public void setTarget(Vector3d pos, RayTraceResult.Type type, World targetWorld, UUID targetID);

    public StatModifier getModifier(String id);
    public HashMap<String, StatModifier> getModifiers();

    public boolean getRecall();
    public void setRecall(boolean recall);

    public void setDisconnected(boolean value);
    public boolean isDisconnected();
}
