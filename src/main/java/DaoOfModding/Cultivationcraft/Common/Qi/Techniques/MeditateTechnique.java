package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Server.BodyPartControl;
import DaoOfModding.Cultivationcraft.Server.ServerListeners;
import DaoOfModding.Cultivationcraft.debug;
import DaoOfModding.mlmanimator.Client.Poses.PoseHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.List;

public class MeditateTechnique extends MovementOverrideTechnique
{
    public MeditateTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.meditate";
        Element = Elements.noElement;

        type = Technique.useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/meditate.png");

        pose = GenericQiPoses.CrossLegs.clone();

        stopMovement = true;

        setLegAnimationLockOffWhileActive(20);
    }

    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        increaseProgress(event);
        BodyPartControl.checkForgeProgress(event.player);

        super.tickServer(event);
    }

    public void tickClient(TickEvent.PlayerTickEvent event)
    {
        // We can increase the progress on the client, as it will just be overridden later
        //increaseProgress(event);

        super.tickClient(event);
    }

    // Increase the bodyforge progress (Server only)
    protected void increaseProgress(TickEvent.PlayerTickEvent event)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(event.player);

        if (stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
        {
            CultivationType cultivation = stats.getCultivation();

            List<QiSource> sources = ChunkQiSources.getQiSourcesInRange(event.player.level, event.player.position(), cultivation.getAbsorbRange(event.player));

            // If meditating in a Qi Source, increase the quest by 1 second
            if (sources.size() > 0)
                QuestHandler.progressQuest(event.player, Quest.QI_SOURCE_MEDITATION, 1.0/20.0);

            // absorb Qi through blood first
            int remaining = cultivation.getAbsorbSpeed(event.player);
            remaining = PlayerHealthManager.getBlood(event.player).meditation(remaining, sources, event.player);


            // Passively cultivate
            cultivation.progressCultivation(event.player, QiSource.getDefaultQi(), Elements.noElement);

            // Loop through every source and try to cultivate from it
            for (QiSource source : sources)
                remaining -= cultivation.progressCultivation(event.player, remaining, source.getElement());

            PacketHandler.sendCultivatorStatsToClient(event.player);
        }
        else if (stats.getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
        {
            List<QiSource> sources = ChunkQiSources.getQiSourcesInRange(event.player.level, event.player.position(), (int)BodyPartStatControl.getPlayerStatControl(event.player).getStats().getStat(StatIDs.qiAbsorbRange));

            // If meditating in a Qi Source, increase the quest by 1 second
            if (sources.size() > 0)
                QuestHandler.progressQuest(event.player, Quest.QI_SOURCE_MEDITATION, 1.0/20.0);

            // absorb Qi through blood first
            int remaining = (int)BodyPartStatControl.getPlayerStatControl(event.player).getStats().getStat(StatIDs.qiAbsorb);
            remaining = PlayerHealthManager.getBlood(event.player).meditation(remaining, sources, event.player);

            IBodyModifications modifications = BodyModifications.getBodyModifications(event.player);

            // Only absorb Qi if a part has been selected
            if (modifications.getSelection().compareTo("") != 0)
            {
                ResourceLocation element = BodyPartNames.getPartOrOption(modifications.getSelection()).getElement();

                int toAdd = 0;

                // Only absorb passively if no element is set
                if (element == null || event.player.isCreative())
                    toAdd = QiSource.getDefaultQi();

                // Draw Qi from each Qi source available
                for (QiSource source : sources)
                {
                    // Only absorb from QiSources of the correct element if an element is set to the body part
                    if (element == null || element.compareTo(source.getElement()) == 0)
                        // Do not absorb more qi than the players max absorb speed
                        if (remaining > 0)
                        {
                            int absorbed = source.absorbQi(remaining, event.player);
                            remaining -= absorbed;
                            toAdd += absorbed;
                        }
                }

                // DEBUG - increase qiAbsorption speed by the debug amount
                toAdd *= debug.qiCollectingSpeed;

                modifications.addProgress(toAdd);

                // Update the client progress
                //if (event.side == LogicalSide.SERVER && ServerListeners.tick % 20 == 0)
                PacketHandler.sendBodyModificationsToClient(event.player);
            }
        }
    }

    @Override
    public boolean isValid(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.BODY_CULTIVATOR || stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
            return true;

        return false;
    }
}
