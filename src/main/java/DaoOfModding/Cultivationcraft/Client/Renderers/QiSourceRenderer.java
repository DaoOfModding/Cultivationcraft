package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Client.Particles.QiParticleData;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import java.util.Random;

public class QiSourceRenderer
{
    // How fast the Qi Particles should move a tick
    public static final double speed = 0.25;

    public static void renderQiSources()
    {
        int LevelChunks = Minecraft.getInstance().options.renderDistance().get()-1;
        int LevelChunkX = genericClientFunctions.getPlayer().blockPosition().getX() >> 4;
        int LevelChunkZ = genericClientFunctions.getPlayer().blockPosition().getZ() >> 4;

        LevelChunk QiLevelChunk;

        // Cycle through all the LevelChunks in render distance, rendering all the QI sources they contain
        for (int x = -LevelChunks; x < LevelChunks; x++)
            for (int z = -LevelChunks; z < LevelChunks; z++)
            {
                QiLevelChunk = Minecraft.getInstance().level.getChunk(LevelChunkX + x, LevelChunkZ + z);

                for (QiSource source : ChunkQiSources.getChunkQiSources(QiLevelChunk).getQiSources())
                    QiSourceRenderer.render(source);
            }
    }

    public static void render(QiSource source)
    {
        double x = source.getPos().getX();
        double y = source.getPos().getY();
        double z = source.getPos().getZ();

        Random random = new Random();

            Vec3 direction = new Vec3(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
            direction.normalize();
            direction = direction.scale(speed);

            QiParticleData particle = new QiParticleData(source);
            Minecraft.getInstance().level.addParticle(particle, x, y, z, direction.x, direction.y, direction.z);
    }
}
