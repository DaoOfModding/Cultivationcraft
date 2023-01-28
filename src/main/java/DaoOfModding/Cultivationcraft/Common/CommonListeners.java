package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Client.ClientListeners;
import DaoOfModding.Cultivationcraft.Client.GUI.SkillHotbarOverlay;
import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStatsCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBind;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Server.ServerItemControl;
import DaoOfModding.Cultivationcraft.Server.ServerListeners;
import DaoOfModding.Cultivationcraft.Server.SkillHotbarServer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber()
public class CommonListeners
{
    protected static ArrayList<IChunkQiSources> tickingQiSources = new ArrayList<IChunkQiSources>();

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
    public static void LevelTick(TickEvent.LevelTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER)
        {
            // Clone the array list so it doesn't bork out if modified during ticking
            ArrayList<IChunkQiSources> ticking = (ArrayList<IChunkQiSources>)tickingQiSources.clone();

            for (IChunkQiSources sources : ticking)
            {
                if (sources.getDimension().compareTo(event.level.dimension().location()) == 0)
                {
                    boolean update = false;

                    if (sources.tick(event.level))
                        update = true;

                    if (update) {
                        LevelChunk chunk = event.level.getChunk(sources.getChunkPos().x, sources.getChunkPos().z);

                        // Mark the LevelChunk as dirty so it will save the updated capability
                        chunk.setUnsaved(true);

                        // Send the updated capability data to all tracking clients
                        // TODO - Is this updating too much?
                        PacketHandler.sendChunkQiSourcesToClient(chunk);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void worldLoad(LevelEvent.Load event)
    {
        if (event.getLevel().isClientSide())
            ClientItemControl.thisWorld = event.getLevel();
        else
            ServerItemControl.loaded = true;
    }

    @SubscribeEvent
    public static void playerJump(LivingEvent.LivingJumpEvent event)
    {
        if (event.getEntity() instanceof Player)
            Physics.applyJump((Player) event.getEntity());
    }

    @SubscribeEvent
    public static void useItem(PlayerInteractEvent.RightClickItem event)
    {
        // TODO: Allow eating of inedible items if the players stomach says they are edible

        // Cancel the eat event if the players stomach is incompatible with the food type
        if (event.getItemStack().isEdible())
            if (event.getEntity().getFoodData() instanceof QiFoodStats)
                if (!((QiFoodStats)((QiFoodStats) event.getEntity().getFoodData())).isEdible(event.getItemStack()))
                    event.setCanceled(true);
    }

    @SubscribeEvent
    public static void playerFall(LivingFallEvent event)
    {
        if (event.getEntity() instanceof Player)
            event.setDistance(Physics.reduceFallDistance((Player) event.getEntity(), event.getDistance()));
    }

    @SubscribeEvent
    public static void LevelChunkLoad(ChunkEvent.Load event)
    {
        IChunkQiSources sources = ChunkQiSources.getChunkQiSources((LevelChunk) event.getChunk());

        // Only on server
        if (!event.getLevel().isClientSide())
        {
            // If the LevelChunk's Qi sources have not been generated yet, generate them
            if (sources.getChunkPos() == null)
            {
                sources.setChunkPos(event.getChunk().getPos());
                sources.setDimension(((LevelChunk) event.getChunk()).getLevel().dimension().location());
                sources.generateQiSources(((LevelChunk) event.getChunk()).getLevel());

                // Mark the LevelChunk as dirty so it will save the updated capability
                event.getChunk().setUnsaved(true);

                // Send the new capability data to all tracking clients
                PacketHandler.sendChunkQiSourcesToClient((LevelChunk) event.getChunk());
            }
        }

        checkQiSourceIsTicking(sources);
    }

    public static void checkQiSourceIsTicking(IChunkQiSources source)
    {
        if (source.countQiSources() > 0)
            if (!tickingQiSources.contains(source))
                tickingQiSources.add(source);
    }

    @SubscribeEvent
    public static void LevelChunkLoad(ChunkEvent.Unload event)
    {
        IChunkQiSources sources = ChunkQiSources.getChunkQiSources((LevelChunk) event.getChunk());
        tickingQiSources.remove(sources);
    }

    // Fired off when an player logs into the world
    @SubscribeEvent
    public static void playerJoinsWorld(PlayerEvent.PlayerLoggedInEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getEntity()).setDisconnected(false);

        // On server
        if (!event.getEntity().getCommandSenderWorld().isClientSide())
        {
            // Loop through every player in the server
            for (Player sendPlayer : event.getEntity().getCommandSenderWorld().players())
            {
                // Send player stats to the newly joined player
                ServerItemControl.sendPlayerStats(sendPlayer, event.getEntity());

                // Do onJoin operations for every part of each player
                for (BodyPart part : BodyModifications.getBodyModifications(sendPlayer).getModifications().values())
                    part.onJoin(event.getEntity());

                for (HashMap<String, BodyPartOption> options : BodyModifications.getBodyModifications(sendPlayer).getModificationOptions().values())
                    for (BodyPartOption option : options.values())
                        option.onJoin(event.getEntity());
            }
            SkillHotbarServer.addPlayer(event.getEntity().getUUID());

            BodyPartStatControl.updateStats(event.getEntity());
        }
    }

    // Fired off when an player respawns into the world
    @SubscribeEvent
    public static void playerRespawns(PlayerEvent.PlayerRespawnEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getEntity()).setDisconnected(false);

        if (!event.getEntity().getCommandSenderWorld().isClientSide())
            ServerItemControl.sendPlayerStats(event.getEntity(), (Player)event.getEntity());
    }

    // Fired off when an player changes dimension
    @SubscribeEvent
    public static void playerChangesDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        CultivatorStats.getCultivatorStats(event.getEntity()).setDisconnected(false);

        if (!event.getEntity().getCommandSenderWorld().isClientSide())
            ServerItemControl.sendPlayerStats(event.getEntity(), (Player)event.getEntity());
    }

    // Fired off when an player starts tracking a target
    @SubscribeEvent
    public static void playerStartsTracking(PlayerEvent.StartTracking event)
    {
        if (!event.getEntity().getCommandSenderWorld().isClientSide())
            if (event.getTarget() instanceof Player)
                ServerItemControl.sendPlayerStats(event.getEntity(), (Player)event.getTarget());
    }

    // Fired off when an player starts watching a LevelChunk
    @SubscribeEvent
    public static void onLevelChunkWatch(ChunkWatchEvent.Watch event)
    {
        if (!event.getLevel().isClientSide())
            PacketHandler.sendChunkQiSourcesToClient(event.getLevel().getChunk(event.getPos().x, event.getPos().z), event.getPlayer());
    }

    @SubscribeEvent
    public static void playerDisconnects(PlayerEvent.PlayerLoggedOutEvent event)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(event.getEntity());

        if (stats != null)
            stats.setDisconnected(true);

        if (!event.getEntity().getCommandSenderWorld().isClientSide())
            SkillHotbarServer.removePlayer(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerDeath(PlayerEvent.Clone event)
    {
        // Do nothing if this wasn't a death
        if (!event.isWasDeath())
            return;

        event.getOriginal().reviveCaps();

        // Copy across the player's capabilities to the revived entity
        CultivatorStats.getCultivatorStats(event.getEntity()).readNBT(CultivatorStats.getCultivatorStats(event.getOriginal()).writeNBT());
        BodyModifications.getBodyModifications(event.getEntity()).read(BodyModifications.getBodyModifications(event.getOriginal()).write());
        CultivatorTechniques.getCultivatorTechniques(event.getEntity()).readNBT(CultivatorTechniques.getCultivatorTechniques(event.getOriginal()).writeNBT());

        event.getOriginal().invalidateCaps();

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

    protected static void cancelPlacement(PlayerInteractEvent event)
    {
        // Cancel placing item if the SkillHotbar is active
        if (event.getLevel().isClientSide())
        {
            if (SkillHotbarOverlay.isActive())
                event.setCanceled(true);
        }
        else
        if (SkillHotbarServer.isActive(event.getEntity().getUUID()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            if (!(((Player) event.getEntity()).isLocalPlayer()))
                QuestHandler.progressQuest((Player)event.getEntity(), Quest.DAMAGE_TAKEN, event.getAmount());
        }
    }
}
