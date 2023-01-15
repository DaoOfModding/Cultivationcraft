package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;

public class StomachPart extends BodyPartOption
{
    QiFoodStats foodStats = new QiFoodStats();

    public StomachPart(String partID, String position, String subPosition, String displayNamePos)
    {
        super(partID, position, subPosition, displayNamePos);
    }

    public void setFoodStats(QiFoodStats newFoodStats)
    {
        foodStats = newFoodStats;
    }

    public QiFoodStats getFoodStats()
    {
        return foodStats;
    }
}
