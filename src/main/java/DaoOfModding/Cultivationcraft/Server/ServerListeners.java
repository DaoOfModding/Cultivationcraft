package DaoOfModding.Cultivationcraft.Server;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordEntity;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.Damage;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber()
public class ServerListeners
{
    protected static ArrayList<IChunkQiSources> tickingQiSources = new ArrayList<IChunkQiSources>();
    public static long lastServerTickTime = System.nanoTime();
    public static int tick = 0;

    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            QuestHandler.progressQuest(event.player, Quest.TIME_ALIVE, 1.0 / (20.0 * 60.0));
            ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(event.player);

            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                if (techs.getTechnique(i) != null)
                {
                    if (techs.getTechnique(i).isValid(event.player))
                    {
                        if (techs.getTechnique(i).isActive())
                            techs.getTechnique(i).tickServer(event);
                        else
                            techs.getTechnique(i).tickInactiveServer(event);
                    }
                    else
                    {
                        techs.setTechnique(i, null);
                    }
                }

            for (PassiveTechnique passive : techs.getPassives())
            {
                if (passive.isActive())
                    passive.tickServer(event);
                else
                    passive.tickInactiveServer(event);
            }

            IBodyModifications modifications = BodyModifications.getBodyModifications(event.player);

            modifications.setHealth(event.player.getHealth());

            for (BodyPart part : modifications.getModifications().values())
                part.onServerTick(event.player);

            for (HashMap<String, BodyPartOption> parts : modifications.getModificationOptions().values())
                for (BodyPartOption part : parts.values())
                    part.onServerTick(event.player);
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            tick = (tick + 1) % 800;

            if(ServerItemControl.loaded)
            {
                ServerItemControl.checkForRecalls();
                FlyingSwordBindProgresser.bindFlyingSword(System.nanoTime() - lastServerTickTime);
            }

            lastServerTickTime = System.nanoTime();
        }
    }

    public static void LevelTick(TickEvent.LevelTickEvent event)
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

                if (update)
                {
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

    // Fired off when an item is thrown
    @SubscribeEvent
    public static void itemThrowEvent(ItemTossEvent event)
    {
        // Check if the entity is an item
        if (event.getEntity() instanceof ItemEntity)
        {
            ItemEntity item = event.getEntity();

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

    @SubscribeEvent
    public static void onPlayerHurtInitial(LivingAttackEvent event)
    {
        if (event.getEntity().level.isClientSide)
            return;

        if (event.getEntity() instanceof Player)
        {
            // Don't modify damage if it bypasses invulnerability
            if (event.getSource().isBypassInvul())
                return;

            // Don't modify damage unless player is a cultivator
            if (!CultivatorStats.isCultivator((Player)event.getEntity()))
                return;

            // This has to be done twice because LivingHurtEvent cancels too late, but Living attack event can't set the amount of damage done -.-
            event.setCanceled(Damage.shouldCancel(event));
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event)
    {
        if (event.getEntity().level.isClientSide)
            return;

        if (event.getEntity() instanceof Player)
        {
            // Don't modify damage if it bypasses invulnerability
            if (event.getSource().isBypassInvul())
                return;

            // Don't modify damage unless player is a cultivator
            if (CultivatorStats.isCultivator((Player) event.getEntity()))
                event.setAmount(Damage.damage(event));

            Damage.applyStatusEffects(event);
        }
        else
        {
            event.setAmount(Damage.damageEntity(event));
            Damage.applyStatusEffects(event);
        }
    }
}
