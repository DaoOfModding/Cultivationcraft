package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Client.Particles.Blood.BloodParticleType;
import DaoOfModding.Cultivationcraft.Client.Particles.Blood.GaseousBloodParticleType;
import DaoOfModding.Cultivationcraft.Client.Particles.FireParticle.FireParticleType;
import DaoOfModding.Cultivationcraft.Client.Particles.Spit.SpitParticleType;
import DaoOfModding.Cultivationcraft.Client.Particles.WaterParticle.WaterParticleType;
import DaoOfModding.Cultivationcraft.Client.Particles.WindParticle.WindParticleType;
import DaoOfModding.Cultivationcraft.Client.Renderers.FlyingSwordRenderer;
import DaoOfModding.Cultivationcraft.Client.Renderers.QiProjectileRenderer;
import DaoOfModding.Cultivationcraft.Client.Textures.AlphaOverlayTexture;
import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainer;
import DaoOfModding.Cultivationcraft.Common.Qi.QiProjectile;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

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

    public static RegistryObject<EntityType<QiProjectile>> QIPROJECTILE = ENTITY_TYPES.register("qiprojectile", () ->
            EntityType.Builder.<QiProjectile>of(QiProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(4)
                    .updateInterval(3)
                    .build("qiprojectile"));

    public static final RegistryObject<MenuType<FlyingSwordContainer>> ContainerTypeFlyingSword = CONTAINERS.register("flyingsword", () -> IForgeMenuType.create(FlyingSwordContainer::createContainerClientSide));

    public static final RegistryObject<SimpleParticleType> qiParticleType = PARTICLES.register("qiparticle", () -> new SimpleParticleType(true));
    public static final RegistryObject<BloodParticleType> bloodParticleType = PARTICLES.register("bloodparticle", () -> new BloodParticleType());
    public static final RegistryObject<GaseousBloodParticleType> gaseousBloodParticleType = PARTICLES.register("gaseousbloodparticle", () -> new GaseousBloodParticleType());
    public static final RegistryObject<SpitParticleType> spitParticleType = PARTICLES.register("spitparticle", () -> new SpitParticleType());
    public static final RegistryObject<WaterParticleType> waterParticleType = PARTICLES.register("waterparticle", () -> new WaterParticleType());
    public static final RegistryObject<WindParticleType> windParticleType = PARTICLES.register("windparticle", () -> new WindParticleType());
    public static final RegistryObject<FireParticleType> fireParticleType = PARTICLES.register("fireparticle", () -> new FireParticleType());

    // Override vanilla lava to make it swimmable and drownable
    private static final DeferredRegister<FluidType> VANILLA_FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, "minecraft");
    public static final RegistryObject<FluidType> LAVA_TYPE = VANILLA_FLUID_TYPES.register("lava", () ->
            new FluidType(FluidType.Properties.create()
                    .descriptionId("block.minecraft.lava")
                    .canSwim(true)
                    .canDrown(true)
                    .pathType(BlockPathTypes.LAVA)
                    .adjacentPathType(null)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                    .lightLevel(15))
            {
                @Override
                public double motionScale(Entity entity)
                {
                    return entity.level.dimensionType().ultraWarm() ? 0.007D : 0.0023333333333333335D;
                }

                @Override
                public void setItemMovement(ItemEntity entity)
                {
                    Vec3 vec3 = entity.getDeltaMovement();
                    entity.setDeltaMovement(vec3.x * (double)0.95F, vec3.y + (double)(vec3.y < (double)0.06F ? 5.0E-4F : 0.0F), vec3.z * (double)0.95F);
                }

                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
                {
                    consumer.accept(new IClientFluidTypeExtensions()
                    {
                        private static final ResourceLocation LAVA_STILL = new ResourceLocation("block/lava_still"),
                                LAVA_FLOW = new ResourceLocation("block/lava_flow");

                        @Override
                        public ResourceLocation getStillTexture()
                        {
                            return LAVA_STILL;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture()
                        {
                            return LAVA_FLOW;
                        }
                    });
                }
            });

    public static void init(IEventBus bus)
    {
        ENTITY_TYPES.register(bus);
        CONTAINERS.register(bus);
        PARTICLES.register(bus);
        VANILLA_FLUID_TYPES.register(bus);
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
        {
            event.registerEntityRenderer(Register.FLYINGSWORD.get(), FlyingSwordRenderer::new);
            event.registerEntityRenderer(Register.QIPROJECTILE.get(), QiProjectileRenderer::new);

            // ClientBlockRegister.registerBlockRenderers();
        }

        @SubscribeEvent
        public static void register(RegisterEvent event)
        {
            event.register(ForgeRegistries.Keys.MENU_TYPES, helper -> helper.register("flyingswordcontainer", IForgeMenuType.create(FlyingSwordContainer::createContainerClientSide)));
        }
    }
}
