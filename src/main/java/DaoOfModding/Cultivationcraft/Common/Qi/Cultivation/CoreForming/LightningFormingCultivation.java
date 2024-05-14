package DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreForming;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CoreFormingCultivation;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.EarthModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.FireModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.LightningModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;

public class LightningFormingCultivation extends CoreFormingCultivation
{
    public LightningFormingCultivation()
    {
        super();

        TechniqueModifier mod = new LightningModifier();

        setCore(mod);

        ID = "cultivationcraft.cultivation.coreforming.lightning";
    }
}
