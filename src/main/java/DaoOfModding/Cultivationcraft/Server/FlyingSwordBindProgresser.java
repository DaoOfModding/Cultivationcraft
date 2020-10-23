package DaoOfModding.Cultivationcraft.Server;

import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBind;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.IFlyingSwordBind;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStack;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.LongNBT;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class FlyingSwordBindProgresser
{
    // Progress any ongoing flying sword bindings
    public static void bindFlyingSword(long time)
    {
        // Cycle through list of all players, dealing with any flying swords they are currently binding

        for(PlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
        {
            ItemStack testItem = FlyingSwordContainerItemStack.getCapability(player).getItemStackHandler().getStackInSlot(0);

            // If the item in the binding slot exists
            if (!testItem.isEmpty())
            {
                // If the item int the binding slot is able to be bound, try to bind it
                if (FlyingSwordController.startFlyingSwordBind(testItem, player.getUniqueID()))
                {
                    IFlyingSwordBind bindData = FlyingSwordBind.getFlyingSwordBind(testItem);

                    setBindTime(testItem, bindData.getBindTime() + time);

                    // If the bind item is already bound to someone else but the bind time is now greater than 0
                    // Mark the item as unbound
                    if (bindData.isBound() && bindData.getBindTime() > 0)
                    {
                        bindData.setBound(false);
                        bindData.setOwner(null);
                    }

                    // If the bind time has reached the max bind time then mark this item as bound to its new owner
                    // And convert it into a flying sword
                    if (bindData.getBindTime() > bindData.getBindTimeMax())
                    {
                        bindData.setOwner(bindData.getBindingPlayer());
                        bindData.setBound(true);

                        FlyingSwordController.addFlyingItem(testItem, player.getUniqueID());
                    }
                }
            }
        }
    }

    public static void setBindTime(ItemStack item, long time)
    {
        IFlyingSwordBind bindData = FlyingSwordBind.getFlyingSwordBind(item);

        bindData.setBindTime(time);

        // This could be done better, but screw it. BindTime is being stored as an NBT tag in the item for the client to read
        CompoundNBT nbt = item.getTag();

        long BindRemaining = (bindData.getBindTimeMax() - time);
        float percent = (float)(time / 1000000000f) / (float)(bindData.getBindTimeMax() / 1000000000f);

        if (percent > 1)
            percent = 1;

        nbt.putFloat("BindRemaining", (Float)(BindRemaining / 1000000000f));
        nbt.putFloat("BindPercent", percent);
    }
}
