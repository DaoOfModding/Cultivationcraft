package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.Renderers.QiSourceRenderer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;

public class DivineSenseTechnique extends Technique
{
    public DivineSenseTechnique()
    {
        name = "Divine Sense";
        elementID = Elements.noElementID;

        icon = new ResourceLocation("cultivationcraft", "textures/techniques/icons/divinesense.png");
    }

    @Override
    public CompoundNBT writeNBT()
    {
        CompoundNBT nbt = super.writeNBT();

        return nbt;
    }

    @Override
    public void readNBTData(CompoundNBT nbt)
    {
        super.readNBTData(nbt);
    }

    @Override
    public void writeBuffer(PacketBuffer buffer)
    {
        super.writeBuffer(buffer);
    }

    @Override
    public void readBufferData(PacketBuffer buffer)
    {
        super.readBufferData(buffer);
    }

    @Override
    public void renderPlayerView()
    {
        //Vector3d test = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();

        int chunks = Minecraft.getInstance().gameSettings.renderDistanceChunks-1;
        int chunkX = Minecraft.getInstance().player.getPosition().getX() >> 4;
        int chunkZ = Minecraft.getInstance().player.getPosition().getZ() >> 4;

        Chunk QiChunk = Minecraft.getInstance().world.getChunk(chunkX, chunkZ);

        // Cycle through all the chunks in render distance, rendering all the QI sources they contain
        for (int x = -chunks; x < chunks; x++)
            for (int z = -chunks; z < chunks; z++)
            {
                QiChunk = Minecraft.getInstance().world.getChunk(chunkX + x, chunkZ + z);

                for (QiSource source : ChunkQiSources.getChunkQiSources(QiChunk).getQiSources())
                    QiSourceRenderer.render(source);
            }
    }
}
