package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;

public class WindLung extends QiLung
{
    public WindLung()
    {
        super();

        canBreath = Breath.WIND;
    }

    @Override
    public boolean canBreath(Breath breath)
    {
        Boolean can = false;

        if (canBreath == breath)
            can = true;

        if (breath == Breath.AIR)
            can = true;

        return can;
    }
}
