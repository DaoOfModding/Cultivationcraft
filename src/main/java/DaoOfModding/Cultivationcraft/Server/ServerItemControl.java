package DaoOfModding.Cultivationcraft.Server;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainerProvider;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Register;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
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
}
