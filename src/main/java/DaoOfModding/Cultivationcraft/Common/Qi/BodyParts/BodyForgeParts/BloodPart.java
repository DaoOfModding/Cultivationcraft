package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.Blood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Blood.QiBlood;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;

public class BloodPart extends BodyPartOption
{
    Blood bloodType = new QiBlood();

    public BloodPart(String partID, String position, String subPosition, String displayNamePos, int qiToForge)
    {
        super(partID, position, subPosition, displayNamePos, qiToForge);
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
