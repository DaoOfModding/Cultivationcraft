package DaoOfModding.Cultivationcraft.Client.Particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class QiParticleType extends ParticleType<QiParticleData>
{

    public QiParticleType()
    {
        super(true, null);
    }

    // What the HELL is this!?
    // I really don't know, it doesn't seem to get called, but it needs to be here...
    public Codec<QiParticleData> codec()
    {
        return null;
    }
}
