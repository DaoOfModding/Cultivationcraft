package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.EarthModifier;

public class EarthFormingCultivation extends CoreFormingCultivation
{
    public EarthFormingCultivation()
    {
        super();

        setElement(Elements.earthElement);
        modifiers.add(new EarthModifier());

        ID = "cultivationcraft.cultivation.coreforming.earth";
    }
}
