package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class ConvertToFlyingPacket extends Packet
{
    int heldItem;
    PlayerEntity owner;

    public ConvertToFlyingPacket(int heldItemID)
    {
        heldItem = heldItemID;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        if (heldItem > -1)
        {
            buffer.writeInt(heldItem);
        }
    }

    public static ConvertToFlyingPacket decode(PacketBuffer buffer)
    {
        ConvertToFlyingPacket returnValue = new ConvertToFlyingPacket(-1);

        try
        {
            // Read in the send values
            int readingHeld = buffer.readInt();

            return new ConvertToFlyingPacket(readingHeld);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading ConvertToFlying message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived.isClient())
        {
            Cultivationcraft.LOGGER.warn("ConvertToFlyingPacket was received by client - This should not happen");
            return;
        }

        // Check to ensure that the packet has valid data values
        if (heldItem == -1)
        {
            Cultivationcraft.LOGGER.warn("ConvertToFlyingPacket was invalid: " + this.toString());
            return;
        }

        owner = ctx.getSender();
        ctx.enqueueWork(() -> processPacket());
    }

    // Process received packet on the Server
    private void processPacket()
    {
        // Convert the specified item to the flying item owned by the specified player entity
        FlyingSwordController.addFlyingItem(owner.inventory.getStackInSlot(heldItem), owner.getUniqueID());
    }
}
