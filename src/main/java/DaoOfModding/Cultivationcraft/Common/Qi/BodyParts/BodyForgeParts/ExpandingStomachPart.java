package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.DefaultPlayerBodyPartWeights;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import DaoOfModding.mlmanimator.Client.Models.GenericLimbNames;
import DaoOfModding.mlmanimator.Client.Poses.PlayerPose;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class ExpandingStomachPart extends StomachPart
{
    protected final float maxXSize = 3;
    protected final float maxYSize = 2;
    protected final float maxZSize = 4;

    protected int oldStamina = -1;
    protected float currentStamina = -1;

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
        if (player instanceof ClientPlayerEntity)
            currentStamina = StaminaHandler.getStamina(player);

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
        float foodPercent = currentStamina / StaminaHandler.getMaxStamina(player);

        // If the hunger percent has changed, update the player's weight stat to reflect this change
        if (currentPercent != foodPercent)
        {
            currentPercent = foodPercent;

            // Maybe multiply other than add here
            float weight = DefaultPlayerBodyPartWeights.bodyWeight * (((maxXSize-1) * currentPercent) + ((maxYSize-1) * currentPercent) + ((maxZSize-1) * currentPercent));

            getStatChanges().setStat(StatIDs.weight, weight);
            BodyPartStatControl.updateStats(player);

            update = true;

            if (oldStamina != (int)currentStamina)
            {
                sendInfo((int)currentStamina, BodyPartNames.stomachSubPosition, BodyPartNames.bodyPosition);
                oldStamina = (int) currentStamina;
            }
        }
    }

    protected void updateExpandingPose()
    {
        expandingPose = new PlayerPose();
        expandingPose.addSize(GenericLimbNames.body, new Vector3d(1 + (maxXSize - 1) * currentPercent, 1 + (maxYSize - 1) * currentPercent, 1 + (maxZSize- 1) * currentPercent), 99, 5);
    }

    @Override
    public void processInfo(PlayerEntity player, int info)
    {
        currentStamina = info;
        oldStamina = info;
    }

    @Override
    public void onJoin(PlayerEntity player)
    {
        sendInfo((int)currentStamina, BodyPartNames.stomachSubPosition, BodyPartNames.bodyPosition);
    }
}
