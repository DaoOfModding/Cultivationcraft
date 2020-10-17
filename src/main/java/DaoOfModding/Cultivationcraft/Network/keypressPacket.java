package DaoOfModding.Cultivationcraft.Network;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Register;
import DaoOfModding.Cultivationcraft.Server.ServerItemControl;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class keypressPacket extends Packet
{
    Register.keyPresses keyPress;

    public keypressPacket(Register.keyPresses keyPressed)
    {
        keyPress = keyPressed;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeEnumValue(keyPress);
    }

    public static keypressPacket decode(PacketBuffer buffer)
    {
        keypressPacket returnValue = new keypressPacket(null);

        try
        {
            // Read in the sent values
            Register.keyPresses readPress = buffer.readEnumValue(Register.keyPresses.class);

            return new keypressPacket(readPress);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading keypress message: " + e);
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
            Cultivationcraft.LOGGER.warn("keypressPacket was received by client - This should not happen");
            return;
        }

        // Check to ensure that the packet has valid data values
        if (keyPress == null)
        {
            Cultivationcraft.LOGGER.warn("keypressPacket was invalid: " + this.toString());
            return;
        }

        ctx.enqueueWork(() -> processPacket(ctx.getSender()));
    }

    // Process received packet on the Server
    private void processPacket(ServerPlayerEntity sender)
    {
        ServerItemControl.handleKeyPress(keyPress, sender);
    }
}
