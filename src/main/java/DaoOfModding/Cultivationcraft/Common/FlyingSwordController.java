package DaoOfModding.Cultivationcraft.Common;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlyingSwordController
{
    static List<FlyingSwordEntity> FlyingSwords = new ArrayList<FlyingSwordEntity>();

    public static void addFlyingItem(ItemStack item, UUID owner)
    {
        CompoundNBT nbt = item.getTag();
        if (nbt == null)
            nbt = new CompoundNBT();

        nbt.putBoolean("Flying", true);

        if (owner != null)
            nbt.putUniqueId("Owner", owner);

        item.setTag(nbt);
    }

    // Returns true if the passed item is set as a flying item
    public static boolean isFlying(ItemStack item)
    {
        CompoundNBT nbt = item.getTag();

        if (nbt != null && nbt.contains("Flying"))
            return nbt.getBoolean("Flying");

        return false;
    }

    // Deletes the specified ItemEntity and replaces it with a flying sword entity
    public static void spawnFlyingSword(ItemEntity item)
    {
        FlyingSwordEntity test = new FlyingSwordEntity(item.world, item.getPosX(), item.getPosY(), item.getPosZ(), item.getItem());

        item.world.addEntity(test);
    }
}
