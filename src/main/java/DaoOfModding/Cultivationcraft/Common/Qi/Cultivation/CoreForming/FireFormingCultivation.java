package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.FireModifier;

public class FireFormingCultivation extends CoreFormingCultivation
{
    public FireFormingCultivation()
    {
        super();

        setElement(Elements.fireElement);
        modifiers.add(new FireModifier());

        ID = "cultivationcraft.cultivation.coreforming.fire";
    }
}
