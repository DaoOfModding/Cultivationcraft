package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TechniqueInfoPacket extends Packet
{
    protected String name;
    protected int info;
    protected UUID player;

    public TechniqueInfoPacket(String langLocation, int newInfo, UUID owner)
    {
        name = langLocation;
        info = newInfo;
        player = owner;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeUtf(name);
        buffer.writeInt(info);
        buffer.writeUUID(player);
    }

    public static TechniqueInfoPacket decode(FriendlyByteBuf buffer)
    {
        TechniqueInfoPacket returnValue = new TechniqueInfoPacket("", -1, null);

        try
        {
            // Read in the sent values
            String readName = buffer.readUtf();
            int readInfo = buffer.readInt();
            UUID readPlayer = buffer.readUUID();

            return new TechniqueInfoPacket(readName, readInfo, readPlayer);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading TechniqueInfo message: " + e);
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
    protected void processPacket(ServerPlayer sender)
    {
        // Send the key press to the technique used
        Technique tech = CultivatorTechniques.getCultivatorTechniques(sender).getTechniqueByName(name);

        if (tech != null)
            tech.processInfo(sender, info);

        // Send the info packet to the client
        PacketHandler.sendTechniqueInfoToClients(this, sender);
    }

    // Process received packet on the Client
    protected void processPacket()
    {
        Player Player = ClientItemControl.thisWorld.getPlayerByUUID(player);

        // Send the key press to the technique used
        Technique tech = CultivatorTechniques.getCultivatorTechniques(Player).getTechniqueByName(name);

        if (tech != null)
            tech.processInfo(Player, info);
    }
}
