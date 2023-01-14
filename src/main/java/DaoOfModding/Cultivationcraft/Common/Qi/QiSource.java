package DaoOfModding.Cultivationcraft.Common.Qi;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class QiSource
{
    protected BlockPos pos;
    protected int range;
    protected int elementID;

    protected int qiMax;
    protected int qiCurrent;
    protected int qiRegen;

    protected int previousCurrent = 0;

    public QiSource(BlockPos position, int size, int element, int qi, int regen)
    {
        this(position, size, element, qi, qi, regen);
    }

    public QiSource(BlockPos position, int size, int element, int qi, int current, int regen)
    {
        pos = position;
        range = size;
        elementID = element;

        qiMax = qi;
        qiCurrent = current;
        qiRegen = regen;
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

    public int getQiCurrent() { return qiCurrent; }

    public boolean tick()
    {
        qiCurrent += qiRegen;

        if (qiCurrent > qiMax)
            qiCurrent = qiMax;

        boolean updated = false;

        if (previousCurrent != qiCurrent)
            updated = true;

        previousCurrent = qiCurrent;

        return updated;
    }

    // Try to absorb the specified amount of qi, returning the amount absorbed
    public int absorbQi(int toAbsorb)
    {
        if (toAbsorb > qiCurrent)
            toAbsorb = qiCurrent;

        qiCurrent -= toAbsorb;

        return toAbsorb;
    }

    public CompoundTag SerializeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        nbt.putLong("pos", pos.asLong());
        nbt.putInt("range", range);
        nbt.putInt("element", elementID);
        nbt.putInt("qimax", qiMax);
        nbt.putInt("qicurrent", qiCurrent);
        nbt.putInt("qiregen", qiRegen);

        return nbt;
    }

    public void WriteBuffer(FriendlyByteBuf buffer)
    {
        buffer.writeLong(pos.asLong());
        buffer.writeInt(range);
        buffer.writeInt(elementID);
        buffer.writeInt(qiMax);
        buffer.writeInt(qiCurrent);
        buffer.writeInt(qiRegen);
    }

    public static QiSource DeserializeNBT(CompoundTag nbt)
    {
        BlockPos newPos = BlockPos.of(nbt.getLong("pos"));
        int size = nbt.getInt("range");
        int element = nbt.getInt("element");
        int qiMax = nbt.getInt("qimax");
        int qiCurrent = nbt.getInt("qicurrent");
        int qiRegen = nbt.getInt("qiregen");

        return new QiSource(newPos, size, element, qiMax, qiCurrent, qiRegen);
    }

    public static QiSource ReadBuffer(FriendlyByteBuf buffer)
    {
        BlockPos newPos = BlockPos.of(buffer.readLong());
        int size = buffer.readInt();
        int element = buffer.readInt();
        int qimax = buffer.readInt();
        int qicurrent = buffer.readInt();
        int qiregen = buffer.readInt();

        return new QiSource(newPos, size, element, qimax, qicurrent, qiregen);
    }

    // Default Qi to absorb without using a QiSource
    public static int getDefaultQi()
    {
        return 10;
    }
}
