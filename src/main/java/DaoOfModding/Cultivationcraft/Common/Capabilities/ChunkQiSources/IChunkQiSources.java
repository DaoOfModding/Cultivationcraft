package DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import net.minecraft.util.math.ChunkPos;

import java.util.List;

public interface IChunkQiSources
{
    public void setChunkPos(ChunkPos position);
    public ChunkPos getChunkPos();

    public List<QiSource> getQiSources();
    public void setQiSources(List<QiSource> sources);

    public void generateQiSources();
}
