package DaoOfModding.Cultivationcraft.Client.Particles.Blood;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class GaseousBloodParticleType extends ParticleType<GaseousBloodParticleData>
{
    public GaseousBloodParticleType()
    {
        super(true, null);
    }

    // What the HELL is this!?
    // I really don't know, it doesn't seem to get called, but it needs to be here...
    public Codec<GaseousBloodParticleData> codec()
    {
        return null;
    }
}
