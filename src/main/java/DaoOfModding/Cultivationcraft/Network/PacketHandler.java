package DaoOfModding.Cultivationcraft.Network;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Network.Packets.*;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.*;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.Packets.keypressPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class PacketHandler
{

    private static final byte KEYPRESS = 07;
    private static final byte FLYING_SWORD_NBT_ID = 35;
    private static final byte FLYING_SWORD_RECALL = 36;
    private static final byte CULTIVATOR_TARGET_ID = 76;
    private static final byte CULTIVATOR_STATS = 99;
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("examplemod", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init()
    {
        channel.registerMessage(KEYPRESS, keypressPacket.class, keypressPacket::encode, keypressPacket::decode, keypressPacket::handle);
        channel.registerMessage(FLYING_SWORD_NBT_ID, ConvertToFlyingPacket.class, ConvertToFlyingPacket::encode, ConvertToFlyingPacket::decode, ConvertToFlyingPacket::handle);
        channel.registerMessage(FLYING_SWORD_RECALL, RecallFlyingSwordPacket.class, RecallFlyingSwordPacket::encode, RecallFlyingSwordPacket::decode, RecallFlyingSwordPacket::handle);
        channel.registerMessage(CULTIVATOR_TARGET_ID, CultivatorTargetPacket.class, CultivatorTargetPacket::encode, CultivatorTargetPacket::decode, CultivatorTargetPacket::handle);
        channel.registerMessage(CULTIVATOR_STATS, CultivatorStatsPacket.class, CultivatorStatsPacket::encode, CultivatorStatsPacket::decode, CultivatorStatsPacket::handle);
    }

    public static void sendKeypressToServer(Register.keyPresses keyPress)
    {
        keypressPacket pack = new keypressPacket(keyPress);
        channel.sendToServer(pack);
    }

    public static void sendFlyingSwordConversionToServer(int heldItemID, UUID owner)
    {
        ConvertToFlyingPacket pack = new ConvertToFlyingPacket(heldItemID, owner);
        channel.sendToServer(pack);
    }

    public static void sendRecallFlyingToServer(boolean recall, UUID playerID)
    {
        RecallFlyingSwordPacket pack = new RecallFlyingSwordPacket(recall, playerID);
        channel.sendToServer(pack);
    }

    public static void sendRecallFlyingToClient(boolean recall, UUID playerID)
    {
        RecallFlyingSwordPacket pack = new RecallFlyingSwordPacket(recall, playerID);
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(playerID)), pack);
    }

    public static void sendCultivatorTargetToServer(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetID)
    {
        CultivatorTargetPacket pack = new CultivatorTargetPacket(playerID, type, pos, targetID);
        channel.sendToServer(pack);
    }

    public static void sendCultivatorTargetToClient(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetID)
    {
        CultivatorTargetPacket pack = new CultivatorTargetPacket(playerID, type, pos, targetID);
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(playerID)), pack);
    }

    public static void sendCultivatorStatsToClient(PlayerEntity player)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player);

        sendCultivatorStatsToClient(player, target);
    }

    public static void sendCultivatorStatsToSpecificClient(PlayerEntity player, ServerPlayerEntity toSend)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> toSend);

        sendCultivatorStatsToClient(player, target);
    }

    private static void sendCultivatorStatsToClient(PlayerEntity player, PacketDistributor.PacketTarget distribute)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        // Send the cultivator's stats to the client
        CultivatorStatsPacket pack = new CultivatorStatsPacket(player.getUniqueID(), stats);
        channel.send(distribute, pack);

        // Send the cultivator's current target to the client
        CultivatorTargetPacket pack2 = new CultivatorTargetPacket(player.getUniqueID(), stats.getTargetType(), stats.getTarget(), stats.getTargetID());
        channel.send(distribute, pack2);
    }
}
