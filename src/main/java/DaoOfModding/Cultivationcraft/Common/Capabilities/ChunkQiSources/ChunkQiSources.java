package DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.BlockElements;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkQiSources implements IChunkQiSources
{
    // TODO: Different dimension spawning

    ChunkPos ChunkPos;
    List<QiSource> QiSources = new ArrayList<QiSource>();
    HashMap<Vec3, Integer> pendingQiSource = new HashMap<Vec3, Integer>();

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

    public int countQiSources()
    {
        return QiSources.size() + pendingQiSource.size();
    }

    public void setQiSources(List<QiSource> sources)
    {
        QiSources = sources;
    }

    public void generateQiSources(LevelAccessor level)
    {
        int number = QiSourceConfig.getQiSourceInLevelChunk();

        for (int i = 0; i < number; i++)
            generateQiSource(level);
    }

    public boolean tick(Level level)
    {
        boolean updated = false;

        for (QiSource source : getQiSources())
            if (source.tick())
                updated = true;

        // Try to create any pending QiSources
        if (pendingQiSource.size() > 0)
        {
            List<Vec3> toRemove = new ArrayList();

            for (Map.Entry<Vec3, Integer> qiSource : pendingQiSource.entrySet())
            {
                if (canCreate(level, qiSource.getKey(), qiSource.getValue()))
                {
                    createQiSource(level, qiSource.getKey(), qiSource.getValue());
                    toRemove.add(qiSource.getKey());
                    updated = true;
                }
            }

            for (Vec3 pos : toRemove)
                pendingQiSource.remove(pos);
        }

        return updated;
    }

    // Check if all chunks needed to create this QiSource have been loaded
    protected boolean canCreate(Level level, Vec3 pos, int range)
    {
        for (int x = -range; x <= range; x+=16)
            for (int z = -range; z <= range; z+=16)
            {
                ChunkPos chunkPos = new ChunkPos(new BlockPos(pos.x + x, pos.y, pos.z + z));
                if (!level.hasChunk(chunkPos.x, chunkPos.z))
                    return false;
            }

        return true;
    }

    protected void createQiSource(Level level, Vec3 pos, int size)
    {
        int element = getElement(level, pos, size);

        QiSources.add(new QiSource(new BlockPos(pos), size, element, QiSourceConfig.generateRandomQiStorage(), QiSourceConfig.generateRandomQiRegen()));
    }

    // Get the element majorly contained within a sphere of range centered at pos
    // Return no element if mixed
    protected int getElement(LevelAccessor level, Vec3 pos, int range)
    {
        int count = 0;
        int[] elements = new int[Elements.getElements().size()];

        // Cycle through each block in the sphere and count the elements inside
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    if (new Vec3(x, y, z).length() < range)
                    {
                        elements[BlockElements.getMaterialElement(level.getBlockState(new BlockPos(pos.add(x, y, z))).getMaterial())]++;
                        count++;
                    }

        // Return and elementID if more than half the sphere contains that element
        for (int elementID = 0; elementID < elements.length; elementID++)
            if (elements[elementID] > count/2)
                return elementID;

        return Elements.noElementID;
    }

    protected void generateQiSource(LevelAccessor level)
    {
        // Generate a random xPos and zPos somewhere within the LevelChunk
        int xPos = (int)(Math.random() * 15) + (ChunkPos.x << 4);
        int zPos = (int)(Math.random() * 15) + (ChunkPos.z << 4);

        // Generate a random yPos, more likely to be at ground level (0.32)
        double x = Math.random();

        int yPos = (int)(((4 * Math.pow(x, 3)) - (5.28 * Math.pow(x, 2)) + (2.28 * x)) * 200);

        int size = QiSourceConfig.generateRandomSize();

        pendingQiSource.put(new Vec3(xPos, yPos, zPos), size);
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

             count = 0;
            // Add NBT data for each PendingQiSource
            for (Map.Entry<Vec3, Integer> source : pendingQiSource.entrySet())
            {
                nbt.putInt("PQiSourceX" + count, (int)source.getKey().x);
                nbt.putInt("PQiSourceY" + count, (int)source.getKey().y);
                nbt.putInt("PQiSourceZ" + count, (int)source.getKey().z);
                nbt.putInt("PQiSourceS" + count, source.getValue());
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
            pendingQiSource.clear();

            int count = 0;
            // Load each QiSource from NBT
            while (nbt.contains("QiSource" + count))
            {
                QiSource source = QiSource.DeserializeNBT((CompoundTag)nbt.get("QiSource" + count));

                sourceList.add(source);

                count++;
            }

            count = 0;
            // Load each PendingQiSource from NBT
            while (nbt.contains("PQiSourceX" + count))
            {
                int x = nbt.getInt("PQiSourceX" + count);
                int y = nbt.getInt("PQiSourceY" + count);
                int z = nbt.getInt("PQiSourceZ" + count);
                int s = nbt.getInt("PQiSourceS" + count);

                pendingQiSource.put(new Vec3(x,y,z), s);

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
