package DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources;

import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

public interface IChunkQiSources
{
    public void setChunkPos(ChunkPos position);
    public ChunkPos getChunkPos();

    public List<QiSource> getQiSources();
    public void setQiSources(List<QiSource> sources);
    public int countQiSources();

    public boolean tick(Level level);

    public void generateQiSources(LevelAccessor level);

    public CompoundTag writeNBT();
    public void readNBT(CompoundTag nbt);
}
