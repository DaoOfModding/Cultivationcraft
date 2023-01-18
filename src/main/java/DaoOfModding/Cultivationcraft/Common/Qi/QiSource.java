package DaoOfModding.Cultivationcraft.Common.Qi;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QiSource
{
    protected BlockPos pos;
    protected int range;
    protected int elementID;

    protected double qiMax;
    protected double qiCurrent;

    protected int qiRegen;

    protected double previousCurrent = 0;

    protected boolean toUpdate = true;

    protected static final int minSpawnTime = 30;

    public int spawnedTick = 0;

    protected HashMap<UUID, Integer> currentAbsorbing = new HashMap();
    protected HashMap<UUID, Integer> absorbing = new HashMap();

    public QiSource(BlockPos position, int size, int element, double qi, int regen)
    {
        this(position, size, element, qi, 1, regen);
    }

    public QiSource(BlockPos position, int size, int element, double qi, double current, int regen)
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

    public int getQiCurrent()
    {
        return (int)(qiCurrent * getQiMax());
    }

    // Return how many ticks should wait before a QiParticle spawn
    public int getSpawnTick()
    {
        return (int)((1 - qiCurrent) * minSpawnTime);
    }

    public int getQiMax()
    {
        return (int)(qiMax * (QiSourceConfig.MaxStorage - QiSourceConfig.MinStorage)) + QiSourceConfig.MinStorage;
    }

    public HashMap<UUID, Integer> getAbsorbingPlayers()
    {
        return currentAbsorbing;
    }

    public boolean tick()
    {
        boolean update = toUpdate;
        toUpdate = false;

        // Clear the list of players absorbing from this source
        currentAbsorbing = absorbing;
        absorbing = new HashMap();

        qiCurrent += (1.0 / qiRegen);

        if (qiCurrent > 1)
            qiCurrent = 1;

        if (previousCurrent != qiCurrent)
            update = true;

        previousCurrent = qiCurrent;

        return update;
    }

    // Try to absorb the specified amount of qi, returning the amount absorbed
    public int absorbQi(int toAbsorb, Player player)
    {
        if (toAbsorb > getQiCurrent())
            toAbsorb = getQiCurrent();

        qiCurrent -= (double)toAbsorb / (double)getQiMax();

        if (toAbsorb > 0)
        {
            absorbing.put(player.getUUID(), toAbsorb);
            toUpdate = true;
        }

        return toAbsorb;
    }

    public CompoundTag SerializeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        nbt.putLong("pos", pos.asLong());
        nbt.putInt("range", range);
        nbt.putInt("element", elementID);
        nbt.putDouble("qimax", qiMax);
        nbt.putDouble("qicurrent", qiCurrent);
        nbt.putInt("qiregen", qiRegen);

        int i = 0;
        for (Map.Entry<UUID, Integer> player : currentAbsorbing.entrySet())
        {
            nbt.putUUID("player" + i, player.getKey());
            nbt.putInt("amount" + i, player.getValue());
            i++;
        }

        return nbt;
    }

    public void WriteBuffer(FriendlyByteBuf buffer)
    {
        buffer.writeNbt(SerializeNBT());
    }

    public static QiSource DeserializeNBT(CompoundTag nbt)
    {
        BlockPos newPos = BlockPos.of(nbt.getLong("pos"));
        int size = nbt.getInt("range");
        int element = nbt.getInt("element");
        double qiMax = nbt.getDouble("qimax");
        double qiCurrent = nbt.getDouble("qicurrent");
        int qiRegen = nbt.getInt("qiregen");

        HashMap<UUID, Integer> absorbing = new HashMap();

        int i = 0;
        while (nbt.hasUUID("player" + i))
        {
            absorbing.put(nbt.getUUID("player" + i), nbt.getInt("amount" + i));
            i++;
        }

        QiSource newSource = new QiSource(newPos, size, element, qiMax, qiCurrent, qiRegen);
        newSource.currentAbsorbing = absorbing;

        return newSource;
    }

    public static QiSource ReadBuffer(FriendlyByteBuf buffer)
    {
        return DeserializeNBT(buffer.readNbt());
    }

    // Default Qi to absorb without using a QiSource
    public static int getDefaultQi()
    {
        return 10;
    }
}
