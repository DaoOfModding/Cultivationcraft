package DaoOfModding.Cultivationcraft.Client.Particles;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class QiParticleData implements IParticleData
{
    QiSource source;

    public QiParticleData(QiSource newSource)
    {
        source = newSource;
    }

    @Override
    public ParticleType<QiParticleData> getType()
    {
        return Register.qiParticleType;
    }

    @Override
    public void write(PacketBuffer buf)
    {
    }

    @Override
    public String getParameters()
    {
        return "Source: " + source.toString();
    }
}
