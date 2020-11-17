package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Client.Particles.QiParticle;
import DaoOfModding.Cultivationcraft.Client.Particles.QiParticleData;
import DaoOfModding.Cultivationcraft.Client.Particles.QiParticleType;
import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenBlock;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSourcesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStatsCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniquesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBindCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStackCapability;
import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainer;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Register
{
    public enum keyPresses { FLYINGSWORDSCREEN, SKILLHOTBARSWITCH }

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Cultivationcraft.MODID);

    public static RegistryObject<EntityType<FlyingSwordEntity>> FLYINGSWORD = ENTITY_TYPES.register("flyingsword", () ->
                                                                                EntityType.Builder.<FlyingSwordEntity>create(FlyingSwordEntity::new, EntityClassification.MISC)
                                                                                        .size(0.5f, 0.5f)
                                                                                        .setUpdateInterval(3)
                                                                                        .build("flyingsword"));

    public static ParticleType<QiParticleData> qiParticleType;

    public static ContainerType<FlyingSwordContainer> ContainerTypeFlyingSword;

    public static void registerCapabilities()
    {
        CultivatorStatsCapability.register();
        FlyingSwordContainerItemStackCapability.register();
        FlyingSwordBindCapability.register();
        ChunkQiSourcesCapability.register();
        CultivatorTechniquesCapability.register();
    }

    public static void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(Register.FLYINGSWORD.get(), FlyingSwordRenderer::new);

        BlockRegister.registerBlockRenderers();
    }

    // TODO: Block color handler
/*
    @SubscribeEvent
    public static void registerItemColourHandlers(final ColorHandlerEvent.Item event)
    {
        final BlockColors blockColors = event.getBlockColors();

        // Use the Block's colour handler for an ItemBlock
        final IBlockColor BlockColourHandler = (stack, tintIndex) -> {
            @SuppressWarnings("deprecation")
            final IBlockState state = ((BlockItem)(stack.getItem())).getBlock().getStateFromMeta(stack.getMetadata());
            return blockColors..colorMultiplier(state, null, null, tintIndex);
        };

        blockColors.register(BlockColourHandler, BlockRegister.frozenBlock);
    }*/


    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {

        @SubscribeEvent
        public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event)
        {
            Register.ContainerTypeFlyingSword = IForgeContainerType.create(FlyingSwordContainer::createContainerClientSide);
            Register.ContainerTypeFlyingSword.setRegistryName("flyingswordcontainer");
            event.getRegistry().register(Register.ContainerTypeFlyingSword);
        }

        @SubscribeEvent
        public static void onIParticleTypeRegistration(final RegistryEvent.Register<ParticleType<?>> event)
        {
            Register.qiParticleType = new QiParticleType();
            Register.qiParticleType.setRegistryName(Cultivationcraft.MODID, "qiparticle");
            event.getRegistry().register(Register.qiParticleType);
        }

        @SubscribeEvent
        public static void onParticleFactoryRegistration(final ParticleFactoryRegisterEvent event)
        {
            Minecraft.getInstance().particles.registerFactory(Register.qiParticleType, QiParticle.Factory::new);
        }
    }
}
