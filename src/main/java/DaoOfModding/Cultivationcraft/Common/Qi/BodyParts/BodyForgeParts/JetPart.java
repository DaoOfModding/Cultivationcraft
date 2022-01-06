package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.mlmanimator.Client.Models.MultiLimbedModel;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class JetPart extends BodyPartOption
{
    boolean enabled = false;

    PlayerStatModifications enabledStats = new PlayerStatModifications();

    public JetPart(String partID, String position, String subPosition, String displayNamePos, int qiToForge)
    {
        super(partID, position, subPosition, displayNamePos, qiToForge);

        enabledStats.setStat(StatIDs.movementSpeed, 0.1f);
    }

    // Only add enabledStats when the jets are enabled
    @Override
    public PlayerStatModifications getStatChanges()
    {
        if (enabled)
        {
            PlayerStatModifications newModifications = new PlayerStatModifications();
            newModifications.combine(enabledStats);
            newModifications.combine(super.getStatChanges());

            return newModifications;
        }
        else
            return super.getStatChanges();
    }

    @Override
    public void onClientTick(PlayerEntity player)
    {
        // Disable jets if in water
        if (player.isInWater())
        {
            enableJets(false, player);
            return;
        }

        Vector3d delta = PoseHandler.getPlayerPoseHandler(player.getUUID()).getDeltaMovement();
        Vector3d move = new Vector3d(delta.x, 0, delta.z).normalize();
        Vector3d direction = player.getForward().normalize();

        double dot = move.dot(direction);

        if (dot > 0 && move.length() > 0)
            enableJets(true, player);
        else
            enableJets(false, player);
    }

    protected void enableJets(Boolean on, PlayerEntity player)
    {

        // Do nothing if not trying to change jet state
        if (enabled == on)
            return;

        MultiLimbedModel model = PoseHandler.getPlayerPoseHandler(player.getUUID()).getPlayerModel();

        // Do nothing if the model has yet to be initialised
        if (model == null)
            return;

        enabled = on;

        if (enabled)
            sendInfo(1, BodyPartNames.backSubPosition, BodyPartNames.bodyPosition);
        else
            sendInfo(0, BodyPartNames.backSubPosition, BodyPartNames.bodyPosition);

        if (on)
        {
            model.getLimb(BodyPartModelNames.jetLeftFlame).visible = true;
            model.getLimb(BodyPartModelNames.jetRightFlame).visible = true;
            model.getLimb(BodyPartModelNames.jetLeftSmoke).visible = false;
            model.getLimb(BodyPartModelNames.jetRightSmoke).visible = false;
        }
        else
        {
            model.getLimb(BodyPartModelNames.jetLeftFlame).visible = false;
            model.getLimb(BodyPartModelNames.jetRightFlame).visible = false;
            model.getLimb(BodyPartModelNames.jetLeftSmoke).visible = true;
            model.getLimb(BodyPartModelNames.jetRightSmoke).visible = true;
        }

        BodyPartStatControl.updateStats(player);
    }

    @Override
    // Process a received int info packet
    public void processInfo(PlayerEntity player, int info)
    {
        if (info == 1)
            enabled = true;
        else
            enabled = false;

        BodyPartStatControl.updateStats(player);
    }
}
