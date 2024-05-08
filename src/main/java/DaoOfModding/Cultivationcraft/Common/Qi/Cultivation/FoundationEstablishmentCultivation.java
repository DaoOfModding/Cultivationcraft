package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation;

import DaoOfModding.Cultivationcraft.Client.GUI.Screens.CultivationTypeScreens.FoundationEstablishmentScreen;
import DaoOfModding.Cultivationcraft.Common.Advancements.CultivationAdvancements;
import DaoOfModding.Cultivationcraft.Common.Advancements.Triggers.BreakthroughTrigger;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives.FoundationPassive;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;

public class FoundationEstablishmentCultivation extends CultivationType {
    public FoundationEstablishmentCultivation() {
        this(1);
    }

    public FoundationEstablishmentCultivation(int cultivationStage) {
        super(cultivationStage);

        passive = new FoundationPassive();
        techLevel = 100;
        maxedTechsToBreakthrough = 3;
        maxStage = 5;
        screen = new FoundationEstablishmentScreen();
        tribulation = new Tribulation(maxStage, 10, 0.2f);

        advancements.add(new QiCondenserCultivation(1));

        ID = "cultivationcraft.cultivation.foundation";
    }

    @Override
    public void breakthrough(Player player)
    {
        if (stage < maxStage)
        {
            FoundationEstablishmentCultivation newCultivation = new FoundationEstablishmentCultivation(stage + 1);

            newCultivation.setPreviousCultivation(this);

            CultivatorStats.getCultivatorStats(player).setCultivation(newCultivation);

            if (player instanceof ServerPlayer)
                CultivationAdvancements.HAS_BROKENTROUGH.trigger((ServerPlayer) player, false);
        }
    }
}
