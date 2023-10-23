package DaoOfModding.Cultivationcraft.Common.Commands;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.BodyPartStatControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Stats.StatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class BodyforgeCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("bodyforge").requires(commandSource -> commandSource.hasPermission(2))
                .then(Commands.literal("reset").then(Commands.argument("players", EntityArgument.players()).executes(commandContext -> reset(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "players")))))
                .then(Commands.literal("completeQuest").then(Commands.argument("players", EntityArgument.players()).executes(commandContext -> completeQuest(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "players")))))
                .then(Commands.literal("completeForge").then(Commands.argument("players", EntityArgument.players()).executes(commandContext -> completeProgress(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "players")))))
                .then(Commands.literal("levelSkills").then(Commands.argument("players", EntityArgument.players()).executes(commandContext -> levelSkills(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "players")))));

        dispatcher.register(command);
    }

    protected static int completeProgress(CommandSourceStack commandSourceStack, Collection<ServerPlayer> players)
    {
        for(ServerPlayer serverplayer : players)
        {
            BodyModifications.getBodyModifications(serverplayer).setProgress((int)BodyPartStatControl.getStats(serverplayer).getStat(StatIDs.qiCost));
            PacketHandler.sendBodyModificationsToClient(serverplayer);
        }

        if (players.size() == 1)
            commandSourceStack.sendSuccess(Component.translatable("cultivationcraft.commands.completeprogress.single", players.iterator().next().getDisplayName()), true);
        else
            commandSourceStack.sendSuccess(Component.translatable("cultivationcraft.commands.completeprogress.multiple", players.size()), true);

        return players.size();
    }

    protected static int completeQuest(CommandSourceStack commandSourceStack, Collection<ServerPlayer> players)
    {
        for(ServerPlayer serverplayer : players)
        {
            BodyModifications.getBodyModifications(serverplayer).setLastForged("");
            PacketHandler.sendBodyModificationsToClient(serverplayer);
        }

        if (players.size() == 1)
            commandSourceStack.sendSuccess(Component.translatable("cultivationcraft.commands.completequest.single", players.iterator().next().getDisplayName()), true);
        else
            commandSourceStack.sendSuccess(Component.translatable("cultivationcraft.commands.completequest.multiple", players.size()), true);

        return players.size();
    }

    protected static int levelSkills(CommandSourceStack commandSourceStack, Collection<ServerPlayer> players)
    {
        for(ServerPlayer serverplayer : players)
        {
            double amount = CultivatorStats.getCultivatorStats(serverplayer).getCultivation().getMaxTechLevel();

            for (Class tech : TechniqueControl.getAvailableTechnics(serverplayer))
            {
                try
                {
                    Technique technique = (Technique) (tech.newInstance());

                    if (technique.isValid(genericClientFunctions.getPlayer()) && technique.canLevel())
                        technique.levelUp(serverplayer, amount);
                }
                catch (Exception e)
                {
                    Cultivationcraft.LOGGER.error(tech.getName() + " is not a Technique: " + e.getMessage());
                }
            }
        }

        if (players.size() == 1)
            commandSourceStack.sendSuccess(Component.translatable("cultivationcraft.commands.levelskills.single", players.iterator().next().getDisplayName()), true);
        else
            commandSourceStack.sendSuccess(Component.translatable("cultivationcraft.commands.levelskills.multiple", players.size()), true);

        return players.size();
    }

    protected static int reset(CommandSourceStack commandSourceStack, Collection<ServerPlayer> players)
    {
        for(ServerPlayer serverplayer : players)
        {
            BodyModifications.getBodyModifications(serverplayer).clearModifications();
            PacketHandler.sendBodyModificationsToClient(serverplayer);

            CultivatorStats.getCultivatorStats(serverplayer).reset();
            PacketHandler.sendCultivatorStatsToClient(serverplayer);
        }

        if (players.size() == 1)
            commandSourceStack.sendSuccess(Component.translatable( "cultivationcraft.commands.reset.single", players.iterator().next().getDisplayName()), true);
        else
            commandSourceStack.sendSuccess(Component.translatable("cultivationcraft.commands.reset.multiple", players.size()), true);

        return players.size();
    }
}
