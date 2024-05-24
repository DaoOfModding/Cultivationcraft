package DaoOfModding.Cultivationcraft;

import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Client.GUI.HelpItems;
import DaoOfModding.Cultivationcraft.Client.Textures.initTextures;
import DaoOfModding.Cultivationcraft.Common.Advancements.CultivationAdvancements;
import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Config;
import DaoOfModding.Cultivationcraft.Common.Items.ItemRegister;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.BreathingHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSourceConfig;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.DefaultQuests;
import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Common.World.ModConfiguredFeatures;
import DaoOfModding.Cultivationcraft.Common.World.ModPlacedFeatures;
import DaoOfModding.Cultivationcraft.Common.World.PlantGeneration.PlantNames;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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

    protected static final ModList MOD_LIST = ModList.get();

    public Cultivationcraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonInit);
        modEventBus.addListener(this::clientInit);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.Server.spec, "cultivationcraft.toml");

        ModConfiguredFeatures.init(modEventBus);
        ModPlacedFeatures.init(modEventBus);

        Register.init(modEventBus);
        BlockRegister.init(modEventBus);
        ItemRegister.init(modEventBus);
        BodyPartNames.registerLungLocations();
        CultivationAdvancements.init(modEventBus);
    }

    protected void commonInit(final FMLCommonSetupEvent event) {
        PacketHandler.init();

        QiSourceConfig.init();
        PlantNames.init();
        Elements.init();
        TechniqueControl.init();
        BodyPartNames.init();
        Reflection.setup();
        DefaultQuests.init();
        BreathingHandler.init();
        ExternalCultivationHandler.init();
    }

    protected void clientInit(final FMLClientSetupEvent event) {
        ClientItemControl.init(event);
        GenericQiPoses.init();
        HelpItems.setup();
        DefaultTechniqueStatIDs.init();
        initTextures.init();
    }
}
