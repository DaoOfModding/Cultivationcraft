package DaoOfModding.Cultivationcraft.Network;

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
    UUID owner;

    public ConvertToFlyingPacket(int heldItemID, UUID newOwner)
    {
        heldItem = heldItemID;
        owner = newOwner;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        if (heldItem > -1 && owner != null)
        {
            buffer.writeInt(heldItem);
            buffer.writeUniqueId(owner);
        }
    }

    public static ConvertToFlyingPacket decode(PacketBuffer buffer)
    {
        ConvertToFlyingPacket returnValue = new ConvertToFlyingPacket(-1, null);

        try
        {
            // Read in the send values
            int readingHeld = buffer.readInt();
            UUID readingOwner = buffer.readUniqueId();

            return new ConvertToFlyingPacket(readingHeld, readingOwner);

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
        if (heldItem == -1 || owner == null)
        {
            Cultivationcraft.LOGGER.warn("ConvertToFlyingPacket was invalid: " + this.toString());
            return;
        }

        ctx.enqueueWork(() -> processPacket());
    }

    // Process received packet on the Server
    private void processPacket()
    {
        // Grab the player entity based on the read UUID
        PlayerEntity ownerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(owner);

        // Convert the specified item to the flying item owned by the specified player entity
        FlyingSwordController.addFlyingItem(ownerEntity.inventory.getStackInSlot(heldItem), owner);
    }
}
