package DaoOfModding.Cultivationcraft.Client.Particles.WindParticle;

import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class WindParticleData implements ParticleOptions
{
    Vec3 pos;
    Entity target;

    public WindParticleData(Vec3 position, Entity targetEntity)
    {
        pos = position;
        target = targetEntity;
    }

    @Override
    public ParticleType<WindParticleData> getType()
    {
        return Register.windParticleType.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf)
    {
    }

    @Override
    public String writeToString()
    {
        return "Water";
    }
}
