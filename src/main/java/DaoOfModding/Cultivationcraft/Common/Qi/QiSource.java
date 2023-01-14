package DaoOfModding.Cultivationcraft.Common.Qi;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class QiSource
{
    protected BlockPos pos;
    protected int range;
    protected int elementID;
    protected int qiOutput;

    // Todo: QiSource stats
    public QiSource(BlockPos position, int size, int element, int qi)
    {
        pos = position;
        range = size;
        elementID = element;

        qiOutput = qi;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public int getSize()
    {
        return range;
    }

    public int getElementID()
    {
        return elementID;
    }

    public int getQiOutput() { return qiOutput; }

    public CompoundTag SerializeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        nbt.putLong("pos", pos.asLong());
        nbt.putInt("range", range);
        nbt.putInt("element", elementID);
        nbt.putInt("qioutput", qiOutput);

        return nbt;
    }

    public void WriteBuffer(FriendlyByteBuf buffer)
    {
        buffer.writeLong(pos.asLong());
        buffer.writeInt(range);
        buffer.writeInt(elementID);
        buffer.writeInt(qiOutput);
    }

    public static QiSource DeserializeNBT(CompoundTag nbt)
    {
        BlockPos newPos = BlockPos.of(nbt.getLong("pos"));
        int size = nbt.getInt("range");
        int element = nbt.getInt("element");
        int qiOutput = nbt.getInt("qioutput");

        return new QiSource(newPos, size, element, qiOutput);
    }

    public static QiSource ReadBuffer(FriendlyByteBuf buffer)
    {
        BlockPos newPos = BlockPos.of(buffer.readLong());
        int size = buffer.readInt();
        int element = buffer.readInt();
        int qioutput = buffer.readInt();

        return new QiSource(newPos, size, element, qioutput);
    }

    // Default Qi to absorb without using a QiSource
    public static int getDefaultQi()
    {
        return 10;
    }
}
