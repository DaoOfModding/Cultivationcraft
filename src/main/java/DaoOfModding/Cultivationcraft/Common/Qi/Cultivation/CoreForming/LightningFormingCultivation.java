package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.FireModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.LightningModifier;

public class LightningFormingCultivation extends CoreFormingCultivation
{
    public LightningFormingCultivation()
    {
        super();

        setElement(Elements.lightningElement);
        modifiers.add(new LightningModifier());

        ID = "cultivationcraft.cultivation.coreforming.lightning";
    }
}
