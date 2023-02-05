package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Particles.*;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

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
