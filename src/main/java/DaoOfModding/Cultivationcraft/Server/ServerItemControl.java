package DaoOfModding.Cultivationcraft.Server;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBind;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.IFlyingSwordBind;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemHandler;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStack;
import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainerProvider;
import DaoOfModding.Cultivationcraft.Common.FlyingSwordController;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ServerItemControl
{
    public static IWorld thisWorld;

    public static void init(FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(ServerItemControl.class);
    }


    // Check for any FlyingSword recalls on the server, set them to false and update clients
    public static void checkForRecalls()
    {
        for(PlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
        {
            ICultivatorStats playerStats = CultivatorStats.getCultivatorStats(player);

            if (playerStats != null && playerStats.getRecall())
            {
                playerStats.setRecall(false);
                PacketHandler.sendRecallFlyingToClient(false, player.getUniqueID());
            }
        }
    }

    public static void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        // Cycle through list of all players, sending their info to the new player
        for(PlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
        {
            // if the player that's info is being sent is the player that joined, send that info to everyone
            if (player == event.getPlayer())
                PacketHandler.sendCultivatorStatsToClient(player);
            else
                PacketHandler.sendCultivatorStatsToSpecificClient(player, (ServerPlayerEntity)event.getPlayer());
        }
    }

    public static void sendPlayerStats(PlayerEntity player, PlayerEntity target)
    {
        PacketHandler.sendCultivatorStatsToSpecificClient(target, (ServerPlayerEntity)player);
    }

    public static void handleKeyPress(Register.keyPresses keyPressed, ServerPlayerEntity pressedBy)
    {
        if (keyPressed == Register.keyPresses.FLYINGSWORDSCREEN)
        {
            INamedContainerProvider flyingSwordContainerProvider = new FlyingSwordContainerProvider(pressedBy);
            NetworkHooks.openGui(pressedBy, flyingSwordContainerProvider);
        }
    }

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

                    bindData.setBindTime(bindData.getBindTime() + time);

                    System.out.println(bindData.getBindTime());

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
}
