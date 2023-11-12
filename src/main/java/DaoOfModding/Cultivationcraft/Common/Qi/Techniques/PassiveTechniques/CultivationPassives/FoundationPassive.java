package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultCultivationStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;

public class FoundationPassive extends BasePassive
{
    public FoundationPassive()
    {
        super();

        canLevel = true;

        TechniqueStatModification maxQiModification = new TechniqueStatModification(DefaultCultivationStatIDs.maxQi);

        maxQiModification.addStatChange(DefaultCultivationStatIDs.maxQi, 1);

        addTechniqueStat(DefaultCultivationStatIDs.maxQi, 0, maxQiModification);
    }
}
