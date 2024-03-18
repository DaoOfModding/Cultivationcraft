package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.PlayerUtils;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.debug;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class QuestHandler
{
    protected static HashMap<String, ResourceLocation> damageQuests = new HashMap<>();

    public static void addDamageQuest(String damageSource, ResourceLocation quest)
    {
        damageQuests.put(damageSource, quest);
    }

    public static ResourceLocation getDamageQuest(String damageSource)
    {
        return damageQuests.get(damageSource);
    }

    public static void damageProgress(Player player, QiDamageSource source, float amount)
    {
        ResourceLocation quest = getDamageQuest(source.getMsgId());

        if (quest == null)
            return;

        progressQuest(player, quest, amount);
    }

    public static double getQuestProgress(Player player)
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);
        return modifications.getQuestProgress();
    }

    public static void resetQuest(Player player, ResourceLocation mode)
    {
        // Don't do anything if this is client side and the player is not the player character
        if (player.level.isClientSide && !PlayerUtils.isClientPlayerCharacter(player))
            return;

        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        // Do nothing if not a body cultivator
        if (stats.getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return;

        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        // Do nothing if nothing is set as the last forged modification
        if (modifications.getLastForged().compareTo("") == 0)
            return;

        BodyPart part = BodyPartNames.getPartOrOption(modifications.getLastForged());

        if (part.getQuest().mode.compareTo(mode) == 0)
            progressQuest(player, mode, modifications.getQuestProgress() * -1);
    }

    public static void progressQuest(Player player, ResourceLocation mode, double amount)
    {
        // Don't do anything if this is client side and the player is not the player character
        if (player.level.isClientSide && !PlayerUtils.isClientPlayerCharacter(player))
            return;

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

        if (PlayerUtils.isClientPlayerCharacter(player))
            ClientPacketHandler.sendQuestProgressToServer(player.getUUID(), progress);
        else if (!player.level.isClientSide)
            PacketHandler.sendQuestProgressToClient(player.getUUID(), progress);


        progressQuest(player, progress);
    }

    public static void progressQuest(Player player, double progress)
    {
        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        if (PlayerUtils.isClientPlayerCharacter(player))
        {
            modifications.setQuestProgress(modifications.getQuestProgress() + progress);
            return;
        }

        if (!debug.skipQuest)
        {
            BodyPart part = BodyPartNames.getPartOrOption(modifications.getLastForged());
            Quest quest = part.getQuest();

            if (progress < 0)
                progress = 0;

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
