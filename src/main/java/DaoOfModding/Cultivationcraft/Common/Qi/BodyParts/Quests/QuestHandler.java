package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.debug;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
        amount *= debug.questProgressSpeed;

        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        // Do nothing if not a body cultivator
        if (stats.getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return;

        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        // Do nothing if nothing is set as the last forged modification
        if (modifications.getLastForged().compareTo("") == 0)
            return;

        BodyPart part = BodyPartNames.getPartOrOption(modifications.getLastForged());

        Quest quest = part.getQuest();

        // Do nothing if quest doesn't exist
        if (quest == null)
        {
            modifications.setLastForged("");
            return;
        }

        double progress = quest.progress(mode, amount);

        // Do nothing is quest doesn't progress
        if (progress == 0 && !debug.skipQuest)
            return;

        progressQuest(player, progress);
    }

    public static void progressQuest(Player player, double progress)
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        if (player.isLocalPlayer())
        {
            modifications.setQuestProgress(modifications.getQuestProgress() + progress);
            ClientPacketHandler.sendQuestProgressToServer(player.getUUID(), progress);
            return;
        }

        if (!debug.skipQuest)
        {
            BodyPart part = BodyPartNames.getPartOrOption(modifications.getLastForged());
            Quest quest = part.getQuest();

            progress = modifications.getQuestProgress() + progress;

            // If the quest isn't complete then update the progress
            if (progress < quest.complete)
            {
                modifications.setQuestProgress(progress);
                return;
            }
        }

        // If the quest is complete then reset the last forged modification and quest progress
        modifications.setLastForged("");
        modifications.setQuestProgress(0);
        PacketHandler.sendBodyModificationsToClient(player);
    }
}
