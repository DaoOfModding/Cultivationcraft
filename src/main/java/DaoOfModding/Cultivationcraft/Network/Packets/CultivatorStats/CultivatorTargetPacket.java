package DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Network.Packets.Packet;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class CultivatorTargetPacket extends Packet
{
    private UUID player = null;
    private RayTraceResult.Type targetType = RayTraceResult.Type.MISS;
    private Vector3d targetPos = null;
    private UUID target = null;

    public CultivatorTargetPacket(UUID playerID, RayTraceResult.Type type, Vector3d pos, UUID targetUUID)
    {
        player = playerID;
        targetType = type;
        targetPos = pos;
        target = targetUUID;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        if (player != null)
        {
            buffer.writeUniqueId(player);
            buffer.writeEnumValue(targetType);

            if (targetPos != null) {
                buffer.writeDouble(targetPos.x);
                buffer.writeDouble(targetPos.y);
                buffer.writeDouble(targetPos.z);

                if (targetType == RayTraceResult.Type.ENTITY)
                    buffer.writeUniqueId(target);
            }
        }
    }

    public static CultivatorTargetPacket decode(PacketBuffer buffer)
    {
        CultivatorTargetPacket returnValue = new CultivatorTargetPacket(null, RayTraceResult.Type.MISS, null, null);

        try
        {
            // Read in the send values
            UUID readingPlayer = buffer.readUniqueId();
            RayTraceResult.Type readingType = buffer.readEnumValue(RayTraceResult.Type.class);

            Vector3d readingPos = null;
            UUID readingTargetID = null;

            // Only read the target position if there is a target
            if (readingType != RayTraceResult.Type.MISS) {
                readingPos = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());

                // Only read the target ID if target is an entity
                if (readingType == RayTraceResult.Type.ENTITY)
                    readingTargetID = buffer.readUniqueId();
            }

            return new CultivatorTargetPacket(readingPlayer, readingType, readingPos, readingTargetID);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading CultivatorTarget message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived.isServer())
        {
            if (ctx.getSender().getUniqueID().compareTo(player) != 0)
                Cultivationcraft.LOGGER.warn("Client sent target message for other player");
            else
                ctx.enqueueWork(() -> processServerPacket());
        }
        else
            ctx.enqueueWork(() -> processClientPacket());
    }


    // Process received packet on the Server
    private void processServerPacket()
    {
        // Grab the player entity based on the read UUID
        PlayerEntity ownerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(player);

        // Process the packet
        processPacket(ownerEntity);

        // Send packet to all clients
        PacketHandler.sendCultivatorTargetToClient(player, targetType, targetPos, target);
    }

    private void processClientPacket()
    {
        processPacket(ClientItemControl.thisWorld.getPlayerByUuid(player));
    }

    // Process received packet
    private void processPacket(PlayerEntity ownerEntity)
    {
        // Grab the stats of the supplied player, if they exist, and set the new target
        if (ownerEntity != null)
        {
            ICultivatorStats stats = CultivatorStats.getCultivatorStats(ownerEntity);

            stats.setTarget(targetPos, targetType, ownerEntity.world, target);
        }
    }
}
