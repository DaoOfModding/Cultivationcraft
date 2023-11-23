package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.CultivationPassives;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultCultivationStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;

public class QiCondenserPassive extends BasePassive
{
    public QiCondenserPassive()
    {
        super();

        canLevel = true;

        TechniqueStatModification maxQiModification = new TechniqueStatModification(DefaultCultivationStatIDs.maxQi);
        TechniqueStatModification passiveModification = new TechniqueStatModification(DefaultCultivationStatIDs.qiPassiveSpeed);
        TechniqueStatModification absorbModification = new TechniqueStatModification(DefaultCultivationStatIDs.qiAbsorbSpeed);
        TechniqueStatModification passiveAbsorbModification = new TechniqueStatModification(DefaultCultivationStatIDs.qiPassiveAbsorbSpeed);

        maxQiModification.addStatChange(DefaultCultivationStatIDs.maxQi, 1);
        passiveModification.addStatChange(DefaultCultivationStatIDs.qiPassiveSpeed, 0.0001);
        absorbModification.addStatChange(DefaultCultivationStatIDs.qiAbsorbSpeed, 0.01);
        passiveAbsorbModification.addStatChange(DefaultCultivationStatIDs.qiPassiveSpeed, 0.001);

        addTechniqueStat(DefaultCultivationStatIDs.maxQi, 0, maxQiModification);
        addTechniqueStat(DefaultCultivationStatIDs.qiPassiveSpeed, 0, passiveModification);
        addTechniqueStat(DefaultCultivationStatIDs.qiAbsorbSpeed, 0, absorbModification);
        addTechniqueStat(DefaultCultivationStatIDs.qiPassiveSpeed, 0, passiveAbsorbModification);
    }
}
