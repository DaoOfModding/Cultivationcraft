package DaoOfModding.Cultivationcraft.Common.Qi;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class QiSource
{
    private BlockPos pos;
    private int range;

    // Todo: QiSource stats
    public QiSource(BlockPos position, int size)
    {
        pos = position;
        range = size;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public int getSize()
    {
        return range;
    }

    public CompoundNBT SerializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putLong("pos", pos.toLong());
        nbt.putInt("range", range);

        return nbt;
    }

    public void WriteBuffer(PacketBuffer buffer)
    {
        buffer.writeLong(pos.toLong());
        buffer.writeInt(range);
    }

    public static QiSource DeserializeNBT(CompoundNBT nbt)
    {
        BlockPos newPos = BlockPos.fromLong(nbt.getLong("pos"));
        int size = nbt.getInt("range");

        return new QiSource(newPos, size);
    }

    public static QiSource ReadBuffer(PacketBuffer buffer)
    {
        BlockPos newPos = BlockPos.fromLong(buffer.readLong());
        int size = buffer.readInt();

        return new QiSource(newPos, size);
    }
}
