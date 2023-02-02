package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Particles.*;
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
    public static ParticleType<QiParticleData> qiParticleType = new QiParticleType();
    public static ParticleType<BloodParticleData> bloodParticleType = new BloodParticleType();

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void register(RegisterEvent event)
        {
            event.register(ForgeRegistries.Keys.PARTICLE_TYPES, helper -> helper.register(new ResourceLocation(Cultivationcraft.MODID, "qiparticle"), qiParticleType));
            event.register(ForgeRegistries.Keys.PARTICLE_TYPES, helper -> helper.register(new ResourceLocation(Cultivationcraft.MODID, "bloodparticle"), bloodParticleType));
        }

        @SubscribeEvent
        public static void onParticleFactoryRegistration(RegisterParticleProvidersEvent event)
        {
            event.register(qiParticleType, QiParticle.Factory::new);
            event.register(bloodParticleType, BloodParticle.Factory::new);
        }
    }
}
