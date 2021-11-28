package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.MovementOverrideTechnique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.MultiLimbedModel;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;

public class JetLegTechnique extends MovementOverrideTechnique
{
    public JetLegTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.jetlegs";
        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/jetlegs.png");
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        // Technique is valid if the player is a body cultivator with Reverse Joint Legs
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                BodyModifications.getBodyModifications(player).hasModification(BodyPartNames.legPosition, BodyPartNames.jetLegPart))
            return true;

        return false;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        // Don't do anything if the player is in the water
        if (event.player.isInWater())
            return;

        MultiLimbedModel model = PoseHandler.getPlayerPoseHandler(event.player.getUUID()).getPlayerModel();

        // Active the leg jets if player is moving upwards
        if (Minecraft.getInstance().player.getDeltaMovement().y > 0)
        {
            model.getLimb(BodyPartModelNames.jetLegLeftEmitter).visible = true;
            model.getLimb(BodyPartModelNames.jetLegRightEmitter).visible = true;
        }
        else
        {
            model.getLimb(BodyPartModelNames.jetLegLeftEmitter).visible = false;
            model.getLimb(BodyPartModelNames.jetLegRightEmitter).visible = false;
        }
    }

    @Override
    public boolean overwriteJump()
    {
        // Do nothing if the player is in water
        if (Minecraft.getInstance().player.isInWater())
            return false;

        PlayerStatControl stats = BodyPartStatControl.getPlayerStatControl(Minecraft.getInstance().player.getUUID());

        float weightModifier = stats.getFlightWeightModifier();
        Vector3d delta = Minecraft.getInstance().player.getDeltaMovement();

        float upwardsPower = stats.getStats().getStat(StatIDs.flightSpeed);
        double y = ((1 - weightModifier) * delta.y) + (upwardsPower * weightModifier);

        Minecraft.getInstance().player.setDeltaMovement(new Vector3d(delta.x, y, delta.z));

        return true;
    }
}
