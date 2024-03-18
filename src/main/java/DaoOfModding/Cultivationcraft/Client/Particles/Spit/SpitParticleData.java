package DaoOfModding.Cultivationcraft.Client.Particles.Spit;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;
import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class SpitParticleData  implements ParticleOptions
{
    Breath type;

    public SpitParticleData(Breath newBreath)
    {
        type = newBreath;
    }

    @Override
    public ParticleType<SpitParticleData> getType()
    {
        return Register.spitParticleType.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf)
    {
    }

    @Override
    public String writeToString()
    {
        return "Spit";
    }
}
