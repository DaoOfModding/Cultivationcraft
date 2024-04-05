package DaoOfModding.Cultivationcraft.Network;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Effects.WindInstance;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.Packets.*;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.*;
import DaoOfModding.Cultivationcraft.Network.Packets.keypressPacket;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.CultivatorStatsPacket;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.CultivatorTypePacket;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.TechniqueStatSelectionPacket;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class PacketHandler
{
    protected static final byte KEYPRESS = 03;
    protected static final byte STAMINA_USE = 05;
    protected static final byte ATTACK = 07;
    protected static final byte LevelChunk_QI_SOURCES = 10;
    protected static final byte ELEMENTAL_EFFECT = 12;
    protected static final byte TECHNIQUE_USE = 20;
    protected static final byte TECHNIQUE_INFO = 21;
    protected static final byte PART_INFO = 22;
    protected static final byte QUEST_PROGRESS = 30;
    protected static final byte QUEST_CANCEL = 31;
    protected static final byte FLYING_SWORD_NBT_ID = 35;
    protected static final byte WIND_INSTANCE = 43;
    protected static final byte BLOOD_SPAWN_ID = 55;
    protected static final byte EXTERNAL_BLOOD_TICK_ID = 56;
    protected static final byte CULTIVATION_BREAKTHROUGH = 90;
    protected static final byte CULTIVATOR_TECH_STAT = 94;
    protected static final byte CULTIVATOR_TYPE = 95;
    protected static final byte BODY_FORGE_SELECTION = 96;
    protected static final byte BODY_MODIFICATIONS = 97;
    protected static final byte CULTIVATOR_TECHNIQUES = 98;
    protected static final byte CULTIVATOR_STATS = 99;
    protected static final byte FROZEN_BLOCK_RENDER = 100;
    protected static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Cultivationcraft.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init()
    {
        channel.registerMessage(KEYPRESS, keypressPacket.class, keypressPacket::encode, keypressPacket::decode, keypressPacket::handle);
        channel.registerMessage(ATTACK, AttackPacket.class, AttackPacket::encode, AttackPacket::decode, AttackPacket::handle);
        channel.registerMessage(STAMINA_USE, StaminaUsePacket.class, StaminaUsePacket::encode, StaminaUsePacket::decode, StaminaUsePacket::handle);
        channel.registerMessage(LevelChunk_QI_SOURCES, ChunkQiSourcesPacket.class, ChunkQiSourcesPacket::encode, ChunkQiSourcesPacket::decode, ChunkQiSourcesPacket::handle);
        channel.registerMessage(ELEMENTAL_EFFECT, ElementalEffectPacket.class, ElementalEffectPacket::encode, ElementalEffectPacket::decode, ElementalEffectPacket::handle);
        channel.registerMessage(TECHNIQUE_USE, TechniqueUsePacket.class, TechniqueUsePacket::encode, TechniqueUsePacket::decode, TechniqueUsePacket::handle);
        channel.registerMessage(TECHNIQUE_INFO, TechniqueInfoPacket.class, TechniqueInfoPacket::encode, TechniqueInfoPacket::decode, TechniqueInfoPacket::handle);
        channel.registerMessage(PART_INFO, PartInfoPacket.class, PartInfoPacket::encode, PartInfoPacket::decode, PartInfoPacket::handle);
        channel.registerMessage(QUEST_PROGRESS, QuestPacket.class, QuestPacket::encode, QuestPacket::decode, QuestPacket::handle);
        channel.registerMessage(QUEST_CANCEL, QuestCancelPacket.class, QuestCancelPacket::encode, QuestCancelPacket::decode, QuestCancelPacket::handle);
        channel.registerMessage(FLYING_SWORD_NBT_ID, ConvertToFlyingPacket.class, ConvertToFlyingPacket::encode, ConvertToFlyingPacket::decode, ConvertToFlyingPacket::handle);
        channel.registerMessage(WIND_INSTANCE, WindPacket.class, WindPacket::encode, WindPacket::decode, WindPacket::handle);
        channel.registerMessage(BLOOD_SPAWN_ID, BloodPacket.class, BloodPacket::encode, BloodPacket::decode, BloodPacket::handle);
        channel.registerMessage(EXTERNAL_BLOOD_TICK_ID, ExternalBloodTickPacket.class, ExternalBloodTickPacket::encode, ExternalBloodTickPacket::decode, ExternalBloodTickPacket::handle);
        channel.registerMessage(CULTIVATION_BREAKTHROUGH, BreakthroughPacket.class, BreakthroughPacket::encode, BreakthroughPacket::decode, BreakthroughPacket::handle);
        channel.registerMessage(CULTIVATOR_TECHNIQUES, CultivatorTechniquesPacket.class, CultivatorTechniquesPacket::encode, CultivatorTechniquesPacket::decode, CultivatorTechniquesPacket::handle);
        channel.registerMessage(CULTIVATOR_STATS, CultivatorStatsPacket.class, CultivatorStatsPacket::encode, CultivatorStatsPacket::decode, CultivatorStatsPacket::handle);
        channel.registerMessage(CULTIVATOR_TYPE, CultivatorTypePacket.class, CultivatorTypePacket::encode, CultivatorTypePacket::decode, CultivatorTypePacket::handle);
        channel.registerMessage(BODY_FORGE_SELECTION, BodyForgeSelectionPacket.class, BodyForgeSelectionPacket::encode, BodyForgeSelectionPacket::decode, BodyForgeSelectionPacket::handle);
        channel.registerMessage(BODY_MODIFICATIONS, BodyModificationsPacket.class, BodyModificationsPacket::encode, BodyModificationsPacket::decode, BodyModificationsPacket::handle);
        channel.registerMessage(CULTIVATOR_TECH_STAT, TechniqueStatSelectionPacket.class, TechniqueStatSelectionPacket::encode, TechniqueStatSelectionPacket::decode, TechniqueStatSelectionPacket::handle);
    }

    public static void sendAttackToClient(UUID playerID, HitResult.Type type, Vec3 pos, UUID targetID, Direction direction, int slot)
    {
        AttackPacket pack = new AttackPacket(playerID, type, pos, targetID, direction, slot);
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID)), pack);
    }

    public static void sendBloodSpawnToClient(UUID playerID, Vec3 source, double amount)
    {
        BloodPacket pack = new BloodPacket(playerID, amount, source);
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID)), pack);
    }

    public static void sendQuestProgressToClient(UUID player, double amount)
    {
        QuestPacket packet = new QuestPacket(player, amount);

        channel.send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(player)), packet);
    }

    public static void sendChunkQiSourcesToClient(LevelChunk LevelChunk)
    {
        IChunkQiSources sources = ChunkQiSources.getChunkQiSources(LevelChunk);
        ChunkQiSourcesPacket pack = new ChunkQiSourcesPacket(sources);

        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> LevelChunk), pack);
    }

    public static void sendWindInstanceToClients(WindInstance wind, Entity entity)
    {
        WindPacket pack = new WindPacket(wind, entity.getUUID());

        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), pack);
    }

    public static void sendChunkQiSourcesToClient(LevelChunk LevelChunk, ServerPlayer player)
    {
        IChunkQiSources sources = ChunkQiSources.getChunkQiSources(LevelChunk);
        ChunkQiSourcesPacket pack = new ChunkQiSourcesPacket(sources);

        channel.send(PacketDistributor.PLAYER.with(() -> player), pack);
    }

    public static void sendCultivatorStatsToClient(Player player)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);

        sendCultivatorStatsToClient(player, target);
        sendCultivatorTechniquesToClient(player);
    }

    public static void sendCultivatorStatsToSpecificClient(Player player, ServerPlayer toSend)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> toSend);

        sendCultivatorStatsToClient(player, target);
        sendCultivatorTechniquesToSpecificClient(player, toSend);
    }

    protected static void sendCultivatorStatsToClient(Player player, PacketDistributor.PacketTarget distribute)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        // Send the cultivator's stats to the client
        CultivatorStatsPacket pack = new CultivatorStatsPacket(player.getUUID(), stats);
        channel.send(distribute, pack);
    }

    public static void updateStaminaForClients(float stamina, Player player)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);

        StaminaUsePacket pack = new StaminaUsePacket(stamina, player.getUUID());
        channel.send(target, pack);
    }

    public static void sendBodyModificationsToSpecificClient(Player player, ServerPlayer toSend)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> toSend);

        sendBodyModificationsToClient(player, target);
    }

    public static void sendBodyModificationsToClient(Player player)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);

        sendBodyModificationsToClient(player, target);
    }

    protected static void sendBodyModificationsToClient(Player player, PacketDistributor.PacketTarget distribute)
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        // Send the cultivator's stats to the client
        BodyModificationsPacket pack = new BodyModificationsPacket(player.getUUID(), modifications);
        channel.send(distribute, pack);
    }

    public static void sendTechniqueInfoToClients(TechniqueInfoPacket packet, Player except)
    {
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> except), packet);
    }

    public static void sendPartInfoToClients(PartInfoPacket packet, Player except)
    {
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> except), packet);
    }

    public static void sendCultivatorTechniquesToClient(Player player)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        CultivatorTechniquesPacket pack = new CultivatorTechniquesPacket(player.getUUID(), techs);
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), pack);
    }

    public static void sendCultivatorTechniquesToSpecificClient(Player player, ServerPlayer toSend)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        CultivatorTechniquesPacket pack = new CultivatorTechniquesPacket(player.getUUID(), techs);
        channel.send(PacketDistributor.PLAYER.with(() -> toSend), pack);
    }
}
