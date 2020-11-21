package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class QiSource
{
    private BlockPos pos;
    private int range;
    private int elementID;

    // Todo: QiSource stats
    public QiSource(BlockPos position, int size, int element)
    {
        pos = position;
        range = size;
        elementID = element;
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

    public CompoundNBT SerializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putLong("pos", pos.toLong());
        nbt.putInt("range", range);
        nbt.putInt("element", elementID);

        return nbt;
    }

    public void WriteBuffer(PacketBuffer buffer)
    {
        buffer.writeLong(pos.toLong());
        buffer.writeInt(range);
        buffer.writeInt(elementID);
    }

    public static QiSource DeserializeNBT(CompoundNBT nbt)
    {
        BlockPos newPos = BlockPos.fromLong(nbt.getLong("pos"));
        int size = nbt.getInt("range");
        int element = nbt.getInt("element");

        return new QiSource(newPos, size, element);
    }

    public static QiSource ReadBuffer(PacketBuffer buffer)
    {
        BlockPos newPos = BlockPos.fromLong(buffer.readLong());
        int size = buffer.readInt();
        int element = buffer.readInt();

        return new QiSource(newPos, size, element);
    }
}
