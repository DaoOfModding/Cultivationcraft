package DaoOfModding.Cultivationcraft;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Client.ClientListeners;
import DaoOfModding.Cultivationcraft.Client.KeybindingControl;
import DaoOfModding.Cultivationcraft.Common.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CapabilityListeners;
import DaoOfModding.Cultivationcraft.Common.CommonListeners;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Server.ServerListeners;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("cultivationcraft")
public class Cultivationcraft {
    public static final String MODID = "cultivationcraft";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public Cultivationcraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonInit);
        modEventBus.addListener(this::clientInit);
        Register.ENTITY_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(Register.class);
        MinecraftForge.EVENT_BUS.register(BlockRegister.class);
        MinecraftForge.EVENT_BUS.register(CapabilityListeners.class);
        MinecraftForge.EVENT_BUS.register(CommonListeners.class);
        MinecraftForge.EVENT_BUS.register(ClientListeners.class);
        MinecraftForge.EVENT_BUS.register(KeybindingControl.class);
        MinecraftForge.EVENT_BUS.register(ServerListeners.class);
    }

    private void commonInit(final FMLCommonSetupEvent event)
    {
        PacketHandler.init();

        Register.registerCapabilities();

        Elements.init();
        TechniqueControl.init();
    }

    private void clientInit(final FMLClientSetupEvent event)
    {
        ClientItemControl.init(event);
        Register.registerRenderers();
    }
}
