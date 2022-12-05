package DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

public interface IChunkQiSources
{
    public void setChunkPos(ChunkPos position);
    public ChunkPos getChunkPos();

    public List<QiSource> getQiSources();
    public void setQiSources(List<QiSource> sources);

    public void generateQiSources();

    public CompoundTag writeNBT();
    public void readNBT(CompoundTag nbt);
}
