package DaoOfModding.Cultivationcraft.Common.Containers;

import DaoOfModding.Cultivationcraft.Common.BasicContainer;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class FlyingSwordContainer extends BasicContainer
{
    public static final int FLYING_SWORD_ITEM_YPOS = 43;
    public static final int FLYING_SWORD_ITEM_XPOS = 80;

    private final FlyingSwordContainerItemHandler itemStackHandler;

    public static FlyingSwordContainer createContainerServerSide(int windowID, PlayerInventory playerInventory, FlyingSwordContainerItemHandler handler)
    {
        return new FlyingSwordContainer(windowID, playerInventory, handler);
    }


    public static FlyingSwordContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData)
    {
        return new FlyingSwordContainer(windowID, playerInventory, new FlyingSwordContainerItemHandler());
    }

    private FlyingSwordContainer(int windowId, PlayerInventory playerInv, FlyingSwordContainerItemHandler handler)
    {
        super(Register.ContainerTypeFlyingSword, windowId);

        itemStackHandler = handler;

        // Add the players inventory slots into the container
        addPlayerInventory(playerInv);

        addSlot(new SlotItemHandler(handler, 0, FLYING_SWORD_ITEM_XPOS, FLYING_SWORD_ITEM_YPOS));
    }

    public float getBindTime()
    {
        return getNbt("BindRemaining");
    }

    public float getBindPercent()
    {
        return getNbt("BindPercent");
    }

    private float getNbt(String tag)
    {
        if (itemStackHandler.getStackInSlot(0) == ItemStack.EMPTY)
            return 0;

        if (!itemStackHandler.getStackInSlot(0).getTag().contains(tag))
            return 0;

        return itemStackHandler.getStackInSlot(0).getTag().getFloat(tag);
    }

    @Override
    // Return true if player can interact with this container, false if not
    public boolean stillValid(@Nonnull PlayerEntity player)
    {
        //TODO: Check if player cultivation high enough to use flying swords
        return true;
    }
}
