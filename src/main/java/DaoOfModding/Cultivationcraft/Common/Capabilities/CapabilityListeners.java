package DaoOfModding.Cultivationcraft.Common.Capabilities;

import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSourcesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStatsCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniquesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBindCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStackCapability;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class CapabilityListeners
{
    public static final ResourceLocation CultivatorStatsCapabilityLocation = new ResourceLocation(Cultivationcraft.MODID, "cultivatorstats");
    public static final ResourceLocation FSCItemStackCapabilityLocation = new ResourceLocation(Cultivationcraft.MODID, "fscitemstack");
    public static final ResourceLocation FlyingSwordBindLocation = new ResourceLocation(Cultivationcraft.MODID, "flyingswordbind");
    public static final ResourceLocation ChunkQiSourcesLocation = new ResourceLocation(Cultivationcraft.MODID, "chunkqisources");
    public static final ResourceLocation CultivatorTechniquesCapabilityLocation = new ResourceLocation(Cultivationcraft.MODID, "cultivatortechniques");

    @SubscribeEvent
    public static void attachCapabilitiesChunk(final AttachCapabilitiesEvent<Chunk> event)
    {
        ChunkQiSourcesCapability newCapability = new ChunkQiSourcesCapability();
        event.addCapability(ChunkQiSourcesLocation, newCapability);
    }

    @SubscribeEvent
    public static void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof PlayerEntity)
        {
            event.addCapability(CultivatorStatsCapabilityLocation, new CultivatorStatsCapability());
            event.addCapability(CultivatorTechniquesCapabilityLocation, new CultivatorTechniquesCapability());
            event.addCapability(FSCItemStackCapabilityLocation, new FlyingSwordContainerItemStackCapability());
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesItem(final AttachCapabilitiesEvent<ItemStack> event)
    {
        // Attach bind capability to items capable of being bound
        if (event.getObject().getItem() instanceof SwordItem)
        {
            FlyingSwordBindCapability newCapability = new FlyingSwordBindCapability();
            event.addCapability(FlyingSwordBindLocation, newCapability);
        }
    }
}
