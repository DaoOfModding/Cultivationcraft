package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartNames;
import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

public class BodyModificationsPacket extends Packet
{
    private UUID owner;
    HashMap<String, BodyPart> modificationsParts = new HashMap<String, BodyPart>();
    String selection = "";

    public BodyModificationsPacket(UUID ownerID, IBodyModifications modifications)
    {
        owner = ownerID;

        if (modifications != null)
        {
            modificationsParts = modifications.getModifications();
            selection = modifications.getSelection();
        }
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeUniqueId(owner);
        buffer.writeString(selection);
        buffer.writeInt(modificationsParts.size());

        for (BodyPart part : modificationsParts.values())
            buffer.writeString(part.getID());
    }

    public static BodyModificationsPacket decode(PacketBuffer buffer)
    {
        BodyModificationsPacket returnValue = new BodyModificationsPacket(null, null);

        try
        {
            // Read in the sent values
            UUID readingOwner = buffer.readUniqueId();

            IBodyModifications modifications = new BodyModifications();

            modifications.setSelection(buffer.readString());

            int numberOfModifications = buffer.readInt();

            for (int i = 0; i < numberOfModifications; i++)
                modifications.setModification(BodyPartNames.getPart(buffer.readString()));

            return new BodyModificationsPacket(readingOwner, modifications);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading BodyModifications message: " + e);
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
            Cultivationcraft.LOGGER.warn("BodyModifications Packet was invalid: " + this.toString());
            return;
        }
        if (sideReceived.isClient())
            ctx.enqueueWork(() -> processPacket());
        else
            Cultivationcraft.LOGGER.warn("BodyModifications Packet was received by server, THIS SHOULD NOT HAPPEN: " + this.toString());
    }

    // Process received packet on client
    private void processPacket()
    {
        // Get the modifications for the specified player
        IBodyModifications modifications = BodyModifications.getBodyModifications(ClientItemControl.thisWorld.getPlayerByUuid(owner));

        modifications.setSelection(selection);

        for (BodyPart part : modificationsParts.values())
            modifications.setModification(part);
    }
}
