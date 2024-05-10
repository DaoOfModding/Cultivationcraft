package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.QiModifier;

public class QiFormingCultivation extends CoreFormingCultivation
{
    public QiFormingCultivation()
    {
        super();

        modifiers.add(new QiModifier());

        ID = "cultivationcraft.cultivation.coreforming.qi";
    }
}
