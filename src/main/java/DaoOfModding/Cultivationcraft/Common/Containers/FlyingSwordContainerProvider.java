package DaoOfModding.Cultivationcraft.Common.Containers;

import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class FlyingSwordContainerProvider implements INamedContainerProvider
{
    private ServerPlayerEntity owner;

    public FlyingSwordContainerProvider(ServerPlayerEntity player)
    {
        owner = player;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return (ITextComponent)(new StringTextComponent("Bind Item"));
    }

    // Creating container on the server
    @Override
    public FlyingSwordContainer createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return FlyingSwordContainer.createContainerServerSide(windowID, playerInventory, FlyingSwordContainerItemStack.getCapability(playerEntity).getItemStackHandler());
    }
}
