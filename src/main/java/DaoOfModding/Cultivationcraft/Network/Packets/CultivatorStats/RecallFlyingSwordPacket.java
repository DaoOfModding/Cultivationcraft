package DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.Packets.Packet;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class RecallFlyingSwordPacket extends Packet
{
    boolean recallOn;
    UUID owner;

    public RecallFlyingSwordPacket(boolean recall, UUID newOwner)
    {
        recallOn = recall;
        owner = newOwner;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        if (owner != null)
        {
            buffer.writeBoolean(recallOn);
            buffer.writeUUID(owner);
        }
    }

    public static RecallFlyingSwordPacket decode(FriendlyByteBuf buffer)
    {
        RecallFlyingSwordPacket returnValue = new RecallFlyingSwordPacket(false, null);

        try
        {
            // Read in the send values
            Boolean readingRecall = buffer.readBoolean();
            UUID readingOwner = buffer.readUUID();

            return new RecallFlyingSwordPacket(readingRecall, readingOwner);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading RecallFlyingSword message: " + e);
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
            Cultivationcraft.LOGGER.warn("RecallFlyingSwordPacket was invalid: " + this.toString());
            return;
        }
        if (sideReceived.isServer())
        {
            if (ctx.getSender().getUUID().compareTo(owner) != 0)
                Cultivationcraft.LOGGER.warn("Client sent recall message for other player - Client=" + ctx.getSender().getUUID().toString() + " Player=" + owner.toString());
            else
                ctx.enqueueWork(() -> processServerPacket());
        }
        else
            ctx.enqueueWork(() -> processClientPacket());
    }

    // Process received packet on the Server
    protected void processServerPacket()
    {
        PacketHandler.sendRecallFlyingToClient(recallOn, owner);

        // Grab the player entity based on the read UUID
        Player ownerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(owner);

        processPacket(ownerEntity);
    }

    protected void processClientPacket()
    {
        // Grab the player entity based on the read UUID
        Player ownerEntity = Minecraft.getInstance().level.getPlayerByUUID(owner);

        if (ownerEntity != null)
            processPacket(ownerEntity);
    }

    // Process received packet
    protected void processPacket(Player ownerEntity)
    {
        // Set the player's flying sword recall to the specified value
        CultivatorStats.getCultivatorStats(ownerEntity).setRecall(recallOn);
    }
}
