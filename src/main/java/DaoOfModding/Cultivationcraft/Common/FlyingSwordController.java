package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBind;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.IFlyingSwordBind;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
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
        if(bindStatus.isBound() && bindStatus.getOwner() == playerID)
            return false;

        // If another player has made any binding progress, reset the bind time
        // (Unless that time is still negative)
        if (bindStatus.getBindingPlayer() != null)
            if (bindStatus.getBindingPlayer().compareTo(playerID) != 0)
                if (bindStatus.getBindTime() > 0)
                    bindStatus.setBindTime(0);

        // Set this player as the binding player and start binding item
        bindStatus.setBindingPlayer(playerID);

        // If item is bound to another player then set the bind time to negative the original bind time
        if (bindStatus.isBound() && bindStatus.getOwner() != playerID)
            bindStatus.setBindTime(bindStatus.getBindTime() * -1);

        return true;
    }
}
