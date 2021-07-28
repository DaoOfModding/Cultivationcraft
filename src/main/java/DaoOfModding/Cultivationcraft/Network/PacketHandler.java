package DaoOfModding.Cultivationcraft.Network;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.Packets.*;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.*;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.Packets.keypressPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class PacketHandler
{
    private static final byte KEYPRESS = 03;
    private static final byte ATTACK = 07;
    private static final byte CHUNK_QI_SOURCES = 10;
    private static final byte TECHNIQUE_USE = 20;
    private static final byte FLYING_SWORD_NBT_ID = 35;
    private static final byte FLYING_SWORD_RECALL = 36;
    private static final byte CULTIVATOR_TARGET_ID = 76;
    private static final byte BODY_FORGE_SELECTION = 96;
    private static final byte BODY_MODIFICATIONS = 97;
    private static final byte CULTIVATOR_TECHNIQUES = 98;
    private static final byte CULTIVATOR_STATS = 99;
    private static final String PROTOCOL_VERSION = "1";

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
        channel.registerMessage(CHUNK_QI_SOURCES, ChunkQiSourcesPacket.class, ChunkQiSourcesPacket::encode, ChunkQiSourcesPacket::decode, ChunkQiSourcesPacket::handle);
        channel.registerMessage(TECHNIQUE_USE, TechniqueUsePacket.class, TechniqueUsePacket::encode, TechniqueUsePacket::decode, TechniqueUsePacket::handle);
        channel.registerMessage(FLYING_SWORD_NBT_ID, ConvertToFlyingPacket.class, ConvertToFlyingPacket::encode, ConvertToFlyingPacket::decode, ConvertToFlyingPacket::handle);
        channel.registerMessage(FLYING_SWORD_RECALL, RecallFlyingSwordPacket.class, RecallFlyingSwordPacket::encode, RecallFlyingSwordPacket::decode, RecallFlyingSwordPacket::handle);
        channel.registerMessage(CULTIVATOR_TARGET_ID, CultivatorTargetPacket.class, CultivatorTargetPacket::encode, CultivatorTargetPacket::decode, CultivatorTargetPacket::handle);
        channel.registerMessage(CULTIVATOR_TECHNIQUES, CultivatorTechniquesPacket.class, CultivatorTechniquesPacket::encode, CultivatorTechniquesPacket::decode, CultivatorTechniquesPacket::handle);
        channel.registerMessage(CULTIVATOR_STATS, CultivatorStatsPacket.class, CultivatorStatsPacket::encode, CultivatorStatsPacket::decode, CultivatorStatsPacket::handle);
        channel.registerMessage(BODY_FORGE_SELECTION, BodyForgeSelectionPacket.class, BodyForgeSelectionPacket::encode, BodyForgeSelectionPacket::decode, BodyForgeSelectionPacket::handle);
        channel.registerMessage(BODY_MODIFICATIONS, BodyModificationsPacket.class, BodyModificationsPacket::encode, BodyModificationsPacket::decode, BodyModificationsPacket::handle);
    }

    public static void sendRecallFlyingToClient(boolean recall, UUID playerID)
    {
        RecallFlyingSwordPacket pack = new RecallFlyingSwordPacket(recall, playerID);
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID)), pack);
    }

    public static void sendCultivatorTargetToClient(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetID)
    {
        CultivatorTargetPacket pack = new CultivatorTargetPacket(playerID, type, pos, targetID);
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID)), pack);
    }

    public static void sendAttackToClient(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetID, int slot)
    {
        AttackPacket pack = new AttackPacket(playerID, type, pos, targetID, slot);
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(playerID)), pack);
    }

    public static void sendChunkQiSourcesToClient(Chunk chunk)
    {
        IChunkQiSources sources = ChunkQiSources.getChunkQiSources(chunk);
        ChunkQiSourcesPacket pack = new ChunkQiSourcesPacket(sources.getChunkPos(), sources.getQiSources());

        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), pack);
    }

    public static void sendChunkQiSourcesToClient(Chunk chunk, ServerPlayerEntity player)
    {
        IChunkQiSources sources = ChunkQiSources.getChunkQiSources(chunk);
        ChunkQiSourcesPacket pack = new ChunkQiSourcesPacket(sources.getChunkPos(), sources.getQiSources());

        channel.send(PacketDistributor.PLAYER.with(() -> player), pack);
    }

    public static void sendCultivatorStatsToClient(PlayerEntity player)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);

        sendCultivatorStatsToClient(player, target);
        sendCultivatorTechniquesToClient(player);
    }

    public static void sendCultivatorStatsToSpecificClient(PlayerEntity player, ServerPlayerEntity toSend)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> toSend);

        sendCultivatorStatsToClient(player, target);
        sendCultivatorTechniquesToSpecificClient(player, toSend);
    }

    private static void sendCultivatorStatsToClient(PlayerEntity player, PacketDistributor.PacketTarget distribute)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        // Send the cultivator's stats to the client
        CultivatorStatsPacket pack = new CultivatorStatsPacket(player.getUUID(), stats);
        channel.send(distribute, pack);

        // Send the cultivator's current target to the client
        CultivatorTargetPacket pack2 = new CultivatorTargetPacket(player.getUUID(), stats.getTargetType(), stats.getTarget(), stats.getTargetID());
        channel.send(distribute, pack2);
    }

    public static void sendBodyModificationsToSpecificClient(PlayerEntity player, ServerPlayerEntity toSend)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> toSend);

        sendBodyModificationsToClient(player, target);
    }

    public static void sendBodyModificationsToClient(PlayerEntity player)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);

        sendBodyModificationsToClient(player, target);
    }

    private static void sendBodyModificationsToClient(PlayerEntity player, PacketDistributor.PacketTarget distribute)
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        // Send the cultivator's stats to the client
        BodyModificationsPacket pack = new BodyModificationsPacket(player.getUUID(), modifications);
        channel.send(distribute, pack);
    }

    public static void sendCultivatorTechniquesToClient(PlayerEntity player)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        CultivatorTechniquesPacket pack = new CultivatorTechniquesPacket(player.getUUID(), techs);
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), pack);
    }

    public static void sendCultivatorTechniquesToSpecificClient(PlayerEntity player, ServerPlayerEntity toSend)
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        CultivatorTechniquesPacket pack = new CultivatorTechniquesPacket(player.getUUID(), techs);
        channel.send(PacketDistributor.PLAYER.with(() -> toSend), pack);
    }
}
