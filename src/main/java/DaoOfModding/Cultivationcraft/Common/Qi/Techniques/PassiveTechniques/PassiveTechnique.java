package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;

public class PassiveTechnique extends Technique
{
    // Passive techniques are always active and don't need to be sloted
    public PassiveTechnique()
    {
        super();

        active = true;
        langLocation = "";
        type = useType.Toggle;
        multiple = false;
    }
}
