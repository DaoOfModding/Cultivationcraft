package DaoOfModding.Cultivationcraft.Client.Particles;

import DaoOfModding.Cultivationcraft.Client.clientRegister;
import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
