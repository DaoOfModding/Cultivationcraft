package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class QuestHandler
{
    public static double getQuestProgress(Player player)
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);
        return modifications.getQuestProgress();
    }

    public static void progressQuest(Player player, ResourceLocation mode, double amount)
    {
        // Do nothing if this is client side
        if (player.isLocalPlayer())
            return;

        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        // Do nothing if not a body cultivator
        if (stats.getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return;

        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        // Do nothing if nothing is set as the last forged modification
        if (modifications.getLastForged().compareTo("") == 0)
            return;

        BodyPart part = BodyPartNames.getPart(modifications.getLastForged());

        if (part == null)
            part = BodyPartNames.getOption(modifications.getLastForged());

        Quest quest = part.getQuest();

        // Do nothing if quest doesn't exist
        if (quest == null)
        {
            modifications.setLastForged("");
            return;
        }

        double progress = quest.progress(mode, amount);

        // Do nothing is quest doesn't progress
        if (progress == 0)
            return;

        progress = modifications.getQuestProgress() + progress;

        // If the quest isn't complete then update the progress for everyone
        if (progress < quest.complete)
        {
            modifications.setQuestProgress(progress);
            PacketHandler.sendBodyModificationsToClient(player);
            return;
        }

        // If the quest is complete then reset the last forged modification and quest progress
        modifications.setLastForged("");
        modifications.setQuestProgress(0);
        PacketHandler.sendBodyModificationsToClient(player);
    }
}
