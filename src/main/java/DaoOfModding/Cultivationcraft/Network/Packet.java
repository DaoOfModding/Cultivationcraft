package DaoOfModding.Cultivationcraft.Network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class Packet {

    public Packet() {

    }

    // Called once the message is sent, encodes data into the buffer to send
    public void encode(PacketBuffer buffer)
    {
    }

    // Called once a message of this type is received, put code to handle the received message here
    // public static Packet decode(PacketBuffer buffer)

    public void handle(Supplier<NetworkEvent.Context> context)
    {
        /*
        PlayerEntity player

        if (player.world.isRemote)
            handleClient(player);
        else
            handleServer(player);*/
    }

    public void handleClient(PlayerEntity player)
    {

    }

    public void handleServer(PlayerEntity player)
    {

    }

}
