package DaoOfModding.Cultivationcraft.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class genericClientFunctions
{
    public static Player getPlayer()
    {
        return Minecraft.getInstance().player;
    }
}
