package DaoOfModding.Cultivationcraft.Common.Containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import DaoOfModding.Cultivationcraft.Register;

import javax.annotation.Nonnull;

public class FlyingSwordContainer extends Container
{
    public static FlyingSwordContainer createContainerServerSide(int windowID, PlayerInventory playerInventory)
    {
        return new FlyingSwordContainer(windowID, playerInventory);
    }


    public static FlyingSwordContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData)
    {
        return new FlyingSwordContainer(windowID, playerInventory);
    }

    private FlyingSwordContainer(int windowId, PlayerInventory playerInv)
    {
        super(Register.ContainerTypeFlyingSword, windowId);
    }

    @Override
    // Return true if player can interact with this container, false if not
    public boolean canInteractWith(@Nonnull PlayerEntity player)
    {
        //TODO: Check if player cultivation high enough to use flying swords
        return true;
    }

    /*
    @Override
    // What happens when an item is SHIFT-clicked in this container
    public ItemStack transferStackInSlot(PlayerEntity player, int sourceSlotIndex)
    {
        return ItemStack.EMPTY;
    }*/
}
