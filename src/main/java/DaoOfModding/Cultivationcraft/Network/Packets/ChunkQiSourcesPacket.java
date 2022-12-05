package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.AddChunkQiSourceToClient;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ChunkQiSourcesPacket extends Packet
{
    ChunkPos ChunkPos;
    List<QiSource> QiSources;

    public ChunkQiSourcesPacket(ChunkPos LevelChunk,  List<QiSource> Qi)
    {
        ChunkPos = LevelChunk;
        QiSources = Qi;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        if (ChunkPos != null)
        {
            buffer.writeLong(ChunkPos.toLong());

            // Write the number of QiSources to loop through
            buffer.writeInt(QiSources.size());

            // Add data for each QiSource
            for (QiSource source : QiSources)
            {
                source.WriteBuffer(buffer);
            }
        }
    }

    public static ChunkQiSourcesPacket decode(FriendlyByteBuf buffer)
    {
        ChunkQiSourcesPacket returnValue = new ChunkQiSourcesPacket(null, null);

        try
        {
            // Read in the send values
            ChunkPos LevelChunk = new ChunkPos(buffer.readLong());

            int numberOfSources = buffer.readInt();

            List<QiSource> NewQiSources = new ArrayList<>();

            for (int i = 0; i < numberOfSources; i++)
                NewQiSources.add(QiSource.ReadBuffer(buffer));

            return new ChunkQiSourcesPacket(LevelChunk, NewQiSources);

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

    // Try to process received packet on the client, will fail if the LevelChunk is not yet loaded
    public boolean processPacket()
    {
        // Disregard this packet if the world is unloaded
        if (Minecraft.getInstance().level == null)
            return true;

        // If the current LevelChunk isn't loaded return false
        if (!Minecraft.getInstance().level.getChunkSource().hasChunk(ChunkPos.x, ChunkPos.z))
            return false;

        // Get the specified LevelChunk from the world
        LevelChunk LevelChunk = Minecraft.getInstance().level.getChunk(ChunkPos.x, ChunkPos.z);

        // Get the ChunkQiSources instance from the LevelChunk
        IChunkQiSources sources = ChunkQiSources.getChunkQiSources(LevelChunk);

        // Set the new values
        sources.setChunkPos(new ChunkPos(ChunkPos.x, ChunkPos.z));
        sources.setQiSources(new ArrayList(QiSources));

        return true;
    }
}
