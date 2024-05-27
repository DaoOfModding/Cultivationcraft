package DaoOfModding.Cultivationcraft.Server;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.CommonListeners;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordEntity;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.Damage;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber()
public class ServerListeners
{
    public static long lastServerTickTime = System.nanoTime();
    public static int tick = 0;

    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            QuestHandler.progressQuest(event.player, Quest.TIME_ALIVE, 1.0 / (20.0 * 60.0));
            QuestHandler.progressQuest(event.player, Quest.LIVE, 1.0 / (20.0 * 60.0));

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

            // Breath once every second
            if (tick %20 == 0)
                PlayerHealthManager.getLungs(event.player).breath(event.player);

            for (BodyPart part : modifications.getModifications().values())
                part.onServerTick(event.player);

            for (HashMap<String, BodyPartOption> parts : modifications.getModificationOptions().values())
                for (BodyPartOption part : parts.values())
                    part.onServerTick(event.player);

            if (CultivatorStats.getCultivatorStats(event.player).getCultivationType() == CultivationTypes.QI_CONDENSER)
                CultivatorStats.getCultivatorStats(event.player).getCultivation().tick(event.player);

            // Update the client every second if it has not been updated to ensure that stamina doesn't get descynced
            if (tick % 20 == 0)
            {
                if (event.player.getFoodData() instanceof QiFoodStats)
                    if (((QiFoodStats) event.player.getFoodData()).shouldUpdate())
                        PacketHandler.updateStaminaForClients(((QiFoodStats) event.player.getFoodData()).getTrueFoodLevel(), event.player);
            }
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event)
    {
        if (event.getEntity().level.isClientSide || !(event.getEntity() instanceof Player) || !event.getEntity().isAlive())
            return;

        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques((Player)event.getEntity());

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (techs.getTechnique(i) != null)
                if (techs.getTechnique(i).isActive())
                    techs.getTechnique(i).onFall(event);

    }

    @SubscribeEvent
    public static void startTracking(PlayerEvent.StartTracking event)
    {
        if (event.getEntity().level.isClientSide)
            return;

        if (!(event.getTarget() instanceof  Player))
            return;

        Player target = (Player)event.getTarget();

        PacketHandler.sendBodyModificationsToSpecificClient(target, (ServerPlayer) event.getEntity());
        PacketHandler.sendCultivatorTechniquesToSpecificClient(target, (ServerPlayer) event.getEntity());

        if (target.getFoodData() instanceof QiFoodStats)
            PacketHandler.updateStaminaForClients(((QiFoodStats) target.getFoodData()).getTrueFoodLevel(), target);
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            tick = (tick + 1) % 800;

            if(ServerItemControl.loaded)
            {
                FlyingSwordBindProgresser.bindFlyingSword(System.nanoTime() - lastServerTickTime);
            }

            lastServerTickTime = System.nanoTime();
        }
    }

    @SubscribeEvent
    public static void LevelTick(TickEvent.LevelTickEvent event)
    {
        // Only do on server
        if (event.side == LogicalSide.CLIENT)
            return;

        // Clone the array list so it doesn't bork out if modified during ticking
        ArrayList<IChunkQiSources> ticking = (ArrayList<IChunkQiSources>) CommonListeners.tickingQiSources.clone();

        for (IChunkQiSources sources : ticking)
        {
            if (sources.getDimension().compareTo(event.level.dimension().location()) == 0)
            {
                boolean update = false;

                if (sources.tick(event.level) || tick % 400 == 0)
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
    public static void onPlayerAttack(AttackEntityEvent event)
    {
        // Cycle through all active AttackTechniques and call onPlayerAttack

        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(event.getEntity());

        for (int i = 0; i < 9; i ++)
        {
            Technique testTech = techs.getTechnique(i);

            if (testTech != null && testTech.isActive() && testTech instanceof AttackTechnique)
                ((AttackTechnique)testTech).onPlayerAttack(event);
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event)
    {
        // Cycle through all active AttackTechniques if this attack is coming from a player and check if this attack has been canceled
        if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player)
        {
            ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques((Player)event.getSource().getEntity());

            for (int i = 0; i < 9; i++) {
                Technique testTech = techs.getTechnique(i);

                if (testTech != null && testTech.isActive() && testTech instanceof AttackTechnique)
                    if (((AttackTechnique) testTech).cancelAttack(event))
                    {
                        event.setCanceled(true);
                        return;
                    }
            }
        }

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

        if (event.getSource().getEntity() instanceof Player)
        {
            QuestHandler.progressQuest((Player)event.getSource().getEntity(), Quest.DAMAGE_DEALT, event.getAmount());

            if (event.getSource().getMsgId().matches("explosion.player"))
                QuestHandler.progressQuest((Player) event.getSource().getEntity(), Quest.EXPLOSION_DAMAGE_DEALT, event.getAmount());

            if (event.getSource() instanceof QiDamageSource)
                QuestHandler.progressQuest((Player)event.getSource().getEntity(), Quest.DAMAGE_DEALT, event.getAmount(), ((QiDamageSource)event.getSource()).getElement().toString());
        }
    }
}
