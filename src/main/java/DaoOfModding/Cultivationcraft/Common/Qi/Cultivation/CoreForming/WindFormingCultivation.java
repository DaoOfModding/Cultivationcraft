package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.FireModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.WindModifier;

public class WindFormingCultivation extends CoreFormingCultivation
{
    public WindFormingCultivation()
    {
        super();

        setElement(Elements.windElement);
        modifiers.add(new WindModifier());

        ID = "cultivationcraft.cultivation.coreforming.wind";
    }
}
