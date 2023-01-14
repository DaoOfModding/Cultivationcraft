package DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSourceConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;

public class ChunkQiSources implements IChunkQiSources
{
    // TODO: Different dimension spawning

    ChunkPos ChunkPos;
    List<QiSource> QiSources = new ArrayList<QiSource>();

    public void setChunkPos(ChunkPos position)
    {
        ChunkPos = position;
    }

    public ChunkPos getChunkPos()
    {
        return ChunkPos;
    }

    public List<QiSource> getQiSources()
    {
        return QiSources;
    }

    public void setQiSources(List<QiSource> sources)
    {
        QiSources = sources;
    }

    public void generateQiSources()
    {
        int number = QiSourceConfig.getQiSourceInLevelChunk();

        for (int i = 0; i < number; i++)
            generateQiSource();
    }

    protected void generateQiSource()
    {
        // Generate a random xPos and zPos somewhere within the LevelChunk
        int xPos = (int)(Math.random() * 15) + (ChunkPos.x << 4);
        int zPos = (int)(Math.random() * 15) + (ChunkPos.z << 4);

        // Generate a random yPos, more likely to be at ground level (0.32)
        double x = Math.random();

        int yPos = (int)(((4 * Math.pow(x, 3)) - (5.28 * Math.pow(x, 2)) + (2.28 * x)) * 200);

        // TODO: Generate different elemental spawns BETTER THAN THIS
        int element = (int)(Math.random() * Elements.getElements().size());

        int count = 0;
        for (Element el : Elements.getElements())
        {
            if (count != element)
                count++;
            else
            {
                element = el.ID;
                break;
            }
        }

        QiSources.add(new QiSource(new BlockPos(xPos, yPos, zPos), QiSourceConfig.generateRandomSize(), element, QiSourceConfig.generateRandomQiOutput()));
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        if (getChunkPos() != null)
        {
            nbt.putLong("QiSource", getChunkPos().toLong());

            int count = 0;
            // Add NBT data for each QiSource
            for (QiSource source : getQiSources())
            {
                nbt.put("QiSource" + count, source.SerializeNBT());
                count++;
            }
        }

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        if (nbt.contains("QiSource"))
        {
            setChunkPos(new ChunkPos(nbt.getLong("QiSource")));

            List<QiSource> sourceList = new ArrayList<QiSource>();

            int count = 0;
            // Load each QiSource from NBT
            while (nbt.contains("QiSource" + count))
            {
                QiSource source = QiSource.DeserializeNBT((CompoundTag)nbt.get("QiSource" + count));

                sourceList.add(source);

                count++;
            }

            setQiSources(sourceList);
        }
    }

    // Return a specified players CultivatorStats
    public static IChunkQiSources getChunkQiSources(LevelChunk LevelChunk)
    {
        return LevelChunk.getCapability(ChunkQiSourcesCapability.INSTANCE).orElseThrow(() -> new IllegalArgumentException("getting LevelChunk Qi sources"));
    }

    public static List<QiSource> getQiSourcesInRange(Level level, Vec3 position, int range)
    {
        ArrayList<QiSource> sources = new ArrayList<QiSource>();

        int searchRange = (range + QiSourceConfig.MaxSize) / 16 + 1;

        ChunkPos test = new ChunkPos(new BlockPos(position));

        // Loop through each chunk within possible range
        for (int x = -searchRange; x <= searchRange; x++)
            for (int z = -searchRange; z <= searchRange; z++)
            {
                List<QiSource> possibleSources = getChunkQiSources(level.getChunk(test.x + x, test.z + z)).getQiSources();

                // Check each source in the chunk to see if it is within range
                for (QiSource source : possibleSources)
                {
                    double distance = position.subtract(source.getPos().getX(), source.getPos().getY(), source.getPos().getZ()).length();

                    if (distance < range + source.getSize())
                        sources.add(source);
                }
            }

        return sources;
    }
}
