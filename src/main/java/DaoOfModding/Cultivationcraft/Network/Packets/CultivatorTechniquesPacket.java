package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Server.ServerItemControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CultivatorTechniquesPacket extends Packet
{
    UUID owner;
    Technique[] techniques = new Technique[CultivatorTechniques.numberOfTechniques];

    public CultivatorTechniquesPacket(UUID player, ICultivatorTechniques techs)
    {
        owner = player;

        if (techs != null)
            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                techniques[i] = techs.getTechnique(i);
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeUUID(owner);

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
        {
            if(techniques[i] != null)
            {
                buffer.writeBoolean(true);
                techniques[i].writeBuffer(buffer);
            }
            else
                buffer.writeBoolean(false);
        }
    }

    public static CultivatorTechniquesPacket decode(FriendlyByteBuf buffer)
    {
        CultivatorTechniquesPacket returnValue = new CultivatorTechniquesPacket(null, null);

        try
        {
            ICultivatorTechniques techs = new CultivatorTechniques();

            UUID owner = buffer.readUUID();

            for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                if (buffer.readBoolean())
                    techs.setTechnique(i, Technique.readBuffer(buffer));
                else
                    techs.setTechnique(i, null);

            return new CultivatorTechniquesPacket(owner, techs);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading CultivatorTechniques message: " + e);
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
            Cultivationcraft.LOGGER.warn("CultivatorTechniquesPacket was invalid: " + this.toString());
            return;
        }

        if (sideReceived.isClient())
            ctx.enqueueWork(() -> processPacket());
        else
        {
            if (owner.compareTo(ctx.getSender().getUUID()) != 0)
            {
                Cultivationcraft.LOGGER.warn("Client sent CultivatorTechniquesPacket for a different player: " + this.toString());
                return;
            }
            ctx.enqueueWork(() -> processPacketServer(ctx.getSender()));
        }
    }

    // Process received packet on client
    protected void processPacket()
    {
        // Get the stats for the specified player
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(ClientItemControl.thisWorld.getPlayerByUUID(owner));

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
             techs.setTechnique(i, techniques[i]);
    }

    // Process received packet on server
    protected void processPacketServer(Player player)
    {
        // Get the stats for the specified player
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        // Clear the stored techniques
        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            techs.setTechnique(i, null);

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
        {
            // Ensure the technique is valid
            // Only add multiple copies of a technique if the technique allows it
            if (techniques[i] == null || (techniques[i].isValid(player) && (techniques[i].allowMultiple() || !techs.techniqueExists(techniques[i]))))
            {
                if (techs.getTechnique(i) != null && techs.getTechnique(i).isActive() && techs.getTechnique(i).getClass() != techniques[i].getClass())
                    techs.getTechnique(i).deactivate(player);

                techs.setTechnique(i, techniques[i]);
            }
            else
                Cultivationcraft.LOGGER.warn("Client tried to set invalid technique: " + techniques[i].toString());
        }

        // Send the updated techniques to clients
        PacketHandler.sendCultivatorTechniquesToClient(player);
    }
}
