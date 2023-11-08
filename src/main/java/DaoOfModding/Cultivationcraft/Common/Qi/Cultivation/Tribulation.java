package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import net.minecraft.world.entity.player.Player;

public class Tribulation
{
    public Tribulation()
    {

    }

    public void tick(Player player, int tick)
    {
        if (player.level.isClientSide)
            return;
    }
}
