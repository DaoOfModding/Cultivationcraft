package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.PlayerUtils;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.MovementOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.MultiLimbedModel;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class JetTechnique extends MovementOverrideTechnique
{
    public static final ResourceLocation jetQuest = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.jet");

    boolean enabled = false;
    boolean forward = true;

    public JetTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.jet";
        type = useType.Toggle;
        multiple = false;
        momentum = true;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/jets.png");

        addTechniqueStat(DefaultTechniqueStatIDs.breathCost, 0.025);
        addTechniqueStat(DefaultTechniqueStatIDs.movementSpeed, 0.1);
    }

    @Override
    public PlayerStatModifications getStats()
    {
        return stats;
    }

    @Override
    public boolean isValid(Player player)
    {
        // Technique is valid if the player is a body cultivator with appropriate teeth
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.BODY_CULTIVATOR &&
                (BodyModifications.getBodyModifications(player).hasOption(BodyPartNames.bodyPosition, BodyPartNames.backSubPosition, BodyPartNames.jetPart)))
            return true;

        return false;
    }

    @Override
    public boolean overwriteForward()
    {
        forward = true;
        return false;
    }

    @Override
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        super.tickClient(event);

        // Do nothing if player isn't pressing forward or is not in water or does not have the stamina remaining to move
        if (!forward || event.player.isInWater() || !PlayerHealthManager.getLungs(event.player).drainBreath(Breath.FIRE, (float)getTechniqueStat(DefaultTechniqueStatIDs.breathCost, event.player)))
        {
            forward = false;
            setMomentum(new Vec3(0, 0, 0));
        }
        else
        {
            Vec3 forward = event.player.getForward();
            setMomentum(new Vec3(forward.x, 0, forward.z).normalize().scale(getTechniqueStat(DefaultTechniqueStatIDs.movementSpeed, event.player)));
        }

        // Only toggle jets for player character
        if (event.player.getUUID().compareTo(Minecraft.getInstance().player.getUUID()) == 0)
        {
            if (forward)
                enableJets(true, event.player);
            else
                enableJets(false, event.player);
        }

        forward = false;
        QuestHandler.progressQuest(event.player, jetQuest, currentSpeed.length());
    }

    @Override
    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        super.tickServer(event);

        if (enabled)
            PlayerHealthManager.getLungs(event.player).drainBreath(Breath.FIRE, (float)getTechniqueStat(DefaultTechniqueStatIDs.breathCost, event.player));
    }

    @Override
    public void tickInactiveClient(TickEvent.PlayerTickEvent event)
    {
        super.tickInactiveClient(event);

        enableJets(false, event.player);
        setMomentum(new Vec3(0, 0, 0));
        forward = false;
        QuestHandler.progressQuest(event.player, jetQuest, currentSpeed.length());
    }

    @Override
    public void deactivate(Player player)
    {
        forward = false;

        if (player.level.isClientSide)
            enableJets(false, player);

        super.deactivate(player);
    }

    protected void enableJets(Boolean on, Player player)
    {
        MultiLimbedModel model = PoseHandler.getPlayerPoseHandler(player.getUUID()).getPlayerModel();

        // Do nothing if the model has yet to be initialised
        if (model == null)
            return;

        if (on)
        {
            model.getLimb(BodyPartModelNames.jetLeftFlame).getModelPart().visible = true;
            model.getLimb(BodyPartModelNames.jetRightFlame).getModelPart().visible = true;
            model.getLimb(BodyPartModelNames.jetLeftSmoke).getModelPart().visible = false;
            model.getLimb(BodyPartModelNames.jetRightSmoke).getModelPart().visible = false;
        }
        else
        {
            model.getLimb(BodyPartModelNames.jetLeftFlame).getModelPart().visible = false;
            model.getLimb(BodyPartModelNames.jetRightFlame).getModelPart().visible = false;
            model.getLimb(BodyPartModelNames.jetLeftSmoke).getModelPart().visible = true;
            model.getLimb(BodyPartModelNames.jetRightSmoke).getModelPart().visible = true;
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
