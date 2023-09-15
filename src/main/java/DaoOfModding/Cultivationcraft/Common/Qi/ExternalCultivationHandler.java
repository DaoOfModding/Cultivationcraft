package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.DefaultCultivation;
import net.minecraft.world.entity.player.Player;

public class ExternalCultivationHandler
{
    public static CultivationType getCultivation(Player player)
    {
        return new DefaultCultivation();
    }
}
