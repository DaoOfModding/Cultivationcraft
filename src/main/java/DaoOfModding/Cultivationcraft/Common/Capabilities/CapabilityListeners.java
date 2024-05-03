package DaoOfModding.Cultivationcraft.Common.Capabilities;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModificationsCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSourcesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStatsCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniquesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStackCapability;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class CapabilityListeners {
    public static final ResourceLocation CultivatorStatsCapabilityLocation = new ResourceLocation(Cultivationcraft.MODID, "cultivatorstats");
    public static final ResourceLocation FSCItemStackCapabilityLocation = new ResourceLocation(Cultivationcraft.MODID, "fscitemstack");
    public static final ResourceLocation ChunkQiSourcesLocation = new ResourceLocation(Cultivationcraft.MODID, "chunkqisources");
    public static final ResourceLocation CultivatorTechniquesCapabilityLocation = new ResourceLocation(Cultivationcraft.MODID, "cultivatortechniques");
    public static final ResourceLocation BodyModificationsCapabilityLocation = new ResourceLocation(Cultivationcraft.MODID, "bodymodification");

    @SubscribeEvent
    public static void attachCapabilitiesLevelChunk(final AttachCapabilitiesEvent<LevelChunk> event) {
        event.addCapability(ChunkQiSourcesLocation, new ChunkQiSourcesCapability());
    }

    @SubscribeEvent
    public static void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(CultivatorStatsCapabilityLocation, new CultivatorStatsCapability());
            event.addCapability(BodyModificationsCapabilityLocation, new BodyModificationsCapability());
            event.addCapability(CultivatorTechniquesCapabilityLocation, new CultivatorTechniquesCapability());
            event.addCapability(FSCItemStackCapabilityLocation, new FlyingSwordContainerItemStackCapability());
        }
    }
}
