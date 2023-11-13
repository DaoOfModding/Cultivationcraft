package DaoOfModding.Cultivationcraft.Common;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class FlyingSwordController
{
    public static void addFlyingItem(ItemStack item, UUID owner)
    {
        CompoundTag nbt = item.getTag();
        if (nbt == null)
            nbt = new CompoundTag();

        nbt.putBoolean("Flying", true);

        if (owner != null)
            nbt.putUUID("Owner", owner);

        item.setTag(nbt);
    }

    public static void removeFlyingItem(ItemStack item)
    {
        CompoundTag nbt = item.getTag();
        if (nbt == null)
            nbt = new CompoundTag();

        nbt.putBoolean("Flying", false);

        item.setTag(nbt);
    }

    // Returns true if the passed item is set as a flying item
    public static boolean isFlying(ItemStack item)
    {
        CompoundTag nbt = item.getTag();

        if (nbt != null && nbt.contains("Flying"))
            return nbt.getBoolean("Flying");

        return false;
    }

    // Deletes the specified ItemEntity and replaces it with a flying sword entity
    public static void spawnFlyingSword(ItemEntity item)
    {
        FlyingSwordEntity test = new FlyingSwordEntity(item.level, item.getX(), item.getY(), item.getZ(), item.getItem());

        item.level.addFreshEntity(test);
    }

    // Start binding to the specified flying sword
    public static boolean startFlyingSwordBind(ItemStack item, UUID playerID)
    {
        // Don't do anything if the itemStack is empty
        if (item == ItemStack.EMPTY)
            return false;

        // Don't do anything if the item is already bound to this player
        if(FlyingSwordBind.isBound(item) && FlyingSwordBind.getOwner(item).compareTo(playerID) == 0)
            return false;

        // If another player has made any binding progress, reset the bind time
        // (Unless the time is already bound to another player)
        if (!FlyingSwordBind.isBound(item) && FlyingSwordBind.getBindingPlayer(item) != null)
            if (FlyingSwordBind.getBindingPlayer(item).compareTo(playerID) != 0)
                FlyingSwordBind.setBindTime(item, 0);

        // Set this player as the binding player and start binding item
        FlyingSwordBind.setBindingPlayer(item, playerID);

        // If item is bound to another player then set the bind time to negative the original bind time
        if (FlyingSwordBind.isBound(item) && FlyingSwordBind.getBindTime(item) > 0)
            FlyingSwordBind.setBindTime(item, FlyingSwordBind.getBindTime(item) * -1);

        return true;
    }
}
