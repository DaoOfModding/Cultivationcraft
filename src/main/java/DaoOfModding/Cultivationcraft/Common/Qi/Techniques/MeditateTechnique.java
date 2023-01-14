package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSources;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.QiSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.PlayerStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Server.BodyPartControl;
import DaoOfModding.Cultivationcraft.debug;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;

import java.util.List;

public class MeditateTechnique extends MovementOverrideTechnique
{
    public MeditateTechnique()
    {
        super();

        langLocation = "cultivationcraft.technique.meditate";
        elementID = Elements.noElementID;

        type = Technique.useType.Toggle;
        multiple = false;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/meditate.png");

        pose = GenericQiPoses.CrossLegs.clone();
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
        increaseProgress(event);

        super.tickClient(event);
    }

    // Increase the bodyforge progress
    protected void increaseProgress(TickEvent.PlayerTickEvent event)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(event.player);

        if (stats.getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
        {
            IBodyModifications modifications = BodyModifications.getBodyModifications(event.player);

            // Only absorb Qi if a part has been selected
            if (modifications.getSelection().compareTo("") != 0)
            {
                float absorbSpeed = BodyPartStatControl.getPlayerStatControl(event.player.getUUID()).getStats().getStat(StatIDs.qiAbsorb);

                int toAdd = QiSource.getDefaultQi();
                int remaining = (int)absorbSpeed;

                // Draw Qi from each Qi source available
                // TODO: Elemental stuff
                for (QiSource source : ChunkQiSources.getQiSourcesInRange(event.player.level, event.player.position(), (int)BodyPartStatControl.getPlayerStatControl(event.player.getUUID()).getStats().getStat(StatIDs.qiAbsorbRange)))
                {
                    // Do not absorb more qi than the players max absorb speed
                    if (remaining > 0)
                    {
                        int absorbed = source.absorbQi(remaining);
                        remaining -= absorbed;
                        toAdd += absorbed;
                    }
                }

                // DEBUG - increase qiAbsorption speed by the debug amount
                toAdd *= debug.qiCollectingSpeed;

                modifications.addProgress(toAdd);
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

    @Override
    public boolean overwriteForward()
    {
        return true;
    }

    @Override
    public boolean overwriteLeft()
    {
        return true;
    }

    @Override
    public boolean overwriteRight()
    {
        return true;
    }

    @Override
    public boolean overwriteBackward()
    {
        return true;
    }

    @Override
    public boolean overwriteJump()
    {
        return true;
    }
}
