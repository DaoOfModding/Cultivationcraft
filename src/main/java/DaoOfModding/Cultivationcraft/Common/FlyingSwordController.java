package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBind;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.IFlyingSwordBind;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlyingSwordController
{
    static List<FlyingSwordEntity> FlyingSwords = new ArrayList<FlyingSwordEntity>();

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

        IFlyingSwordBind bindStatus = FlyingSwordBind.getFlyingSwordBind(item);

        // Don't do anything if the itemStack isn't bindable
        if(bindStatus == null)
            return false;

        // Don't do anything if the item is already bound to this player
        if(bindStatus.isBound() && bindStatus.getOwner().compareTo(playerID) == 0)
            return false;

        // If another player has made any binding progress, reset the bind time
        // (Unless the time is already bound to another player)
        if (!bindStatus.isBound() && bindStatus.getBindingPlayer() != null)
            if (bindStatus.getBindingPlayer().compareTo(playerID) != 0)
                bindStatus.setBindTime(0);

        // Set this player as the binding player and start binding item
        bindStatus.setBindingPlayer(playerID);

        // If item is bound to another player then set the bind time to negative the original bind time
        if (bindStatus.isBound() && bindStatus.getBindTime() > 0)
            bindStatus.setBindTime(bindStatus.getBindTime() * -1);

        return true;
    }
}
