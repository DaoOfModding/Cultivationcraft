package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Client.Particles.BloodParticleType;
import DaoOfModding.Cultivationcraft.Client.Particles.QiParticleType;
import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainer;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class Register
{
    public enum keyPresses { FLYINGSWORDSCREEN, SKILLHOTBARSWITCH }

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Cultivationcraft.MODID);
    public static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Cultivationcraft.MODID);
    public static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Cultivationcraft.MODID);

    public static RegistryObject<EntityType<FlyingSwordEntity>> FLYINGSWORD = ENTITY_TYPES.register("flyingsword", () ->
                                                                                EntityType.Builder.<FlyingSwordEntity>of(FlyingSwordEntity::new, MobCategory.MISC)
                                                                                        .sized(0.5f, 0.5f)
                                                                                        .setUpdateInterval(3)
                                                                                        .build("flyingsword"));

    public static RegistryObject<MenuType<FlyingSwordContainer>> ContainerTypeFlyingSword = CONTAINERS.register("flyingsword", () -> IForgeMenuType.create(FlyingSwordContainer::createContainerClientSide));

    public static RegistryObject<QiParticleType> qiParticleType = PARTICLES.register("qiparticle", () -> new QiParticleType());
    public static RegistryObject<BloodParticleType> bloodParticleType = PARTICLES.register("bloodparticle", () -> new BloodParticleType());

    public static void init(IEventBus bus)
    {
        ENTITY_TYPES.register(bus);
        CONTAINERS.register(bus);
        PARTICLES.register(bus);
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
        {
            event.registerEntityRenderer(Register.FLYINGSWORD.get(), FlyingSwordRenderer::new);

            // ClientBlockRegister.registerBlockRenderers();
        }

        @SubscribeEvent
        public static void register(RegisterEvent event)
        {
            event.register(ForgeRegistries.Keys.MENU_TYPES, helper -> helper.register("flyingswordcontainer", IForgeMenuType.create(FlyingSwordContainer::createContainerClientSide)));
        }
    }
}
