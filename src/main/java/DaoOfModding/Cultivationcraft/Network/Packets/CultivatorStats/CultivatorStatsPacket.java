package DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.Packets.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CultivatorStatsPacket extends Packet
{
    protected UUID owner;
    protected ICultivatorStats cultStats;

    public CultivatorStatsPacket(UUID ownerID, ICultivatorStats stats)
    {
        owner = ownerID;
        cultStats = stats;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        if (owner != null)
        {
            buffer.writeUUID(owner);
            buffer.writeNbt(cultStats.writeNBT());
        }
    }

    public static CultivatorStatsPacket decode(FriendlyByteBuf buffer)
    {
        CultivatorStatsPacket returnValue = new CultivatorStatsPacket(null, null);

        try
        {
            // Read in the send values
            UUID readingOwner = buffer.readUUID();

            CultivatorStats stats = new CultivatorStats();
            stats.readNBT(buffer.readNbt());

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
    protected void processPacket()
    {
        // Update the stats for the specified player
        CultivatorStats.getCultivatorStats(Minecraft.getInstance().level.getPlayerByUUID(owner)).readNBT(cultStats.writeNBT());
    }
}
