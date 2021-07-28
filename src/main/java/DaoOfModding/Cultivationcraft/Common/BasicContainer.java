package DaoOfModding.Cultivationcraft.Common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicContainer extends Container
{
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;

    private static final int SLOT_X_SPACING = 18;
    private static final int SLOT_Y_SPACING = 18;
    private static final int HOTBAR_XPOS = 8;
    private static final int HOTBAR_YPOS = 155;

    private static final int PLAYER_INVENTORY_YPOS = 97;


    public static final int FIRST_FREE_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;


    public BasicContainer(@Nullable net.minecraft.inventory.container.ContainerType<?> type, int windowId)
    {
        super(type, windowId);
    }

    public void addPlayerInventory(PlayerInventory playerInv)
    {

        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++)
        {
            int slotNumber = x;
            addSlot(new Slot(playerInv, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
        }

        final int PLAYER_INVENTORY_XPOS = 8;
        // Add the rest of the player's inventory to the gui
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new Slot(playerInv, slotNumber, xpos, ypos));
            }
        }
    }

    @Override
    // Return true if player can interact with this container, false if not
    public boolean stillValid(@Nonnull PlayerEntity player)
    {
        return true;
    }

    @Override
    // What happens when an item is SHIFT-clicked in this container
    public ItemStack quickMoveStack(PlayerEntity player, int sourceSlotIndex)
    {
        return ItemStack.EMPTY;
    }
}
