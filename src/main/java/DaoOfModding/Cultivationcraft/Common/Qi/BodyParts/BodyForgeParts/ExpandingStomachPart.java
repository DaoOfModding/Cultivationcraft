package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.DefaultPlayerBodyPartWeights;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class ExpandingStomachPart extends StomachPart
{
    protected final float maxXSize = 3;
    protected final float maxYSize = 2;
    protected final float maxZSize = 4;

    protected float currentPercent;

    protected boolean update = false;

    PlayerPose expandingPose = new PlayerPose();

    public ExpandingStomachPart(String partID, String position, String subPosition, String displayNamePos, int qiToForge)
    {
        super(partID, position, subPosition, displayNamePos, qiToForge);
    }

    @Override
    public void onClientTick(PlayerEntity player)
    {
        updateSizeStats(player);

        if (update)
            updateExpandingPose();

        PoseHandler.addPose(player.getUUID(), expandingPose);
    }

    @Override
    public void onServerTick(PlayerEntity player)
    {
        updateSizeStats(player);
    }

    protected void updateSizeStats(PlayerEntity player)
    {
        float foodPercent = (float)((QiFoodStats)player.getFoodData()).getTrueFoodLevel() / ((QiFoodStats)player.getFoodData()).getMaxFood();

        // If the hunger percent has changed, update the player's weight stat to reflect this change
        if (currentPercent != foodPercent)
        {
            currentPercent = foodPercent;

            getStatChanges().setStat(StatIDs.weight, DefaultPlayerBodyPartWeights.bodyWeight * ((maxXSize-1) * currentPercent) * ((maxYSize-1) * currentPercent) * ((maxZSize-1) * currentPercent));
            BodyPartStatControl.updateStats(player);

            update = true;
        }
    }

    protected void updateExpandingPose()
    {
        expandingPose = new PlayerPose();
        expandingPose.addSize(GenericLimbNames.body, new Vector3d(1 + (maxXSize - 1) * currentPercent, 1 + (maxYSize - 1) * currentPercent, 1 + (maxZSize- 1) * currentPercent), 99, 5);
    }
}
