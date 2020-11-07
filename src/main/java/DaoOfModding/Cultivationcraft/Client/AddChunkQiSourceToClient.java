package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Network.Packets.ChunkQiSourcesPacket;

import java.util.ArrayList;

public class AddChunkQiSourceToClient
{
    private static ArrayList<ChunkQiSourcesPacket> packets = new ArrayList<ChunkQiSourcesPacket>();

    public static void addPacket(ChunkQiSourcesPacket packet)
    {
        packets.add(packet);
    }

    // Go through all packets in the list, trying to process them
    // Remove them from the list if they are successfully processed
    public static void processPackets()
    {
        ArrayList<ChunkQiSourcesPacket> tempPackets = new ArrayList(packets);

        for (ChunkQiSourcesPacket packet : tempPackets)
            if (packet.processPacket())
                packets.remove(packet);
    }
}
