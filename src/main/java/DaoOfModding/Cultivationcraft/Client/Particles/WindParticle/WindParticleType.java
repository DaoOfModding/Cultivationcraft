package DaoOfModding.Cultivationcraft.Client.Particles.WindParticle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class WindParticleType extends ParticleType<WindParticleData>
{
    public WindParticleType()
    {
        super(true, null);
    }

    // What the HELL is this!?
    // I really don't know, it doesn't seem to get called, but it needs to be here...
    public Codec<WindParticleData> codec()
    {
        return null;
    }
}

