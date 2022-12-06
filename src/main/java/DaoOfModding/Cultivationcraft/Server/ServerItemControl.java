package DaoOfModding.Cultivationcraft.Server;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainerProvider;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ServerItemControl
{
    public static boolean loaded = false;


    // Check for any FlyingSword recalls on the server, set them to false and update clients
    public static void checkForRecalls()
    {
        for(Player player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
        {
            if (player.isAlive())
            {
                ICultivatorStats playerStats = CultivatorStats.getCultivatorStats(player);

                if (playerStats != null && playerStats.getRecall()) {
                    playerStats.setRecall(false);
                    PacketHandler.sendRecallFlyingToClient(false, player.getUUID());
                }
            }
        }
    }

    public static void sendPlayerStats(Player player, Player target)
    {
        PacketHandler.sendCultivatorStatsToSpecificClient(target, (ServerPlayer)player);
        PacketHandler.sendBodyModificationsToSpecificClient(target, (ServerPlayer)player);
    }

    public static void handleKeyPress(Register.keyPresses keyPressed, ServerPlayer pressedBy)
    {
        if (keyPressed == Register.keyPresses.FLYINGSWORDSCREEN)
        {
            MenuProvider flyingSwordContainerProvider = new FlyingSwordContainerProvider(pressedBy);
            NetworkHooks.openScreen(pressedBy, flyingSwordContainerProvider);
        }

        if (keyPressed == Register.keyPresses.SKILLHOTBARSWITCH)
        {
            SkillHotbarServer.switchActive(pressedBy.getUUID());
        }
    }
}
