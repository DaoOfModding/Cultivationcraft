package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.CultivatorBlood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;

public class BloodPart extends BodyPartOption
{
    Blood bloodType = new CultivatorBlood();

    public BloodPart(String partID, String position, String subPosition, String displayNamePos)
    {
        super(partID, position, subPosition, displayNamePos);
    }

    public void setBloodType(Blood newBloodType)
    {
        bloodType = newBloodType;
    }

    public Blood getBloodType()
    {
        return bloodType;
    }
}
