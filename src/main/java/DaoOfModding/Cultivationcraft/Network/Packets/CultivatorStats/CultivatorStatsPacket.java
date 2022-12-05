package DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.Packets.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CultivatorStatsPacket extends Packet
{
    private UUID owner;

    private double flyingItemSpeed;
    private double flyingItemMAXSpeed;
    private double flyingItemturnSpeed;
    private double flyingItemControlRange;

    public CultivatorStatsPacket(UUID ownerID, ICultivatorStats stats)
    {
        owner = ownerID;

        if (stats != null)
        {
            flyingItemSpeed = stats.getFlyingItemSpeed();
            flyingItemMAXSpeed = stats.getFlyingItemMaxSpeed();
            flyingItemturnSpeed = stats.getFlyingItemTurnSpeed();
            flyingItemControlRange = stats.getFlyingControlRange();
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        if (owner != null)
        {
            buffer.writeUUID(owner);
            buffer.writeDouble(flyingItemSpeed);
            buffer.writeDouble(flyingItemMAXSpeed);
            buffer.writeDouble(flyingItemturnSpeed);
            buffer.writeDouble(flyingItemControlRange);
        }
    }

    public static CultivatorStatsPacket decode(FriendlyByteBuf buffer)
    {
        CultivatorStatsPacket returnValue = new CultivatorStatsPacket(null, null);

        try
        {
            // Read in the send values
            UUID readingOwner = buffer.readUUID();

            ICultivatorStats stats = new CultivatorStats();
            stats.setFlyingItemSpeed(buffer.readDouble());
            stats.setFlyingItemMaxSpeed(buffer.readDouble());
            stats.setFlyingItemTurnSpeed(buffer.readDouble());
            stats.setFlyingControlRange(buffer.readDouble());

            return new CultivatorStatsPacket(readingOwner, stats);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading CultivatorStats message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        // Check to ensure that the packet has valid data values
        if (owner == null)
        {
            Cultivationcraft.LOGGER.warn("CultivatorStatsPacket was invalid: " + this.toString());
            return;
        }
        if (sideReceived.isClient())
            ctx.enqueueWork(() -> processPacket());
        else
            Cultivationcraft.LOGGER.warn("CultivatorStatsPacket was received by server, THIS SHOULD NOT HAPPEN: " + this.toString());
    }

    // Process received packet on client
    private void processPacket()
    {
        // Get the stats for the specified player
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(ClientItemControl.thisWorld.getPlayerByUUID(owner));

        stats.setFlyingItemSpeed(flyingItemSpeed);
        stats.setFlyingItemMaxSpeed(flyingItemMAXSpeed);
        stats.setFlyingItemTurnSpeed(flyingItemturnSpeed);
        stats.setFlyingControlRange(flyingItemControlRange);
    }
}
