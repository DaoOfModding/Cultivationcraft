package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExternalBloodTickPacket extends Packet
{
    double x;
    double y;
    double z;
    boolean onGround;

    public ExternalBloodTickPacket(double newX, double newY, double newZ, boolean ground)
    {
        x = newX;
        y = newY;
        z = newZ;
        onGround = ground;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeBoolean(onGround);
    }

    public static ExternalBloodTickPacket decode(FriendlyByteBuf buffer)
    {
        ExternalBloodTickPacket returnValue = new ExternalBloodTickPacket(0, 0 , 0,false);

        try
        {
            // Read in the send values
            double newx = buffer.readDouble();
            double newy = buffer.readDouble();
            double newz = buffer.readDouble();
            boolean ground = buffer.readBoolean();

            return new ExternalBloodTickPacket(newx, newy, newz, ground);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading external blood tick message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived.isServer())
        {
            processPacket(ctx.getSender());
        }
        else
            Cultivationcraft.LOGGER.warn("Client sent external blood tick message");
    }

    protected void processPacket(Player player)
    {
        PlayerHealthManager.getBlood(player).externalTick(player.level, x, y, z, onGround);
    }
}
