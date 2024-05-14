package DaoOfModding.Cultivationcraft.Common.Qi.Quests;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.PlayerUtils;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.ExternalCultivationHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.debug;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
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
        progressQuest(player, mode, amount, null);
    }

    public static void progressQuest(Player player, ResourceLocation mode, double amount, ResourceLocation extra)
    {
        // Don't do anything if this is client side and the player is not the player character
        if (player.level.isClientSide && !PlayerUtils.isClientPlayerCharacter(player))
            return;

        amount *= debug.questProgressSpeed;

        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        // Do nothing if not a body cultivator
        if (stats.getCultivationType() == CultivationTypes.BODY_CULTIVATOR)
            progressQuestInternal(player, mode, amount, extra);
        else if (stats.getCultivationType() == CultivationTypes.QI_CONDENSER)
            progressQuestExternal(player, mode, amount, extra);
    }

    public static void progressQuestExternal(Player player, ResourceLocation mode, double amount, ResourceLocation extra)
    {
        ICultivatorStats stats = CultivatorStats.getCultivatorStats(player);

        for (TechniqueModifier modifier : ExternalCultivationHandler.getModifiers())
        {
            if (modifier.canUse(player) && !modifier.hasLearnt(player) && modifier.canLearn(player))
            {
                double progress = modifier.getUnlockQuest().progress(mode, amount, extra);

                if (progress > 0)
                {
                    progress += stats.getConceptProgress(modifier.ID);

                    stats.setConceptProgress(modifier.ID, progress);

                    if (modifier.hasLearnt(player))
                    {
                        // TODO: Message concept has been learnt
                    }
                }
            }
        }

        // Try to progress the Cultivation quest if it exists, sending progress to clients if it progresses
        if (stats.getCultivation().getQuest() != null)
            if (stats.getCultivation().progressQuest(stats.getCultivation().getQuest().progress(mode, amount, extra)))
                PacketHandler.sendCultivatorStatsToClient(player);
    }

    public static void progressQuestInternal(Player player, ResourceLocation mode, double amount, ResourceLocation extra)
    {
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

        double progress = quest.progress(mode, amount, extra);

        // Do nothing if quest doesn't progress
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
