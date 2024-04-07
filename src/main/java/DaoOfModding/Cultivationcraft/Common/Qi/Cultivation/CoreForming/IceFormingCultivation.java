package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.IceModifier;

public class IceFormingCultivation extends CoreFormingCultivation
{
    public IceFormingCultivation()
    {
        super();

        setElement(Elements.iceElement);
        modifiers.add(new IceModifier());

        ID = "cultivationcraft.cultivation.coreforming.ice";
    }
}
