package DaoOfModding.Cultivationcraft;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericPoses;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
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
    }

    private void commonInit(final FMLCommonSetupEvent event)
    {
        PacketHandler.init();

        Register.registerCapabilities();

        Elements.init();
        TechniqueControl.init();
        GenericPoses.init();
        GenericQiPoses.init();
        BodyPartNames.init();
    }

    private void clientInit(final FMLClientSetupEvent event)
    {
        ClientItemControl.init(event);
        Register.registerRenderers();
    }
}
