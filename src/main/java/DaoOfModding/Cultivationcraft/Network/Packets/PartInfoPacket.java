package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PartInfoPacket extends Packet
{
    private String sublimbID;
    private String limbID;
    private int info;
    private UUID player;

    public PartInfoPacket(String subLimbid, String limb, int newInfo, UUID owner)
    {
        sublimbID = subLimbid;
        limbID = limb;
        info = newInfo;
        player = owner;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeInt(info);
        buffer.writeUUID(player);
        buffer.writeUtf(sublimbID);
        buffer.writeUtf(limbID);
    }

    public static PartInfoPacket decode(FriendlyByteBuf buffer)
    {
        PartInfoPacket returnValue = new PartInfoPacket("", "", -1, null);

        try
        {
            // Read in the sent values
            int readInfo = buffer.readInt();
            UUID readPlayer = buffer.readUUID();
            String readID = buffer.readUtf();
            String limbID = buffer.readUtf();

            return new PartInfoPacket(readID, limbID, readInfo, readPlayer);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading PartInfo message: " + e);
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
            ctx.enqueueWork(() -> processPacket());
        else
            ctx.enqueueWork(() -> processPacket(ctx.getSender()));
    }

    // Process received packet on the Server
    private void processPacket(ServerPlayer sender)
    {
        BodyPart part;

        // Send the key press to the technique used
        if (sublimbID == "")
            part = BodyModifications.getBodyModifications(sender).getModification(limbID);
        else
            part = BodyModifications.getBodyModifications(sender).getOption(limbID, sublimbID);

        if (part == null)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading PartInfo message, part " + sublimbID + " doesn't exist for sender");
            return;
        }

        part.processInfo(sender, info);

        // Send the info packet to the client
        PacketHandler.sendPartInfoToClients(this, sender);
    }

    // Process received packet on the Client
    private void processPacket()
    {
        Player Player = ClientItemControl.thisWorld.getPlayerByUUID(player);

        BodyPart part;
        // Send the key press to the technique used
        if (sublimbID == "")
            part = BodyModifications.getBodyModifications(Player).getModification(limbID);
        else
            part = BodyModifications.getBodyModifications(Player).getOption(limbID, sublimbID);


        if (part == null)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading PartInfo message, part " + sublimbID + " doesn't exist for sender");
            return;
        }

        part.processInfo(Player, info);
    }
}
