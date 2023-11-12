package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultCultivationStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class BasePassive  extends PassiveTechnique
{
    public BasePassive()
    {
        super();

        addTechniqueStat(DefaultCultivationStatIDs.maxQi, 0);
        addTechniqueStat(DefaultCultivationStatIDs.qiAbsorbRange, 0);
        addTechniqueStat(DefaultCultivationStatIDs.qiAbsorbSpeed, 1);
    }
}
