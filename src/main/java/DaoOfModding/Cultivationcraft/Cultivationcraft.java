package DaoOfModding.Cultivationcraft;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStatsCapability;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordEntity;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordRenderer;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Server.ServerItemControl;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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


// KNOWN ISSUES: Dolphins trying to 'play' with flying swords, crashing the game. Caused by DolphinEntity::PlayWithItemsGoal


// The value here should match an entry in the META-INF/mods.toml file
@Mod("cultivationcraft")
public class Cultivationcraft
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static long lastTickTime;

    public static final ResourceLocation CultivatorStatsCapabilityLocation = new ResourceLocation("cultivationcraft", "cultivatorstats");

    public Cultivationcraft() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the setup method for modloading
        modEventBus.addListener(this::commonInit);
        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::clientInit);

        Register.ENTITY_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonInit(final FMLCommonSetupEvent event)
    {
        ServerItemControl.init(event);
        PacketHandler.init();

        CultivatorStatsCapability.register();
    }

    private void clientInit(final FMLClientSetupEvent event)
    {

        ClientItemControl.init(event);
        RenderingRegistry.registerEntityRenderingHandler(Register.FLYINGSWORD.get(), FlyingSwordRenderer::new);
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
    }

    @SubscribeEvent
    public void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof PlayerEntity)
            event.addCapability(CultivatorStatsCapabilityLocation, new CultivatorStatsCapability());
    }

    @SubscribeEvent
    public void gameTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if(ClientItemControl.thisWorld != null)
            {
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
            }
        }
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event)
    {


    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event)
    {
        if (event.getWorld().isRemote())
            ClientItemControl.thisWorld = event.getWorld();
        else
            ServerItemControl.thisWorld = event.getWorld();
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
        if (!event.getEntity().getEntityWorld().isRemote)
            ServerItemControl.playerLogsIn(event);

        // TO DO, relink flying swords to player
    }

    // Fired off when an entity joins the world, this happens on both the client and the server
    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event)
    {
    }
}
