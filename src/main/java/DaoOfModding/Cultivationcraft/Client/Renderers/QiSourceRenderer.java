package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Client.Particles.QiParticle;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class QiSourceRenderer
{
    protected static float tick = 0;
    public static QiSource qisource = null;
    public static Entity target = null;
    public static Element element = Elements.getElement(Elements.noElement);

    public static void renderQiSources(float partialTick)
    {
        int LevelChunks = Minecraft.getInstance().options.renderDistance().get()-1;
        int LevelChunkX = genericClientFunctions.getPlayer().blockPosition().getX() >> 4;
        int LevelChunkZ = genericClientFunctions.getPlayer().blockPosition().getZ() >> 4;

        LevelChunk QiLevelChunk;

        // Cycle through all the LevelChunks in render distance, rendering all the Qi sources they contain
        for (int x = -LevelChunks; x < LevelChunks; x++)
            for (int z = -LevelChunks; z < LevelChunks; z++)
            {
                QiLevelChunk = Minecraft.getInstance().level.getChunk(LevelChunkX + x, LevelChunkZ + z);

                for (QiSource source : ChunkQiSources.getChunkQiSources(QiLevelChunk).getQiSources())
                    QiSourceRenderer.render(source);
            }

        tick += partialTick;

        if (tick >= (QiSource.minSpawnTime+QiSource.maxSpawnTime)*(QiSource.minSpawnTime+QiSource.maxSpawnTime))
            tick = 0;
    }

    // Return a random player absorbing from this source, weighted by the amount they are absorbing
    protected static Player getRandomAbsorbingPlayer(QiSource source)
    {
        HashMap<UUID, Double> players = source.getAbsorbingPlayers();

        // Return null if no players are absorbing from this source
        if (players.size() == 0)
            return null;

        // If only one player is absorbing return that player
        if (players.size() == 1)
            return Minecraft.getInstance().level.getPlayerByUUID((UUID)players.keySet().toArray()[0]);

        int absorbingAmount = 0;

        for (Double absorbing : players.values())
            absorbingAmount += absorbing;

        // Subtract a random amount from the total amount of Qi being absorbed
        absorbingAmount -= new Random().nextInt(absorbingAmount);

        // Go through every absorbing player, subtracting the amount they are absorbing from the randomly chosen number
        // Until it hits 0
        for (Map.Entry<UUID, Double> entry : players.entrySet())
        {
            absorbingAmount -= entry.getValue();

            if (absorbingAmount <= 0)
                return Minecraft.getInstance().level.getPlayerByUUID(entry.getKey());
        }

        // THIS SHOULD NOT HAPPEN
        return null;
    }

    public static void render(QiSource source)
    {
        if (source.spawnedTick == (int)(tick + 1))
            return;

        if ((int)(tick + 1) % (source.getSpawnTick() + 1 + QiSource.maxSpawnTime) > 0)
            return;

        source.spawnedTick = (int)(tick + 1);

        Random random = new Random();

        double x = source.getPos().getX() + (random.nextDouble() - 0.5) * 0.2;
        double y = source.getPos().getY() + (random.nextDouble() - 0.5) * 0.2;
        double z = source.getPos().getZ() + (random.nextDouble() - 0.5) * 0.2;

        Vec3 direction;

        Player absorbing = getRandomAbsorbingPlayer(source);

        // If a player is absorbing from this QiSource then particles move towards one of the players
        if (absorbing != null)
        {
            float height = PoseHandler.getPlayerPoseHandler(absorbing.getUUID()).getPlayerModel().getHeightAdjustment();
            direction = absorbing.position().add(0, height, 0).subtract(source.getPos().getX(), source.getPos().getY(), source.getPos().getZ());
        }
        // If not player is absorbing from this QiSource then particles move in random directions
        else
        {
            direction = new Vec3(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
        }

        direction = direction.normalize();

        qisource = source;
        target = absorbing;
        element = Elements.getElement(source.getElement());

        Minecraft.getInstance().level.addParticle(Register.qiParticleType.get(), x, y, z, direction.x, direction.y, direction.z);

        qisource = null;
        target = null;
        element = Elements.getElement(Elements.noElement);
    }
}
