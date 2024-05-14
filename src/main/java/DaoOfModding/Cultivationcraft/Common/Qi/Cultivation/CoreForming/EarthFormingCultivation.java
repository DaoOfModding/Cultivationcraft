package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.EarthModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;

public class EarthFormingCultivation extends CoreFormingCultivation
{
    public EarthFormingCultivation()
    {
        super();

        TechniqueModifier mod = new EarthModifier();

        setElement(mod.getElement());
        setCore(mod);

        ID = "cultivationcraft.cultivation.coreforming.earth";
    }
}
