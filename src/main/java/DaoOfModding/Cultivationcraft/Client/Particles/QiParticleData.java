package DaoOfModding.Cultivationcraft.Client.Particles;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Client.clientRegister;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;

public class QiParticleData implements ParticleOptions
{
    QiSource source;

    public QiParticleData(QiSource newSource)
    {
        source = newSource;
    }

    @Override
    public ParticleType<QiParticleData> getType()
    {
        return clientRegister.qiParticleType;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf)
    {
    }

    @Override
    public String writeToString()
    {
        return "Source: " + source.toString();
    }
}
