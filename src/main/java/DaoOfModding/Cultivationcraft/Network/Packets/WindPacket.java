package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Qi.Effects.Wind;
import DaoOfModding.Cultivationcraft.Common.Qi.Effects.WindInstance;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class WindPacket extends Packet
{
    WindInstance instance;
    UUID entity;

    public WindPacket(WindInstance wind, UUID entityID)
    {
        instance = wind;
        entity = entityID;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeFloat(instance.getSpeed());
        buffer.writeFloat(instance.getStrength());
        buffer.writeDouble(instance.getDirection().x);
        buffer.writeDouble(instance.getDirection().y);
        buffer.writeDouble(instance.getDirection().z);
        buffer.writeUUID(entity);
    }

    public static WindPacket decode(FriendlyByteBuf buffer)
    {
        WindPacket returnValue = new WindPacket(null, null);

        try
        {
            // Read in the send values
            float speed = buffer.readFloat();
            float strength = buffer.readFloat();
            Vec3 direction = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            UUID entityID = buffer.readUUID();

            return new WindPacket(new WindInstance(direction, speed, strength), entityID);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading WindPacket message: " + e);
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
            Cultivationcraft.LOGGER.warn("Client sent WindInstance message");
        }
        else
        {
            ctx.enqueueWork(() -> processPacket());
        }
    }

    // Process received packet on the Client
    protected void processPacket()
    {
        Wind.addWindEffectClient(entity, instance);
    }
}
