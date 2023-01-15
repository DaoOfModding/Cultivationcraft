package DaoOfModding.Cultivationcraft.Client.Particles;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Client.clientRegister;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;

public class QiParticleData implements ParticleOptions
{
    QiSource source;
    Player target;

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

    public void setTarget(Player player)
    {
        target = player;
    }
}
