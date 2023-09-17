package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.PlayerUtils;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.MovementOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Reflection;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.MultiLimbedModel;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class JetLegTechnique extends MovementOverrideTechnique
{
    protected boolean jump = false;
    protected boolean enabled = false;
    protected int enabledTicks = 0;

    public JetLegTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.jetlegs";
        type = useType.Toggle;
        multiple = false;
        momentum = true;
        momentumFactor = 0.2f;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/jetlegs.png");

        addTechniqueStat(DefaultTechniqueStatIDs.breathCost, 0.025);
    }

    @Override
    public boolean isValid(Player player)
    {
        // Technique is valid if the player is a body cultivator with Jet Legs
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                BodyModifications.getBodyModifications(player).hasModification(BodyPartNames.legPosition, BodyPartNames.jetLegPart))
            return true;

        return false;
    }

    protected void calculateJetPower(Player player)
    {
        PlayerStatControl stats = BodyPartStatControl.getPlayerStatControl(player);

        float weightModifier = stats.getFlightWeightModifier();

        float upwardsPower = stats.getStats().getStat(StatIDs.flightSpeed);
        double y = (upwardsPower * weightModifier);

        setMomentum(new Vec3(0, y, 0));
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        // Don't do anything if the player is in the water
        if (event.player.isInWater())
            return;

        // Activate the leg jets if player is moving upwards
        if (PlayerUtils.isClientPlayerCharacter(event.player))
        {
            if (jump)
                enableJets(true, event.player);
            else
                enableJets(false, event.player);
        }
        else
        {
            if (enabled)
                enableJets(true, event.player);
            else
                enableJets(false, event.player);
        }

        if (enabled)
            calculateJetPower(event.player);
        else
            setMomentum(new Vec3(0, 0, 0));


        jump = false;

        QuestHandler.progressQuest(event.player, Quest.FLIGHT, currentSpeed.length());
    }

    @Override
    public void tickInactiveClient(TickEvent.PlayerTickEvent event)
    {
        super.tickInactiveClient(event);

        setMomentum(new Vec3(0, 0, 0));
        jump = false;

        QuestHandler.progressQuest(event.player, Quest.FLIGHT, currentSpeed.length());
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        Reflection.allowFlight((ServerPlayer) event.player);

        super.tickServer(event);
        tickInactiveServer(event);

        if (enabled && !PlayerHealthManager.getLungs(event.player).drainBreath(Breath.FIRE, (float)getTechniqueStat(DefaultTechniqueStatIDs.breathCost, event.player)))
            enabled = false;

        if (enabled)
            enabledTicks++;
        else
            enabledTicks--;

        enabledTicks = Math.min(20, Math.max(0, enabledTicks));
    }

    public void onFall(LivingFallEvent event)
    {
        if (enabledTicks > 10)
        {
            event.setCanceled(true);
        }
        else if (enabledTicks > 5)
        {
            float damageMultiplier = 1 - ((enabledTicks - 5) / 5f);
            event.setDamageMultiplier(event.getDamageMultiplier() * damageMultiplier);
        }
    }

    protected void enableJets(Boolean on, Player player)
    {
        MultiLimbedModel model = PoseHandler.getPlayerPoseHandler(player.getUUID()).getPlayerModel();

        // Do nothing if the model has yet to be initialised
        if (model == null)
            return;

        if (on)
        {
            model.getLimb(BodyPartModelNames.jetLegLeftEmitter).getModelPart().visible = true;
            model.getLimb(BodyPartModelNames.jetLegRightEmitter).getModelPart().visible = true;

            PoseHandler.getPlayerPoseHandler(player.getUUID()).disableJumpingAnimationThisTick = true;
        }
        else
        {
            model.getLimb(BodyPartModelNames.jetLegLeftEmitter).getModelPart().visible = false;
            model.getLimb(BodyPartModelNames.jetLegRightEmitter).getModelPart().visible = false;
        }

        // Do nothing if not trying to change jet state
        if (enabled == on)
            return;

        enabled = on;

        // Only send info if this is the player character
        if (PlayerUtils.isClientPlayerCharacter(player))
        {
            if (enabled)
                sendInfo(1);
            else
                sendInfo(0);
        }
    }

    @Override
    public boolean overwriteJump()
    {
        Player player = genericClientFunctions.getPlayer();
        // Do nothing if the player is in water
        if (player.isInWater())
            return false;

        // Do nothing if player is out of stamina
        if (!PlayerHealthManager.getLungs(player).drainBreath(Breath.FIRE, (float)getTechniqueStat(DefaultTechniqueStatIDs.breathCost, player)))
            return false;

        jump = true;

        return true;
    }

    @Override
    public void deactivate(Player player)
    {
        if (player.level.isClientSide)
        {
            MultiLimbedModel model = PoseHandler.getPlayerPoseHandler(player.getUUID()).getPlayerModel();

            model.getLimb(BodyPartModelNames.jetLegLeftEmitter).getModelPart().visible = false;
            model.getLimb(BodyPartModelNames.jetLegRightEmitter).getModelPart().visible = false;
        }

        super.deactivate(player);
    }

    @Override
    // Process a received int info packet
    public void processInfo(Player player, int info)
    {
        boolean on = false;

        if (info == 1)
            on = true;

        if (!player.level.isClientSide())
            enabled = on;
        else
            enableJets(on, player);
    }
}
