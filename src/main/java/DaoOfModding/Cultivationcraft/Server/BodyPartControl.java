package DaoOfModding.Cultivationcraft.Server;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.debug;
import net.minecraft.world.entity.player.Player;

public class BodyPartControl
{
    public static void checkForgeProgress(Player player)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        if (stats.getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
        {
            IBodyModifications modifications = BodyModifications.getBodyModifications(player);
            BodyPart toComplete = BodyPartNames.getPartOrOption(modifications.getSelection());

            if (toComplete == null)
                return;

            // If the QI progress is higher than the QI needed for this part
            // Add this part onto player's body modifications and clear the selection
            if (BodyPartStatControl.getStats(player).getStat(StatIDs.qiCost) < modifications.getProgress())
            {
                if (toComplete instanceof BodyPartOption)
                    modifications.setOption((BodyPartOption)toComplete);
                else
                    modifications.setModification(toComplete);

                if (!debug.skipQuest)
                {
                    modifications.setLastForged(modifications.getSelection());
                    modifications.setQuestProgress(0);
                }

                modifications.setSelection("");
                modifications.setProgress(0);

                PacketHandler.sendBodyModificationsToClient(player);

                CultivatorTechniques.getCultivatorTechniques(player).determinePassives(player);

                BodyPartStatControl.addStats(player, toComplete.getStatChanges());
                BodyPartStatControl.updateStats(player);
            }
        }
    }
}
