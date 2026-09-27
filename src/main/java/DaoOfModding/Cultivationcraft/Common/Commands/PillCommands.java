package DaoOfModding.Cultivationcraft.Common.Commands;

import DaoOfModding.Cultivationcraft.Common.Alchemy.PillCatalog;
import DaoOfModding.Cultivationcraft.Common.Alchemy.PillDefinition;
import DaoOfModding.Cultivationcraft.Common.Alchemy.PillStacks;
import DaoOfModding.Cultivationcraft.Common.Containers.AlchemyQi;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = Cultivationcraft.MODID)
public final class PillCommands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        var command = Commands.literal("givepill").executes(ctx -> help(ctx.getSource()))
                .then(Commands.argument("player", EntityArgument.player())
                    .then(Commands.argument("family", StringArgumentType.word())
                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(
                                PillDefinition.all().stream().map(PillDefinition::group).distinct(), builder))
                        .then(Commands.argument("id", ResourceLocationArgument.id()).suggests((ctx, builder) -> {
                            String family = StringArgumentType.getString(ctx, "family");
                            String remaining = builder.getRemainingLowerCase();
                            var level = ctx.getSource().getLevel();
                            for (var pill : PillDefinition.all()) {
                                if (!pill.group().equals(family)) continue;
                                String name = PillCatalog.get(level).name(pill, level);
                                if (pill.id().toString().contains(remaining) || name.toLowerCase(Locale.ROOT).contains(remaining))
                                    builder.suggest(pill.id().toString(), Component.literal("T" + pill.tier() + " | " + name));
                            }
                            return builder.buildFuture();
                        }).then(number("purity", 0, 100, "Purity percent (0-100)", 25, 50, 75, 95, 100)
                            .then(number("quantity", 1, 1024, "Total pills; stacks hold 16", 1, 4, 8, 16, 32, 64)
                                .executes(ctx -> give(ctx, Elements.woodElement))
                                .then(Commands.argument("affinity", ResourceLocationArgument.id())
                                    .suggests((ctx, builder) -> SharedSuggestionProvider.suggestResource(
                                            AlchemyQi.ELEMENTS.stream().filter(element -> !element.equals(Elements.noElement)), builder))
                                    .executes(ctx -> give(ctx, ResourceLocationArgument.getId(ctx, "affinity")))))))));
        event.getDispatcher().register(Commands.literal("cultivation").requires(src -> src.hasPermission(2)).then(command));
    }

    private static RequiredArgumentBuilder<CommandSourceStack, Integer> number(String name, int min, int max, String hint, int... values) {
        return Commands.argument(name, IntegerArgumentType.integer(min, max)).suggests((ctx, builder) -> {
            for (int value : values) if (Integer.toString(value).startsWith(builder.getRemaining()))
                builder.suggest(value, Component.literal(hint));
            return builder.buildFuture();
        });
    }

    public static int help(CommandSourceStack source) {
        source.sendSuccess(Component.literal("/cultivation givepill <player> <family> <id> <purity> <quantity> [affinity]"), false);
        source.sendSuccess(Component.literal("Tab suggests families and matching pill IDs/names. Purity: 0-100; quantity: 1-1024. "
                + "Cultivation pills default to Wood; optional affinity selects another element. Pills remain unidentified."), false);
        return 1;
    }

    private static int give(CommandContext<CommandSourceStack> ctx, ResourceLocation affinity) throws CommandSyntaxException {
        var source = ctx.getSource();
        var id = ResourceLocationArgument.getId(ctx, "id");
        PillDefinition definition = PillDefinition.get(id.toString());
        if (definition == null && id.getNamespace().equals("minecraft"))
            definition = PillDefinition.get(new ResourceLocation(Cultivationcraft.MODID, id.getPath()).toString());
        if (definition == null || !definition.group().equals(StringArgumentType.getString(ctx, "family"))) {
            source.sendFailure(Component.literal("Unknown pill or wrong effect family. Press Tab on id for matching pills."));
            return 0;
        }
        if (!AlchemyQi.ELEMENTS.contains(affinity) || affinity.equals(Elements.noElement)) {
            source.sendFailure(Component.literal("Choose a non-neutral affinity using Tab."));
            return 0;
        }
        var player = EntityArgument.getPlayer(ctx, "player");
        int purity = IntegerArgumentType.getInteger(ctx, "purity"), quantity = IntegerArgumentType.getInteger(ctx, "quantity");
        ItemStack template = PillStacks.create(player.getLevel(), definition, purity, affinity);
        for (int remaining = quantity; remaining > 0;) {
            ItemStack stack = template.copy();
            int count = Math.min(remaining, stack.getMaxStackSize());
            stack.setCount(count);
            player.getInventory().add(stack);
            if (!stack.isEmpty()) {
                var drop = player.drop(stack, false);
                if (drop != null) { drop.setNoPickUpDelay(); drop.setOwner(player.getUUID()); }
            }
            remaining -= count;
        }
        player.containerMenu.broadcastChanges();
        source.sendSuccess(Component.literal("Gave " + quantity + "x " + template.getTag().getString("PillName")
                + " (" + purity + "% purity) to " + player.getGameProfile().getName()), true);
        return quantity;
    }
}
