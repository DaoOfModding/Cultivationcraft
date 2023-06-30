package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.QiLung;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.LungConnection.LeftLungConnection;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.LungConnection.LungConnection;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.LungConnection.RightLungConnection;

public class MixedLungs extends QiLungs
{
    public MixedLungs()
    {
        lung = new LungConnection[]{new LeftLungConnection(new QiLung()), new RightLungConnection(new QiLung())};
    }

    public MixedLungs copy(Lungs lungCopy)
    {
        MixedLungs newLung = new MixedLungs();

        for (int i = 0; i < getLungAmount(); i++)
            newLung.setLung(i, getConnection(i).getLung());

        // ...apparently this is completely unnecessary and just breaks things
        /*for (int i = 0; i < getLungAmount(); i++)
            if (lungCopy.canBreath(lung[i].getLung().getBreath()))
                newLung.lung[i].getLung().setCurrent(lungCopy.getCurrent(lung[i].getLung().getBreath()));*/

        newLung.breathingColor = lungCopy.breathingColor;

        if (lungCopy instanceof QiLungs)
            newLung.suffocating = ((QiLungs) lungCopy).suffocating;

        return newLung;
    }
}
