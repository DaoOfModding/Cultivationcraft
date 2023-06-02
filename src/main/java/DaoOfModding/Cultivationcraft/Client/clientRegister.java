package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Particles.*;
import DaoOfModding.Cultivationcraft.Client.Particles.Blood.BloodParticle;
import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class clientRegister
{
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onParticleFactoryRegistration(RegisterParticleProvidersEvent event)
        {
            event.register(Register.qiParticleType.get(), QiParticle.Factory::new);
            event.register(Register.bloodParticleType.get(), BloodParticle.Factory::new);
        }
    }
}
