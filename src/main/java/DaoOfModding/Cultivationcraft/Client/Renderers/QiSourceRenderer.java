package DaoOfModding.Cultivationcraft.Client.Renderers;

import DaoOfModding.Cultivationcraft.Client.Particles.QiParticleData;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QiSourceRenderer
{
    protected static int tick = 0;

    public static void renderQiSources()
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

        tick += 1;

        if (tick == 30)
            tick = 0;
    }

    // Return a random player absorbing from this source, weighted by the amount they are absorbing
    protected static Player getRandomAbsorbingPlayer(QiSource source)
    {
        // Return null if no players are absorbing from this source
        if (source.absorbing.size() == 0)
            return null;

        // If only one player is absorbing return that player
        if (source.absorbing.size() == 1)
            return (Player)source.absorbing.keySet().toArray()[0];

        // Clone the hashmap so it can't be modified during calculations
        HashMap<Player, Integer> players = (HashMap<Player, Integer>)source.absorbing.clone();

        int absorbingAmount = 0;

        for (int absorbing : players.values())
            absorbingAmount += absorbing;

        // Subtract a random amount from the total amount of Qi being absorbed
        absorbingAmount -= new Random().nextInt(absorbingAmount);

        // Go through every absorbing player, subtracting the amount they are absorbing from the randomly chosen number
        // Until it hits 0
        for (Map.Entry<Player, Integer> entry : players.entrySet())
        {
            absorbingAmount -= entry.getValue();

            if (absorbingAmount <= 0)
                return entry.getKey();
        }

        // THIS SHOULD NOT HAPPEN
        return null;
    }

    public static void render(QiSource source)
    {
        if ((tick + 1) % (source.getSpawnTick() +1) > 0)
            return;

        Random random = new Random();

        double x = source.getPos().getX() + (random.nextDouble() - 0.5) * 0.2;
        double y = source.getPos().getY() + (random.nextDouble() - 0.5) * 0.2;
        double z = source.getPos().getZ() + (random.nextDouble() - 0.5) * 0.2;

        Vec3 direction;

        Player absorbing = getRandomAbsorbingPlayer(source);

        // If a player is absorbing from this QiSource then particles move towards one of the players
        if (absorbing != null)
        {
            direction = absorbing.position().subtract(source.getPos().getX(), source.getPos().getY(), source.getPos().getZ());
        }
        // If not player is absorbing from this QiSource then particles move in random directions
        else
        {
            direction = new Vec3(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
        }

        direction = direction.normalize();

        QiParticleData particle = new QiParticleData(source);
        particle.setTarget(absorbing);
        Minecraft.getInstance().level.addParticle(particle, x, y, z, direction.x, direction.y, direction.z);
    }
}
