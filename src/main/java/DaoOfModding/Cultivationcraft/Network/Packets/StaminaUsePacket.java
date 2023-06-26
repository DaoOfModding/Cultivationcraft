package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StaminaUsePacket extends Packet
{
    protected float stamina;

    public StaminaUsePacket(float staminaUse)
    {
        stamina = staminaUse;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeFloat(stamina);
    }

    public static StaminaUsePacket decode(FriendlyByteBuf buffer)
    {
        StaminaUsePacket returnValue = new StaminaUsePacket(0);

        try
        {
            // Read in the sent values
            float stam = buffer.readFloat();

            return new StaminaUsePacket(stam);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading TechniqueUse message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (!sideReceived.isServer())
        {
            Cultivationcraft.LOGGER.warn("StaminaUsePacket was received by client - This should not happen");
            return;
        }

        // Check to ensure that the packet has valid data values
        if (stamina < 0)
        {
            Cultivationcraft.LOGGER.warn("StaminaUsePacker tried to recover stamina: " + this.toString());
            return;
        }

        ctx.enqueueWork(() -> processPacket(ctx.getSender()));
    }

    // Process received packet on the Server
    protected void processPacket(ServerPlayer sender)
    {
        StaminaHandler.consumeStamina(sender, stamina);
    }
}

