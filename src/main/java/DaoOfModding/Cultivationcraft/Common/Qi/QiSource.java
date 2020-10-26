package DaoOfModding.Cultivationcraft.Common.Qi;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class QiSource
{
    private BlockPos pos;

    // Todo: QiSource stats
    public QiSource(BlockPos position)
    {
        pos = position;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public CompoundNBT SerializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putLong("pos", pos.toLong());

        return nbt;
    }

    public static QiSource DeserializeNBT(CompoundNBT nbt)
    {
        BlockPos newPos = BlockPos.fromLong(nbt.getLong("pos"));

        return new QiSource(newPos);
    }
}
