package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.EarthModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.FireModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.WindModifier;

public class WindFormingCultivation extends CoreFormingCultivation
{
    public WindFormingCultivation()
    {
        super();

        TechniqueModifier mod = new WindModifier();

        setElement(mod.getElement());
        setCore(mod);

        ID = "cultivationcraft.cultivation.coreforming.wind";
    }
}
