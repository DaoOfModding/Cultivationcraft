package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Particles.WindParticle.WindParticleData;
import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.mlmanimator.Client.Models.MultiLimbedModel;
import DaoOfModding.mlmanimator.Client.Models.ParticleEmitter;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class AirJumpLegPart extends MovementOverridePart
{
    protected boolean jump = false;
    protected boolean jumpWasDown = true;
    protected float jumpBreathCost = 75;

    public AirJumpLegPart(String partID, String position, String displayNamePos)
    {
        super(partID, position, displayNamePos);
    }

    @Override
    public void onClientTick(Player player)
    {
        if (jump)
        {
            if (jumpWasDown)
            {
                if (!player.isInWater())
                {
                    if (player.isOnGround())
                        Physics.applyJump(player);
                    else if (PlayerHealthManager.getLungs(player).drainBreath(Breath.WIND, jumpBreathCost)) {
                        player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
                        Physics.applyJump(player);

                        MultiLimbedModel model = PoseHandler.getPlayerPoseHandler(player.getUUID()).getPlayerModel();

                        ParticleOptions particle = new WindParticleData(player.position(), null);
                        ((ParticleEmitter) model.getLimb(BodyPartModelNames.jetLegLeftEmitter)).spawnParticle(player, particle);
                        ((ParticleEmitter) model.getLimb(BodyPartModelNames.jetLegRightEmitter)).spawnParticle(player, particle);
                    }
                }

                jumpWasDown = false;
            }
        }
        else
            jumpWasDown = true;


        jump = false;

        return;
    }

    @Override
    public void onServerTick(Player player)
    {
        Reflection.allowFlight((ServerPlayer) player);

        if (jump)
            PlayerHealthManager.getLungs(player).drainBreath(Breath.WIND, jumpBreathCost);

        jump = false;
    }

    @Override
    public boolean overwriteJump()
    {
        if (!jumpWasDown && jump == false)
            sendInfo(1);

        jump = true;

        return !jumpWasDown;
    }

    @Override
    // Process a received int info packet
    public void processInfo(Player player, int info)
    {
        if (info == 1)
            jump = true;
    }
}
