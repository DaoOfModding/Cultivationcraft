package DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainer;

import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

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
        return ITextComponent.func_244388_a("Test3");
    }

    // Creating container on the server
    @Override
    public FlyingSwordContainer createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return FlyingSwordContainer.createContainerServerSide(windowID, playerInventory);
    }
}
