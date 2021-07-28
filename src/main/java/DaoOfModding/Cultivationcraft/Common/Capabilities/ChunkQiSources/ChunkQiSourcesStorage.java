package DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;

public class ChunkQiSourcesStorage implements Capability.IStorage<IChunkQiSources>
{
    @Override
    public INBT writeNBT(Capability<IChunkQiSources> capability, IChunkQiSources instance, Direction side)
    {
        CompoundNBT nbt = new CompoundNBT();

        if (instance.getChunkPos() != null)
        {
            nbt.putLong("QiSource", instance.getChunkPos().toLong());

            int count = 0;
            // Add NBT data for each QiSource
            for (QiSource source : instance.getQiSources())
            {
                nbt.put("QiSource" + count, source.SerializeNBT());
                count++;
            }
        }

        return nbt;
    }

    @Override
    public void readNBT(Capability<IChunkQiSources> capability, IChunkQiSources instance, Direction side, INBT nbt)
    {
        if (!(instance instanceof IChunkQiSources))
            throw new IllegalArgumentException("Tried to read Chunk Qi Sources from non ChunkQiSources instance");

        if (((CompoundNBT)nbt).contains("QiSource"))
        {
            instance.setChunkPos(new ChunkPos(((CompoundNBT) nbt).getLong("QiSource")));

            List<QiSource> sourceList = new ArrayList<QiSource>();

            int count = 0;
            // Load each QiSource from NBT
            while (((CompoundNBT)nbt).contains("QiSource" + count))
            {
                QiSource source = QiSource.DeserializeNBT((CompoundNBT)((CompoundNBT)nbt).get("QiSource" + count));

                sourceList.add(source);

                count++;
            }

            instance.setQiSources(sourceList);
        }
    }
}
