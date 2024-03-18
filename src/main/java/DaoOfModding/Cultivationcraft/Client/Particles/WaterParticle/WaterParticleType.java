package DaoOfModding.Cultivationcraft.Client.Particles.WaterParticle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class WaterParticleType extends ParticleType<WaterParticleData>
{
    public WaterParticleType()
    {
        super(true, null);
    }

    // What the HELL is this!?
    // I really don't know, it doesn't seem to get called, but it needs to be here...
    public Codec<WaterParticleData> codec()
    {
        return null;
    }
}

