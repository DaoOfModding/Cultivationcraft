package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Client.Particles.QiParticleData;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

public class QiSourceRenderer
{
    // How fast the Qi Particles should move a tick
    public static final double speed = 0.25;

    public static void renderQiSources()
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

    public static void render(QiSource source)
    {
        double x = source.getPos().getX();
        double y = source.getPos().getY();
        double z = source.getPos().getZ();

        Random random = new Random();

            Vector3d direction = new Vector3d(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
            direction.normalize();
            direction = direction.scale(speed);

            QiParticleData particle = new QiParticleData(source);
            Minecraft.getInstance().world.addParticle(particle, x, y, z, direction.x, direction.y, direction.z);
    }
}
