package DaoOfModding.Cultivationcraft.Common.Containers;

import DaoOfModding.Cultivationcraft.Common.BasicContainer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStack;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.IFlyingSwordContainerItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import DaoOfModding.Cultivationcraft.Register;

import javax.annotation.Nonnull;

public class FlyingSwordContainer extends BasicContainer
{
    public static final int FLYING_SWORD_ITEM_YPOS = 26;

    private final IFlyingSwordContainerItemStack itemStackHandler;

    public static FlyingSwordContainer createContainerServerSide(int windowID, PlayerInventory playerInventory, IFlyingSwordContainerItemStack handler)
    {
        return new FlyingSwordContainer(windowID, playerInventory, handler);
    }


    public static FlyingSwordContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData)
    {
        return new FlyingSwordContainer(windowID, playerInventory, new FlyingSwordContainerItemStack());
    }

    private FlyingSwordContainer(int windowId, PlayerInventory playerInv, IFlyingSwordContainerItemStack handler)
    {
        super(Register.ContainerTypeFlyingSword, windowId);

        itemStackHandler = handler;

        addPlayerInventory(playerInv);
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
