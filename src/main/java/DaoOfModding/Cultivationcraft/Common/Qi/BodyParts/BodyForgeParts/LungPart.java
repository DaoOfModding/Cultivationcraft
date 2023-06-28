package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lungs;

public class LungPart extends BodyPartOption
{
    Lungs lungType = new Lungs();

    public LungPart(String partID, String position, String subPosition, String displayNamePos)
    {
        super(partID, position, subPosition, displayNamePos);
    }

    public void setLungType(Lungs newLung)
    {
        lungType = newLung;
    }

    public Lungs getLungType()
    {
        return lungType;
    }
}
