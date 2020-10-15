package DaoOfModding.Cultivationcraft.Network;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Network.CultivatorStats.CultivatorStatsPacket;
import DaoOfModding.Cultivationcraft.Network.CultivatorStats.CultivatorTargetPacket;
import DaoOfModding.Cultivationcraft.Network.CultivatorStats.RecallFlyingSwordPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.UUID;

public class PacketHandler
{

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
        channel.registerMessage(FLYING_SWORD_NBT_ID, ConvertToFlyingPacket.class, ConvertToFlyingPacket::encode, ConvertToFlyingPacket::decode, ConvertToFlyingPacket::handle);
        channel.registerMessage(FLYING_SWORD_RECALL, RecallFlyingSwordPacket.class, RecallFlyingSwordPacket::encode, RecallFlyingSwordPacket::decode, RecallFlyingSwordPacket::handle);
        channel.registerMessage(CULTIVATOR_TARGET_ID, CultivatorTargetPacket.class, CultivatorTargetPacket::encode, CultivatorTargetPacket::decode, CultivatorTargetPacket::handle);
        channel.registerMessage(CULTIVATOR_STATS, CultivatorStatsPacket.class, CultivatorStatsPacket::encode, CultivatorStatsPacket::decode, CultivatorStatsPacket::handle);
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
        channel.send(PacketDistributor.ALL.noArg(), pack);
    }

    public static void sendCultivatorTargetToServer(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetID)
    {
        CultivatorTargetPacket pack = new CultivatorTargetPacket(playerID, type, pos, targetID);
        channel.sendToServer(pack);
    }

    public static void sendCultivatorTargetToClient(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetID)
    {
        CultivatorTargetPacket pack = new CultivatorTargetPacket(playerID, type, pos, targetID);
        channel.send(PacketDistributor.ALL.noArg(), pack);
    }

    public static void sendCultivatorStatsToClient(PlayerEntity player)
    {
        PacketDistributor.PacketTarget target = PacketDistributor.ALL.noArg();

        sendCultivatorStatsToClient(player, target);
    }

    public static void sendCultivatorStatsToSpecificClient(PlayerEntity player, ServerPlayerEntity toSend)
    {

        PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> toSend);

        sendCultivatorStatsToClient(player, target);
    }

    private static void sendCultivatorStatsToClient(PlayerEntity player, PacketDistributor.PacketTarget distribute)
    {
        CultivatorStatsPacket pack = new CultivatorStatsPacket(player.getUniqueID(), CultivatorStats.getCultivatorStats(player));
        channel.send(distribute, pack);

        // TO DO send cultivator target
    }
}
