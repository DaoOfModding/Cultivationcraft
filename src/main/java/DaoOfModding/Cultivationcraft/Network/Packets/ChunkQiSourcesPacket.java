package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.AddChunkQiSourceToClient;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ChunkQiSourcesPacket extends Packet
{
    ChunkPos chunkPos;
    List<QiSource> QiSources;

    public ChunkQiSourcesPacket(ChunkPos chunk,  List<QiSource> Qi)
    {
        chunkPos = chunk;
        QiSources = Qi;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        if (chunkPos != null)
        {
            buffer.writeLong(chunkPos.asLong());

            // Write the number of QiSources to loop through
            buffer.writeInt(QiSources.size());

            // Add data for each QiSource
            for (QiSource source : QiSources)
            {
                source.WriteBuffer(buffer);
            }
        }
    }

    public static ChunkQiSourcesPacket decode(PacketBuffer buffer)
    {
        ChunkQiSourcesPacket returnValue = new ChunkQiSourcesPacket(null, null);

        try
        {
            // Read in the send values
            ChunkPos chunk = new ChunkPos(buffer.readLong());

            int numberOfSources = buffer.readInt();

            List<QiSource> NewQiSources = new ArrayList<>();

            for (int i = 0; i < numberOfSources; i++)
                NewQiSources.add(QiSource.ReadBuffer(buffer));

            return new ChunkQiSourcesPacket(chunk, NewQiSources);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading ChunkQiSource message: " + e);
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
            Cultivationcraft.LOGGER.warn("ChunkQiSourcePacket was received by server - This should not happen");
            return;
        }

        ctx.enqueueWork(() -> setToProcess());
    }

    // Set this packet to try and process, will repeat until successful
    private void setToProcess()
    {
        AddChunkQiSourceToClient.addPacket(this);
    }

    // Try to process received packet on the client, will fail if the chunk is not yet loaded
    public boolean processPacket()
    {
        // If the current chunk isn't loaded return false
        if (!Minecraft.getInstance().world.getChunkProvider().isChunkLoaded(chunkPos))
            return false;

        // Get the specified chunk from the world
        Chunk chunk = Minecraft.getInstance().world.getChunk(chunkPos.x, chunkPos.z);

        // Get the ChunkQiSources instance from the chunk
        IChunkQiSources sources = ChunkQiSources.getChunkQiSources(chunk);

        // Set the new values
        sources.setChunkPos(new ChunkPos(chunkPos.x, chunkPos.z));
        sources.setQiSources(new ArrayList(QiSources));

        return true;
    }
}
