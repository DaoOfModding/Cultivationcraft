package DaoOfModding.Cultivationcraft.Client.Particles.Blood;

import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class BloodParticleData implements ParticleOptions
{
    Player sourcePlayer;

    public BloodParticleData(Player source)
    {
        sourcePlayer = source;
    }

    @Override
    public ParticleType<BloodParticleData> getType()
    {
        return Register.bloodParticleType.get();
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
