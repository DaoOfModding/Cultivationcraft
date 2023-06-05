package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.BodyForgeTechniques;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModelNames;
import DaoOfModding.Cultivationcraft.Client.Physics;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.MovementOverrideTechnique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import DaoOfModding.mlmanimator.Client.Models.MultiLimbedModel;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class JetTechnique extends MovementOverrideTechnique
{
    public static final ResourceLocation jetQuest = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.jet");
    protected static final float speed = 0.2f;
    protected static final float staminaUse = 0.01f;

    boolean enabled = false;
    boolean forward = true;

    protected Vec3 targetSpeed = new Vec3(0, 0, 0);
    protected Vec3 currentSpeed = new Vec3(0, 0, 0);

    public JetTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.jet";
        type = useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/jets.png");
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
        if (!forward || event.player.isInWater() || !StaminaHandler.consumeStamina(event.player, staminaUse))
        {
            forward = false;
            targetSpeed = new Vec3(0, 0, 0);
        }
        else
        {
            targetSpeed = event.player.getForward();
            targetSpeed = new Vec3(targetSpeed.x, 0, targetSpeed.z).normalize().scale(speed);
        }

        applySpeed(event.player);
    }

    @Override
    public void tickInactiveClient(TickEvent.PlayerTickEvent event)
    {
        super.tickInactiveClient(event);

        targetSpeed = new Vec3(0, 0, 0);
        forward = false;

        applySpeed(event.player);
    }

    @Override
    public void deactivate(Player player)
    {
        forward = false;
        enableJets(false, player);

        super.deactivate(player);
    }

    protected void applySpeed(Player player)
    {
        // Only toggle jets for player character
        if (player.getUUID().compareTo(Minecraft.getInstance().player.getUUID()) == 0)
        {
            if (forward)
                enableJets(true, player);
            else
                enableJets(false, player);
        }

        QuestHandler.progressQuest(player, jetQuest, currentSpeed.length());

        // Slowly ramp up to the target speed
        currentSpeed = currentSpeed.lerp(targetSpeed, 0.1);

        player.setDeltaMovement(player.getDeltaMovement().add(currentSpeed));

        forward = false;
    }

    protected void enableJets(Boolean on, Player player)
    {
        // Do nothing if not trying to change jet state
        if (enabled == on)
            return;

        MultiLimbedModel model = PoseHandler.getPlayerPoseHandler(player.getUUID()).getPlayerModel();

        // Do nothing if the model has yet to be initialised
        if (model == null)
            return;

        enabled = on;

        // Only send info if this is the player character
        if (Minecraft.getInstance().player == player)
        {
            if (enabled)
                sendInfo(1);
            else
                sendInfo(0);
        }

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
