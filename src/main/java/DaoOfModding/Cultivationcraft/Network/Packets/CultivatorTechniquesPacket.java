package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.ClientItemControl;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

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
    public void encode(PacketBuffer buffer)
    {
        buffer.writeUniqueId(owner);

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

    public static CultivatorTechniquesPacket decode(PacketBuffer buffer)
    {
        CultivatorTechniquesPacket returnValue = new CultivatorTechniquesPacket(null, null);

        try
        {
            ICultivatorTechniques techs = new CultivatorTechniques();

            UUID owner = buffer.readUniqueId();

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
            Cultivationcraft.LOGGER.warn("CultivatorTechniquesPacket was received by server, THIS SHOULD NOT HAPPEN: " + this.toString());
    }

    // Process received packet on client
    private void processPacket()
    {
        // Get the stats for the specified player
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(ClientItemControl.thisWorld.getPlayerByUuid(owner));

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
             techs.setTechnique(i, techniques[i]);
    }
}
