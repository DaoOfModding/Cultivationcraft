package DaoOfModding.Cultivationcraft.Server;

import DaoOfModding.Cultivationcraft.Common.Advancements.CultivationAdvancements;
import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.FlyingSwordTrigger;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStack;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordBind;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.server.ServerLifecycleHooks;

public class FlyingSwordBindProgresser {
    // Progress any ongoing flying sword bindings
    public static void bindFlyingSword(long time) {
        // Cycle through list of all players, dealing with any flying swords they are currently binding

        for (Player player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (player.isAlive()) {
                ItemStack testItem = FlyingSwordContainerItemStack.getCapability(player).getItemStackHandler().getStackInSlot(0);

                // If the item in the binding slot exists
                if (!testItem.isEmpty()) {
                    // If the item int the binding slot is able to be bound, try to bind it
                    if (FlyingSwordController.startFlyingSwordBind(testItem, player.getUUID())) {
                        increaseBindTime(testItem, time);

                        // If the bind item is already bound to someone else but the bind time is now greater than 0
                        // Mark the item as unbound
                        if (FlyingSwordBind.isBound(testItem) && FlyingSwordBind.getBindTime(testItem) > 0) {
                            FlyingSwordBind.setBound(testItem, false);
                            FlyingSwordBind.setOwner(testItem, null);
                        }

                        // If the bind time has reached the max bind time then mark this item as bound to its new owner
                        // And convert it into a flying sword
                        if (FlyingSwordBind.getBindTime(testItem) > FlyingSwordBind.getBindTimeMax(testItem)) {
                            FlyingSwordBind.setOwner(testItem, FlyingSwordBind.getBindingPlayer(testItem));
                            FlyingSwordBind.setBound(testItem, true);

                            FlyingSwordController.addFlyingItem(testItem, player.getUUID());

                            //used for Advancement trigger
                            if (player instanceof ServerPlayer serverPlayer)
                                CultivationAdvancements.HAS_FLYING_SWORD.trigger(serverPlayer, false);
                        }
                    }
                }
            }
        }
    }

    public static void increaseBindTime(ItemStack item, long time) {
        long newTime = FlyingSwordBind.getBindTime(item) + time;

        // If item is bound to another player and bind time is > 0 then unbind the item
        if (newTime > 0 && FlyingSwordBind.isBound(item) && FlyingSwordBind.getOwner(item).compareTo(FlyingSwordBind.getBindingPlayer(item)) == 0) {
            FlyingSwordBind.setBound(item, false);
            FlyingSwordBind.setOwner(item, null);
            FlyingSwordController.removeFlyingItem(item);
        }

        setBindTime(item, newTime);
    }

    public static void setBindTime(ItemStack item, long time) {
        FlyingSwordBind.setBindTime(item, time);

        // This could be done better, but screw it. BindTime is being stored as an NBT tag in the item for the client to read
        CompoundTag nbt = item.getTag();

        long BindRemaining = (FlyingSwordBind.getBindTimeMax(item) - time);
        float percent = (float) (time / 1000000000f) / (float) (FlyingSwordBind.getBindTimeMax(item) / 1000000000f);

        if (percent > 1)
            percent = 1;

        nbt.putFloat("BindRemaining", (Float) (BindRemaining / 1000000000f));
        nbt.putFloat("BindPercent", percent);
    }
}
