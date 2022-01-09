package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Client.ClientListeners;
import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Server.ServerItemControl;
import DaoOfModding.Cultivationcraft.Server.ServerListeners;
import DaoOfModding.Cultivationcraft.Server.SkillHotbarServer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.HashMap;

@Mod.EventBusSubscriber()
public class CommonListeners
{
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if (!event.player.isAlive())
            return;

        if (event.player.getCommandSenderWorld().isClientSide())
            ClientListeners.playerTick(event);
        else
            ServerListeners.playerTick(event);
    }

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event)
    {
        if (event.getWorld().isClientSide())
            ClientItemControl.thisWorld = event.getWorld();
        else
            ServerItemControl.loaded = true;
    }

    @SubscribeEvent
    public static void playerJump(LivingEvent.LivingJumpEvent event)
    {
        if (event.getEntity() instanceof PlayerEntity)
            Physics.applyJump((PlayerEntity) event.getEntity());
    }

    @SubscribeEvent
    public static void useItem(PlayerInteractEvent.RightClickItem event)
    {
        // TODO: Allow eating of inedible items if the players stomach says they are edible

        // Cancel the eat event if the players stomach is incompatible with the food type
        if (event.getItemStack().isEdible())
            if (event.getPlayer().getFoodData() instanceof QiFoodStats)
                if (!((QiFoodStats)((QiFoodStats) event.getPlayer().getFoodData())).isEdible(event.getItemStack()))
                    event.setCanceled(true);
    }

    @SubscribeEvent
    public static void playerFall(LivingFallEvent event)
    {
        if (event.getEntity() instanceof PlayerEntity)
            event.setDistance(Physics.reduceFallDistance((PlayerEntity) event.getEntity(), event.getDistance()));
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkEvent.Load event)
    {
        // Only on server
        if (!event.getWorld().isClientSide())
        {
            // If the Chunk's Qi sources have not been generated yet, generate them
            IChunkQiSources sources = ChunkQiSources.getChunkQiSources((Chunk) event.getChunk());
            if (sources.getChunkPos() == null)
            {
                sources.setChunkPos(event.getChunk().getPos());
                sources.generateQiSources();

                // Mark the chunk as dirty so it will save the updated capability
                ((Chunk) event.getChunk()).markUnsaved();

                // Send the new capability data to all tracking clients
                PacketHandler.sendChunkQiSourcesToClient((Chunk) event.getChunk());
            }
        }
    }

    // Fired off when an player logs into the world
    @SubscribeEvent
    public static void playerJoinsWorld(PlayerEvent.PlayerLoggedInEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getPlayer()).setDisconnected(false);

        // On server
        if (!event.getEntity().getCommandSenderWorld().isClientSide())
        {
            // Loop through every player in the server
            for (PlayerEntity sendPlayer : event.getEntity().getCommandSenderWorld().players())
            {
                // Send player stats to the newly joined player
                ServerItemControl.sendPlayerStats(sendPlayer, event.getPlayer());

                // Do onJoin operations for every part of each player
                for (BodyPart part : BodyModifications.getBodyModifications(sendPlayer).getModifications().values())
                    part.onJoin(event.getPlayer());

                for (HashMap<String, BodyPartOption> options : BodyModifications.getBodyModifications(sendPlayer).getModificationOptions().values())
                    for (BodyPartOption option : options.values())
                        option.onJoin(event.getPlayer());
            }
            SkillHotbarServer.addPlayer(event.getPlayer().getUUID());

            BodyPartStatControl.updateStats(event.getPlayer());
        }
    }

    // Fired off when an player respawns into the world
    @SubscribeEvent
    public static void playerRespawns(PlayerEvent.PlayerRespawnEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getPlayer()).setDisconnected(false);

        if (!event.getEntity().getCommandSenderWorld().isClientSide())
            ServerItemControl.sendPlayerStats(event.getPlayer(), (PlayerEntity)event.getPlayer());
    }

    // Fired off when an player changes dimension
    @SubscribeEvent
    public static void playerChangesDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getPlayer()).setDisconnected(false);

        if (!event.getEntity().getCommandSenderWorld().isClientSide())
            ServerItemControl.sendPlayerStats(event.getPlayer(), (PlayerEntity)event.getPlayer());
    }

    // Fired off when an player starts tracking a target
    @SubscribeEvent
    public static void playerStartsTracking(PlayerEvent.StartTracking event)
    {
        if (!event.getEntity().getCommandSenderWorld().isClientSide())
            if (event.getTarget() instanceof PlayerEntity)
                ServerItemControl.sendPlayerStats(event.getPlayer(), (PlayerEntity)event.getTarget());
    }

    // Fired off when an player starts watching a chunk
    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Watch event)
    {
        if (!event.getWorld().isClientSide())
            PacketHandler.sendChunkQiSourcesToClient(event.getWorld().getChunk(event.getPos().x, event.getPos().z), event.getPlayer());
    }

    @SubscribeEvent
    public static void playerDisconnects(PlayerEvent.PlayerLoggedOutEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getPlayer()).setDisconnected(true);

        if (!event.getPlayer().getCommandSenderWorld().isClientSide())
            SkillHotbarServer.removePlayer(event.getPlayer().getUUID());
    }

    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.RightClickBlock event)
    {
        cancelPlacement(event);
    }

    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.RightClickItem event)
    {
        cancelPlacement(event);
    }

    private static void cancelPlacement(PlayerInteractEvent event)
    {
        // Cancel placing item if the SkillHotbar is active
        if (event.getWorld().isClientSide())
        {
            if (SkillHotbarOverlay.isActive())
                event.setCanceled(true);
        }
        else
        if (SkillHotbarServer.isActive(event.getPlayer().getUUID()))
            event.setCanceled(true);
    }

    // Fired off when an entity joins the world, this happens on both the client and the server
    @SubscribeEvent
    public void entityJoinWorld(EntityJoinWorldEvent event)
    {
    }
}
