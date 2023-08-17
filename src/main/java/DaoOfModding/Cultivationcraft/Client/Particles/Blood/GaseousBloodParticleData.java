package DaoOfModding.Cultivationcraft.Client.Particles.Blood;

import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class GaseousBloodParticleData implements ParticleOptions
{
    Player sourcePlayer;

    public GaseousBloodParticleData(Player source)
    {
        sourcePlayer = source;
    }

    @Override
    public ParticleType<GaseousBloodParticleData> getType()
    {
        return Register.gaseousBloodParticleType.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf)
    {
    }

    @Override
    public String writeToString()
    {
        return "Blood from: " + sourcePlayer.getDisplayName();
    }
}
