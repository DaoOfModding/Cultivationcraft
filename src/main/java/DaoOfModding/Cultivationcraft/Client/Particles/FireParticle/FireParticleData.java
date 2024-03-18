package DaoOfModding.Cultivationcraft.Client.Particles.FireParticle;

import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class FireParticleData implements ParticleOptions
{
    Vec3 pos;
    Entity target;

    public FireParticleData(Vec3 position, Entity targetEntity)
    {
        pos = position;
        target = targetEntity;
    }

    @Override
    public ParticleType<DaoOfModding.Cultivationcraft.Client.Particles.FireParticle.FireParticleData> getType()
    {
        return Register.fireParticleType.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf)
    {
    }

    @Override
    public String writeToString()
    {
        return "Fire";
    }
}
