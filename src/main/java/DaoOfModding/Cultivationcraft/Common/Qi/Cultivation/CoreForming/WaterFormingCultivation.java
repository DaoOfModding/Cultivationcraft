package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.FireModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.WaterModifier;

public class WaterFormingCultivation extends CoreFormingCultivation
{
    public WaterFormingCultivation()
    {
        super();

        setElement(Elements.waterElement);
        modifiers.add(new WaterModifier());

        ID = "cultivationcraft.cultivation.coreforming.water";
    }
}
