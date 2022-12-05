package DaoOfModding.Cultivationcraft.Common.Containers;

import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;

public class FlyingSwordContainerProvider implements MenuProvider
{
    private ServerPlayer owner;

    public FlyingSwordContainerProvider(ServerPlayer player)
    {
        owner = player;
    }

    @Override
    public Component getDisplayName()
    {
        return Component.literal("Bind Item");
    }

    // Creating container on the server
    @Override
    public FlyingSwordContainer createMenu(int windowID, Inventory playerInventory, Player Player)
    {
        return FlyingSwordContainer.createContainerServerSide(windowID, playerInventory, FlyingSwordContainerItemStack.getCapability(Player).getItemStackHandler());
    }
}
