package DaoOfModding.Cultivationcraft.Client.Particles.WaterParticle;

import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class WaterParticleData implements ParticleOptions
{
    Vec3 pos;
    Entity target;

    public WaterParticleData(Vec3 position, Entity targetEntity)
    {
        pos = position;
        target = targetEntity;
    }

    @Override
    public ParticleType<WaterParticleData> getType()
    {
        return Register.waterParticleType.get();
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
