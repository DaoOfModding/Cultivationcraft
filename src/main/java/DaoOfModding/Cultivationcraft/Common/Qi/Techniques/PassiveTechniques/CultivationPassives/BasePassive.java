package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultCultivationStatIDs;

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
