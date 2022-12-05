package DaoOfModding.Cultivationcraft.Network.Packets;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Packet {

    public Packet() {

    }

    // Called once the message is sent, encodes data into the buffer to send
    public void encode(FriendlyByteBuf buffer)
    {
    }

    // Called once a message of this type is received, put code to handle the received message here
    // public static Packet decode(FriendlyByteBuf buffer)

    public void handle(Supplier<NetworkEvent.Context> context)
    {
        /*
        Player player

        if (player.world.isRemote)
            handleClient(player);
        else
            handleServer(player);*/
    }

    public void handleClient(Player player)
    {

    }

    public void handleServer(Player player)
    {

    }

}
