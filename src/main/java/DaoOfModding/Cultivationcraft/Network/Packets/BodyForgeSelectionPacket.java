package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BodyForgeSelectionPacket extends Packet
{
    private String selectionID;

    public BodyForgeSelectionPacket(String ID)
    {
        selectionID = ID;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeUtf(selectionID);
    }

    public static BodyForgeSelectionPacket decode(FriendlyByteBuf buffer)
    {
        BodyForgeSelectionPacket returnValue = new BodyForgeSelectionPacket("");

        try
        {
            // Read in the sent values
            String readingID = buffer.readUtf();

            return new BodyForgeSelectionPacket(readingID);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading BodyForgeSelection message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (!sideReceived.isClient())
            ctx.enqueueWork(() -> processPacket(ctx.getSender()));
        else
            Cultivationcraft.LOGGER.warn("BodyForgeSelection Packet was received by client, THIS SHOULD NOT HAPPEN: " + this.toString());
    }

    // Process received packet on server
    private void processPacket(Player player)
    {
        // Do nothing if the received selection is already selected
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);
        if (modifications.getSelection().compareTo(selectionID) == 0)
            return;

        // Reset the body forge progress
        modifications.setProgress(0);

        // Set the current selection to nothing if nothing was passed through
        if (selectionID.compareTo("") == 0)
        {
            modifications.setSelection(selectionID);
        }
        else
        {
            // Ensure that this is a valid selection for this player
            BodyPart part = BodyPartNames.getPart(selectionID);
            if (part == null)
                part = BodyPartNames.getOption(selectionID);

            if (part == null || !part.canBeForged(player)) {
                Cultivationcraft.LOGGER.warn(player.getName().getString() + " tried to forge an invalid bodyPart: " + selectionID);
                return;
            }

            // Set the new selection
            modifications.setSelection(selectionID);
        }

        // Update clients with new selection
        PacketHandler.sendBodyModificationsToClient(player);
    }
}
