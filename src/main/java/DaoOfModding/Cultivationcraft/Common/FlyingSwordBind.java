package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FlyingSwordBind
{
    public static boolean isBound(ItemStack item)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        if (tag.contains(Cultivationcraft.MODID + "bound"))
            return tag.getBoolean(Cultivationcraft.MODID + "bound");

        return false;
    }

    public static void setBound(ItemStack item, boolean value)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        tag.putBoolean(Cultivationcraft.MODID + "bound", value);

        item.setTag(tag);
    }

    public static long getBindTime(ItemStack item)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        if (tag.contains(Cultivationcraft.MODID + "bindtime"))
            return tag.getLong(Cultivationcraft.MODID + "bindtime");

        return 0;
    }

    public static void setBindTime(ItemStack item, long newTime)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        tag.putLong(Cultivationcraft.MODID + "bindtime", newTime);

        item.setTag(tag);
    }

    public static void setOwner(ItemStack item, UUID newOwner)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        tag.putUUID(Cultivationcraft.MODID + "bindowner", newOwner);

        item.setTag(tag);
    }

    public static UUID getOwner(ItemStack item)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        if (tag.contains(Cultivationcraft.MODID + "bindowner"))
            return tag.getUUID(Cultivationcraft.MODID + "bindowner");

        return null;
    }

    public static void setBindingPlayer(ItemStack item, UUID newPlayer)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        tag.putUUID(Cultivationcraft.MODID + "bindingplayer", newPlayer);

        item.setTag(tag);
    }

    public static UUID getBindingPlayer(ItemStack item)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        if (tag.contains(Cultivationcraft.MODID + "bindingplayer"))
            return tag.getUUID(Cultivationcraft.MODID + "bindingplayer");

        return null;
    }

    public static long getBindTimeMax(ItemStack item)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        if (tag.contains(Cultivationcraft.MODID + "bindtimemax"))
            return tag.getLong(Cultivationcraft.MODID + "bindtimemax");

        return TimeUnit.SECONDS.toNanos(10);
    }

    public static void setBindTimeMax(ItemStack item, long newTime)
    {
        CompoundTag tag = item.getTag();

        if (tag == null)
            tag = new CompoundTag();

        tag.putLong(Cultivationcraft.MODID + "bindtimemax", newTime);

        item.setTag(tag);
    }
}
