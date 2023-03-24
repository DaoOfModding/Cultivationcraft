package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques;

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
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.mlmanimator.Client.Models.MultiLimbedModel;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

public class JetTechnique extends PassiveTechnique
{
    public static final ResourceLocation jetQuest = new ResourceLocation(Cultivationcraft.MODID, "cultivationcraft.quest.jet");

    boolean enabled = false;

    PlayerStatModifications enabledStats = new PlayerStatModifications();

    public JetTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.jet";
        enabledStats.setStat(StatIDs.movementSpeed, 0.1f);
    }

    @Override
    public PlayerStatModifications getStats()
    {
        if (enabled)
            return enabledStats;
        else
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
    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        // Disable jets if in water
        if (event.player.isInWater())
        {
            enableJets(false, event.player);
            return;
        }

        Vec3 delta = Physics.getDelta(event.player);
        Vec3 move = new Vec3(delta.x, 0, delta.z);
        Vec3 direction = event.player.getForward().normalize();

        QuestHandler.progressQuest(event.player, jetQuest, move.length());

        double dot = move.normalize().dot(direction);

        if (dot > 0 && move.length() > 0)
            enableJets(true, event.player);
        else
            enableJets(false, event.player);
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

        BodyPartStatControl.updateStats(player);
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
