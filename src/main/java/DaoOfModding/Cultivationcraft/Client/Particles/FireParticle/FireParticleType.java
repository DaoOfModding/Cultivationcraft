package DaoOfModding.Cultivationcraft.Client.Particles.FireParticle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class FireParticleType extends ParticleType<FireParticleData>
{
    public FireParticleType()
    {
        super(true, null);
    }

    // What the HELL is this!?
    // I really don't know, it doesn't seem to get called, but it needs to be here...
    public Codec<FireParticleData> codec()
    {
        return null;
    }
}