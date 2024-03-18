package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.Renderers.BloodRenderer;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BloodPacket extends Packet
{
    protected UUID player;
    Vec3 source;
    double amount;

    public BloodPacket(UUID playerID, double damage, Vec3 sourcePosition)
    {
        player = playerID;
        amount = damage;
        source = sourcePosition;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        if (player != null)
        {
            buffer.writeUUID(player);
            buffer.writeDouble(amount);

            if (source != null)
            {
                buffer.writeBoolean(true);
                buffer.writeDouble(source.x);
                buffer.writeDouble(source.y);
                buffer.writeDouble(source.z);
            }
            else
                buffer.writeBoolean(false);
        }
    }

    public static BloodPacket decode(FriendlyByteBuf buffer)
    {
        BloodPacket returnValue = new BloodPacket(null, 0 , null);

        try
        {
            // Read in the send values
            UUID readingPlayer = buffer.readUUID();
            double damage = buffer.readDouble();
            Vec3 sourcePosition = null;

            if (buffer.readBoolean())
                sourcePosition = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());

            return new BloodPacket(readingPlayer, damage, sourcePosition);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading Blood spawn message: " + e);
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
            Cultivationcraft.LOGGER.warn("Client sent blood spawn message");
        }
        else
            ctx.enqueueWork(() -> processClientPacket());
    }

    protected void processClientPacket()
    {
        BloodRenderer.spawnBlood(Minecraft.getInstance().level.getPlayerByUUID(player), source, amount);
    }
}
