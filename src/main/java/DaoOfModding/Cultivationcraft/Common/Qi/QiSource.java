package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Element;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QiSource
{
    protected BlockPos pos;
    protected int range;
    protected ResourceLocation Element;

    protected double qiMax;
    protected double qiCurrent;

    protected int qiRegen;

    protected double previousCurrent = 0;

    protected boolean toUpdate = true;

    protected static final int minSpawnTime = 30;

    public int spawnedTick = 0;

    protected HashMap<UUID, Integer> currentAbsorbing = new HashMap();
    protected HashMap<UUID, Integer> absorbing = new HashMap();

    public QiSource(BlockPos position, int size, ResourceLocation element, double qi, int regen)
    {
        this(position, size, element, qi, 1, regen);
    }

    public QiSource(BlockPos position, int size, ResourceLocation element, double qi, double current, int regen)
    {
        pos = position;
        range = size;
        Element = element;

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

    public ResourceLocation getElement()
    {
        return Element;
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

    public BlockPos getRandomPos()
    {
        Vec3 randomPos;

        do
        {
            randomPos = new Vec3((Math.random() * getSize() * 2) - getSize(), Math.random() * getSize() * 2 - getSize(),  Math.random() * getSize() * 2 - getSize());
        } while (randomPos.length() > getSize());

        return new BlockPos(pos.getX() + randomPos.x, pos.getY() + randomPos.y, pos.getZ() + randomPos.z);
    }

    // Apply elemental effects to all blocks in range
    public void effectAllBlocks(Level level)
    {
        Element element = Elements.getElement(getElement());

        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                    if (new Vec3(x, y, z).length() <= range)
                        element.effectBlock(level, new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
    }

    public boolean tick(Level level)
    {
        Element element = Elements.getElement(getElement());

        if (element.shouldDoBlockEffect())
            element.effectBlock(level, getRandomPos());


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

        subtractQi(toAbsorb);

        if (toAbsorb > 0)
        {
            absorbing.put(player.getUUID(), toAbsorb);
            toUpdate = true;
        }

        return toAbsorb;
    }

    public void subtractQi(int toSubtract)
    {
        qiCurrent -= (double)toSubtract / (double)getQiMax();

        if (qiCurrent < 0)
            qiCurrent = 0;
    }

    public CompoundTag SerializeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        nbt.putLong("pos", pos.asLong());
        nbt.putInt("range", range);
        nbt.putString("element", Element.toString());
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
        ResourceLocation element = new ResourceLocation(nbt.getString("element"));
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
