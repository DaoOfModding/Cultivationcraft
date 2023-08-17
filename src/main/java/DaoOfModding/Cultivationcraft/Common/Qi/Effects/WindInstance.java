package DaoOfModding.Cultivationcraft.Common.Qi.Effects;

import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WindInstance
{
    protected static final int MaxLife = 40;

    protected Vec3 dir;
    protected float momentum;
    protected float power;
    protected int life = MaxLife;

    public WindInstance(Vec3 direction, float speed, float strength)
    {
        dir = direction;
        momentum = speed;
        power = strength;
    }

    public Vec3 getDirection()
    {
        return dir;
    }

    public float getSpeed()
    {
        return momentum;
    }

    public float getStrength()
    {
        return power;
    }

    public void tick(Entity entity)
    {
        float weight;

        if (entity instanceof Player)
        {
            weight = BodyPartStatControl.getPlayerStatControl((Player)entity).getStats().getStat(StatIDs.weight);
        }
        else
        {
            weight = (float) entity.getBoundingBox().getSize();
        }

        float speed = momentum;

        if (weight >= power)
            speed *= Math.max(0f, 2 - weight / power);

        entity.setDeltaMovement(entity.getDeltaMovement().add(dir.scale(speed)));

        // Reduce momentum of the wind
        momentum *= 0.98;

        life--;
    }

    public boolean isDone()
    {
        if (life > 0)
            return false;

        return true;
    }
}
