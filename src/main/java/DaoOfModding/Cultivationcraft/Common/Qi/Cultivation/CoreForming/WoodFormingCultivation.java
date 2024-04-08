package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.FireModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.WoodModifier;

public class WoodFormingCultivation extends CoreFormingCultivation
{
    public WoodFormingCultivation()
    {
        super();

        setElement(Elements.woodElement);
        modifiers.add(new WoodModifier());

        ID = "cultivationcraft.cultivation.coreforming.wood";
    }
}
