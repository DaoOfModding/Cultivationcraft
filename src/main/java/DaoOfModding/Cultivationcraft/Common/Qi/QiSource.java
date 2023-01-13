package DaoOfModding.Cultivationcraft.Common.Qi;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class QiSource
{
    protected BlockPos pos;
    protected int range;
    protected int elementID;

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

    public CompoundTag SerializeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        nbt.putLong("pos", pos.asLong());
        nbt.putInt("range", range);
        nbt.putInt("element", elementID);

        return nbt;
    }

    public void WriteBuffer(FriendlyByteBuf buffer)
    {
        buffer.writeLong(pos.asLong());
        buffer.writeInt(range);
        buffer.writeInt(elementID);
    }

    public static QiSource DeserializeNBT(CompoundTag nbt)
    {
        BlockPos newPos = BlockPos.of(nbt.getLong("pos"));
        int size = nbt.getInt("range");
        int element = nbt.getInt("element");

        return new QiSource(newPos, size, element);
    }

    public static QiSource ReadBuffer(FriendlyByteBuf buffer)
    {
        BlockPos newPos = BlockPos.of(buffer.readLong());
        int size = buffer.readInt();
        int element = buffer.readInt();

        return new QiSource(newPos, size, element);
    }
}
