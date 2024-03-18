package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Client.AddChunkQiSourceToClient;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.IChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.CommonListeners;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ChunkQiSourcesPacket extends Packet
{
    IChunkQiSources chunkQiSources;

    public ChunkQiSourcesPacket(IChunkQiSources sources)
    {
        chunkQiSources = sources;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeNbt(chunkQiSources.writeNBT());
    }

    public static ChunkQiSourcesPacket decode(FriendlyByteBuf buffer)
    {
        ChunkQiSourcesPacket returnValue = new ChunkQiSourcesPacket(null);

        try
        {
            IChunkQiSources testSource = new ChunkQiSources();
            testSource.readNBT(buffer.readNbt());

            return new ChunkQiSourcesPacket(testSource);

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
    protected void setToProcess()
    {
        AddChunkQiSourceToClient.addPacket(this);
    }

    // Try to process received packet on the client, will fail if the LevelChunk is not yet loaded
    public boolean processPacket()
    {
        // Disregard this packet if the world is unloaded
        if (Minecraft.getInstance().level == null)
            return true;

        // Disregard this packet if the current LevelChunk isn't from the right dimension
        if (Minecraft.getInstance().level.dimension().location().compareTo(chunkQiSources.getDimension()) != 0)
            return true;

        // If the current LevelChunk isn't loaded return false
        if (!Minecraft.getInstance().level.getChunkSource().hasChunk(chunkQiSources.getChunkPos().x, chunkQiSources.getChunkPos().z))
            return false;

        // Get the specified LevelChunk from the world
        LevelChunk LevelChunk = Minecraft.getInstance().level.getChunk(chunkQiSources.getChunkPos().x, chunkQiSources.getChunkPos().z);

        // Get the ChunkQiSources instance from the LevelChunk
        IChunkQiSources sources = ChunkQiSources.getChunkQiSources(LevelChunk);

        // Set the new values
        sources.readNBT(chunkQiSources.writeNBT());

        CommonListeners.checkQiSourceIsTicking(sources);

        return true;
    }
}
