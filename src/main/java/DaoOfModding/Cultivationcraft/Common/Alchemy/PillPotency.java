package DaoOfModding.Cultivationcraft.Common.Alchemy;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.QiCondenserCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.FoundationEstablishmentCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public final class PillPotency {
    public static double restorationMultiplier(Player player, int pillTier) {
        if (player == null) return 1;
        return Math.pow(.5, Math.max(0, realm(player) - Math.max(1, pillTier)));
    }

    public static int realm(Player player) {
        var cultivation = CultivatorStats.getCultivatorStats(player).getCultivation();
        return cultivation instanceof CoreFormingCultivation ? 3 : cultivation instanceof QiCondenserCultivation ? 2
                : cultivation instanceof FoundationEstablishmentCultivation ? 1 : 0;
    }

    public static boolean canCultivate(Player player, int pillTier) {
        return CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.QI_CONDENSER
                && realm(player) == pillTier;
    }

    public static Component realmName(int tier) {
        return Component.translatable("cultivationcraft.cultivation." + switch (tier) {
            case 2 -> "qicondensation";
            case 3 -> "coreforming";
            default -> "foundation";
        });
    }
}
