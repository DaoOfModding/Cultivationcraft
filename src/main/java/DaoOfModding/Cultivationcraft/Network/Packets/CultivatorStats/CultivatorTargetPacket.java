package DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Network.Packets.Packet;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class CultivatorTargetPacket extends Packet
{
    protected UUID player = null;
    protected HitResult.Type targetType = HitResult.Type.MISS;
    protected Vec3 targetPos = null;
    protected UUID target = null;

    public CultivatorTargetPacket(UUID playerID, HitResult.Type type, Vec3 pos, UUID targetUUID)
    {
        player = playerID;
        targetType = type;
        targetPos = pos;
        target = targetUUID;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        if (player != null)
        {
            buffer.writeUUID(player);
            buffer.writeEnum(targetType);

            if (targetPos != null) {
                buffer.writeDouble(targetPos.x);
                buffer.writeDouble(targetPos.y);
                buffer.writeDouble(targetPos.z);

                if (targetType == HitResult.Type.ENTITY)
                    buffer.writeUUID(target);
            }
        }
    }

    public static CultivatorTargetPacket decode(FriendlyByteBuf buffer)
    {
        CultivatorTargetPacket returnValue = new CultivatorTargetPacket(null, HitResult.Type.MISS, null, null);

        try
        {
            // Read in the send values
            UUID readingPlayer = buffer.readUUID();
            HitResult.Type readingType = buffer.readEnum(HitResult.Type.class);

            Vec3 readingPos = null;
            UUID readingTargetID = null;

            // Only read the target position if there is a target
            if (readingType != HitResult.Type.MISS) {
                readingPos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());

                // Only read the target ID if target is an entity
                if (readingType == HitResult.Type.ENTITY)
                    readingTargetID = buffer.readUUID();
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
            if (ctx.getSender().getUUID().compareTo(player) != 0)
                Cultivationcraft.LOGGER.warn("Client sent target message for other player");
            else
                ctx.enqueueWork(() -> processServerPacket());
        }
        else
            ctx.enqueueWork(() -> processClientPacket());
    }


    // Process received packet on the Server
    protected void processServerPacket()
    {
        // Grab the player entity based on the read UUID
        Player ownerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(player);

        // Process the packet
        processPacket(ownerEntity);

        // Send packet to all clients
        PacketHandler.sendCultivatorTargetToClient(player, targetType, targetPos, target);
    }

    protected void processClientPacket()
    {
        processPacket(Minecraft.getInstance().level.getPlayerByUUID(player));
    }

    // Process received packet
    protected void processPacket(Player ownerEntity)
    {
        // Grab the stats of the supplied player, if they exist, and set the new target
        if (ownerEntity != null)
        {
            ICultivatorStats stats = CultivatorStats.getCultivatorStats(ownerEntity);

            stats.setTarget(targetPos, targetType, ownerEntity.level, target);
        }
    }
}
