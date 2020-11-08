package DaoOfModding.Cultivationcraft;

import DaoOfModding.Cultivationcraft.Client.AddChunkQiSourceToClient;
import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Client.Particles.QiParticle;
import DaoOfModding.Cultivationcraft.Client.Particles.QiParticleType;
import DaoOfModding.Cultivationcraft.Client.Renderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSourcesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStatsCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniquesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBindCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStackCapability;
import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainer;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordEntity;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordRenderer;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.DivineSenseTechnique;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Server.FlyingSwordBindProgresser;
import DaoOfModding.Cultivationcraft.Server.ServerItemControl;
import DaoOfModding.Cultivationcraft.Server.SkillHotbarServer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("cultivationcraft")
public class Cultivationcraft
{
    // TODO: Add all these random listeners to a proper listener class
    // TODO: Add MODID

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static long lastTickTime = System.nanoTime();
    public static long lastServerTickTime = System.nanoTime();

    public static final ResourceLocation CultivatorStatsCapabilityLocation = new ResourceLocation("cultivationcraft", "cultivatorstats");
    public static final ResourceLocation FSCItemStackCapabilityLocation = new ResourceLocation("cultivationcraft", "fscitemstack");
    public static final ResourceLocation FlyingSwordBindLocation = new ResourceLocation("cultivationcraft", "flyingswordbind");
    public static final ResourceLocation ChunkQiSourcesLocation = new ResourceLocation("cultivationcraft", "chunkqisources");
    public static final ResourceLocation CultivatorTechniquesCapabilityLocation = new ResourceLocation("cultivationcraft", "cultivatortechniques");

    public Cultivationcraft()
    {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonInit);
        modEventBus.addListener(this::clientInit);
        modEventBus.addListener(this::onParticleFactoryRegistration);

        Register.ENTITY_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonInit(final FMLCommonSetupEvent event)
    {
        ServerItemControl.init(event);
        PacketHandler.init();

        Register.registerCapabilities();

        Elements.init();
    }

    private void clientInit(final FMLClientSetupEvent event)
    {

        ClientItemControl.init(event);
        RenderingRegistry.registerEntityRenderingHandler(Register.FLYINGSWORD.get(), FlyingSwordRenderer::new);
    }

    @SubscribeEvent
    public void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particles.registerFactory(Register.qiParticleType, QiParticle.Factory::new);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // register a new block here
        }

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
            Register.qiParticleType.setRegistryName("cultivationcraft", "qiparticle");
            event.getRegistry().register(Register.qiParticleType);
        }
    }

    @SubscribeEvent
    public void attachCapabilitiesChunk(final AttachCapabilitiesEvent<Chunk> event)
    {
        ChunkQiSourcesCapability newCapability = new ChunkQiSourcesCapability();
        event.addCapability(ChunkQiSourcesLocation, newCapability);
    }

    @SubscribeEvent
    public void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof PlayerEntity)
        {
            event.addCapability(CultivatorStatsCapabilityLocation, new CultivatorStatsCapability());
            event.addCapability(CultivatorTechniquesCapabilityLocation, new CultivatorTechniquesCapability());
            event.addCapability(FSCItemStackCapabilityLocation, new FlyingSwordContainerItemStackCapability());
        }
    }

    @SubscribeEvent
    public void attachCapabilitiesItem(final AttachCapabilitiesEvent<ItemStack> event)
    {
        // Attach bind capability to items capable of being bound
        if (event.getObject().getItem() instanceof SwordItem)
        {
            FlyingSwordBindCapability newCapability = new FlyingSwordBindCapability();
            event.addCapability(FlyingSwordBindLocation, newCapability);
        }
    }

    @SubscribeEvent
    public void gameTick(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
        {
            SkillHotbarOverlay.PreRenderSkillHotbar(event.getMatrixStack());
        }
    }

    @SubscribeEvent
    public void gameTick(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
        {
            SkillHotbarOverlay.PostRenderSkillHotbar(event.getMatrixStack());
        }
    }

    @SubscribeEvent
    public void gameTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if(ClientItemControl.thisWorld != null)
            {
                // Attempt to process any ChunkQiSource packets pending
                AddChunkQiSourceToClient.processPackets();
            }

            lastTickTime = System.nanoTime();
        }
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if(ServerItemControl.thisWorld != null)
            {
                ServerItemControl.checkForRecalls();
                FlyingSwordBindProgresser.bindFlyingSword(System.nanoTime() - lastServerTickTime);
            }

            lastServerTickTime = System.nanoTime();
        }
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
            if (Minecraft.getInstance().world != null)
                Renderer.render();
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event)
    {
        if (event.getWorld().isRemote())
            ClientItemControl.thisWorld = event.getWorld();
        else
            ServerItemControl.thisWorld = event.getWorld();
    }

    @SubscribeEvent
    public void chunkLoad(ChunkEvent.Load event)
    {
        // Only on server
        if (!event.getWorld().isRemote())
        {
            // If the Chunk's Qi sources have no been generated yet, generate them
            IChunkQiSources sources = ChunkQiSources.getChunkQiSources((Chunk) event.getChunk());
            if (sources.getChunkPos() == null)
            {
                sources.setChunkPos(event.getChunk().getPos());
                sources.generateQiSources();

                // Mark the chunk as dirty so it will save the updated capability
                ((Chunk) event.getChunk()).markDirty();

                // Send the new capability data to all tracking clients
                PacketHandler.sendChunkQiSourcesToClient((Chunk) event.getChunk());
            }
        }
    }

    // Fired off when an item is thrown, server only
    @SubscribeEvent
    public void entityJoinWorld(ItemTossEvent event)
    {
        // Check if the entity is an item
        if (event.getEntity() instanceof ItemEntity)
        {
            ItemEntity item = (ItemEntity) event.getEntity();

            // If the item is a flying sword, spawn a flying sword item and cancel the current spawn
            if (FlyingSwordController.isFlying(item.getItem()))
            {
                // Only do this if the entity is not already a FlyingSwordEntity
                if (!(event.getEntity() instanceof FlyingSwordEntity))
                {
                    event.setCanceled(true);
                    FlyingSwordController.spawnFlyingSword(item);
                }
            }
        }
    }

    // Fired off when an player logs into the world
    @SubscribeEvent
    public void playerJoinsWorld(PlayerEvent.PlayerLoggedInEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getPlayer()).setDisconnected(false);

        if (!event.getEntity().getEntityWorld().isRemote)
        {
            // TESTING
            CultivatorTechniques.getCultivatorTechniques(event.getPlayer()).setTechnique(0, new DivineSenseTechnique());

            ServerItemControl.sendPlayerStats(event.getPlayer(), (PlayerEntity) event.getPlayer());

        }

        if (!event.getPlayer().getEntityWorld().isRemote)
            SkillHotbarServer.addPlayer(event.getPlayer().getUniqueID());
    }

    // Fired off when an player respawns into the world
    @SubscribeEvent
    public void playerRespawns(PlayerEvent.PlayerRespawnEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getPlayer()).setDisconnected(false);

        if (!event.getEntity().getEntityWorld().isRemote)
            ServerItemControl.sendPlayerStats(event.getPlayer(), (PlayerEntity)event.getPlayer());
    }

    // Fired off when an player changes dimension
    @SubscribeEvent
    public void playerChangesDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getPlayer()).setDisconnected(false);

        if (!event.getEntity().getEntityWorld().isRemote)
            ServerItemControl.sendPlayerStats(event.getPlayer(), (PlayerEntity)event.getPlayer());
    }

    // Fired off when an player starts tracking a target
    @SubscribeEvent
    public void playerStartsTracking(PlayerEvent.StartTracking event)
    {
        if (!event.getEntity().getEntityWorld().isRemote)
            if (event.getTarget() instanceof PlayerEntity)
                ServerItemControl.sendPlayerStats(event.getPlayer(), (PlayerEntity)event.getTarget());
    }

    // Fired off when an player starts watching a chunk
    @SubscribeEvent
    public void onChunkWatch(ChunkWatchEvent.Watch event)
    {
        if (!event.getWorld().isRemote)
            PacketHandler.sendChunkQiSourcesToClient(event.getWorld().getChunk(event.getPos().x, event.getPos().z), event.getPlayer());
    }

    @SubscribeEvent
    public void playerDisconnects(PlayerEvent.PlayerLoggedOutEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getPlayer()).setDisconnected(true);

        if (!event.getPlayer().getEntityWorld().isRemote)
            SkillHotbarServer.removePlayer(event.getPlayer().getUniqueID());
    }

    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent.RightClickBlock event)
    {
        cancelPlacement(event);
    }

    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent.RightClickItem event)
    {
        cancelPlacement(event);
    }

    private void cancelPlacement(PlayerInteractEvent event)
    {
        // Cancel placing item is the SkillHotbar is active
        if (event.getWorld().isRemote)
        {
            if (SkillHotbarOverlay.isActive())
                event.setCanceled(true);
        }
        else
        if (SkillHotbarServer.isActive(event.getPlayer().getUniqueID()))
            event.setCanceled(true);
    }

    // Fired off when an entity joins the world, this happens on both the client and the server
    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event)
    {
    }
}
