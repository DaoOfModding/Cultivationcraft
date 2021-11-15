package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;

public class StomachPart extends BodyPartOption
{
    QiFoodStats foodStats = new QiFoodStats();

    public StomachPart(String partID, String position, String subPosition, String displayNamePos, int qiToForge)
    {
        super(partID, position, subPosition, displayNamePos, qiToForge);
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
