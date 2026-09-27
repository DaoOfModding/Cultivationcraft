package DaoOfModding.Cultivationcraft.Common.Knowledge;

import DaoOfModding.Cultivationcraft.Common.Alchemy.*;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Containers.AlchemyQi;
import DaoOfModding.Cultivationcraft.Common.Items.ItemRegister;
import DaoOfModding.Cultivationcraft.Common.Items.JadeSlipItem;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.Network.Packets.KnowledgePagesPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Cultivationcraft.MODID)
public final class PlayerKnowledge {
    public static final String PURITY_KNOWLEDGE = "cultivationcraft:alchemy/alchemy_toxicity";
    public static CompoundTag data(Player player) { return CultivatorStats.getCultivatorStats(player).getKnowledgeData(); }
    public static boolean knows(Player player, String id) {
        if (player == null) return false;
        KnowledgeEntry entry = KnowledgeEntry.get(id);
        CompoundTag data = data(player);
        return data.getBoolean("Known:" + id) || (entry != null && (entry.unlockedByDefault()
                || data.getBoolean("Known:" + entry.id())
                || entry.aliases().stream().anyMatch(alias -> data.getBoolean("Known:" + alias))));
    }
    public static KnowledgeEntry entry(ItemStack stack) {
        return stack.getItem() instanceof JadeSlipItem && stack.hasTag() ? KnowledgeEntry.get(stack.getTag().getString("Knowledge")) : null;
    }
    public static boolean canRefine(Player player, ItemStack stack) {
        KnowledgeEntry entry = entry(stack);
        return entry != null && entry.hasSlip() && entry.available() && !knows(player, entry.id().toString());
    }
    public static boolean knowsRecipe(ServerPlayer player, PillDefinition pill) {
        return PillEffects.data(player).getBoolean("Recipe:" + PillCatalog.get(player.getLevel()).key(pill));
    }
    public static ItemStack create(ServerLevel level, KnowledgeEntry entry, RandomSource random) {
        ItemStack stack = new ItemStack(ItemRegister.JADE_SLIP.get());
        stack.getOrCreateTag().putString("Knowledge", entry.id().toString());
        stack.getOrCreateTag().putString("SlipTitle", entry.resolveTitle(entry.titles().get(random.nextInt(entry.titles().size())), level));
        return stack;
    }
    public static int progress(Player player, ItemStack stack) {
        if (!stack.hasTag()) return 0;
        String id = stack.getTag().getString("Knowledge");
        if (knows(player, id)) return 1000;
        return id.equals(data(player).getString("Refining")) ? (int) (data(player).getLong("RefineTime") / 5_000_000L) : 0;
    }
    public static void refine(ServerPlayer player, ItemStack stack, long elapsedNanos) {
        CompoundTag data = data(player);
        if (!(stack.getItem() instanceof JadeSlipItem)) {
            data.remove("Refining"); data.remove("RefineTime"); data.remove("Notice");
            return;
        }
        KnowledgeEntry entry = entry(stack);
        String id = stack.hasTag() ? stack.getTag().getString("Knowledge") : "";
        if (entry == null || !entry.hasSlip() || !entry.available() || knows(player, id)) {
            if (!data.getString("Notice").equals(id + ":blocked")) {
                player.displayClientMessage(Component.translatable(knows(player, id) ? "cultivationcraft.jade.known" : "cultivationcraft.jade.invalid"), true);
                data.putString("Notice", id + ":blocked");
            }
            return;
        }
        if (!id.equals(data.getString("Refining"))) {
            data.putString("Refining", id);
            data.putLong("RefineTime", 0);
        }
        long progress = Math.min(5_000_000_000L, data.getLong("RefineTime") + Math.max(0, Math.min(elapsedNanos, 250_000_000L)));
        data.putLong("RefineTime", progress);
        if (progress < 5_000_000_000L) return;
        data.putBoolean("Known:" + entry.id(), true);
        data.putString("Notice", id + ":blocked");
        stack.shrink(1);
        sync(player);
        player.displayClientMessage(Component.translatable("cultivationcraft.jade.learned", entry.resolveTitle(entry.title(), player.getLevel())), false);
    }

    public static void sync(ServerPlayer player) {
        var pages = new ArrayList<KnowledgePagesPacket.Page>();
        for (KnowledgeEntry entry : KnowledgeEntry.all()) {
            if (!knows(player, entry.id().toString()) || !entry.available()) continue;
            String body = entry.text();
            PillDefinition pill = PillDefinition.get(entry.recipe());
            if (pill != null) {
                PillEffects.data(player).putBoolean("Recipe:" + PillCatalog.get(player.getLevel()).key(pill), true);
                body += recipeText(pill);
            }
            pages.add(new KnowledgePagesPacket.Page(entry.id().toString(), entry.resolveTitle(entry.title(), player.getLevel()), body, category(entry, pill),
                    entry.translationKey(), entry.operatorOnly(), entry.cultivationType(), entry.aliases()));
        }
        PacketHandler.sendCultivatorStatsToClient(player);
        PacketHandler.channel.send(PacketDistributor.PLAYER.with(() -> player), new KnowledgePagesPacket(pages));
    }

    private static java.util.List<String> category(KnowledgeEntry entry, PillDefinition pill) {
        if (pill == null) return entry.category();
        String family = switch (pill.group()) {
            case "healing" -> "Healing";
            case "qi" -> "Qi Restoration";
            case "cultivation" -> "Cultivation";
            case "food" -> "Sustenance";
            default -> "Other Pills";
        };
        return java.util.List.of("Recipe", family);
    }

    private static String recipeText(PillDefinition pill) {
        StringBuilder text = new StringBuilder("\n\n\nPill tier: ").append(pill.tier())
                .append("\nRecipe difficulty: ").append(pill.complexity())
                .append("\nRequired plant: at least one T").append(pill.tier()).append(" or higher")
                .append("\n\n\nRequired scores:");
        if (pill.cultivation()) text.append("\nNon-neutral total: ").append(pill.minimumScore());
        for (int i = 0; i < pill.targets().length; i++) {
            if (pill.targets()[i] <= 0) continue;
            String element = AlchemyQi.ELEMENTS.get(i).getPath();
            text.append("\n").append(element.substring(element.lastIndexOf('.') + 1)).append(": ").append(pill.targets()[i]);
        }
        text.append("\n\n\nQi to channel: ").append(pill.qi());
        return text.toString();
    }

    @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) sync(player);
    }
    @SubscribeEvent public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            data(player).remove("Refining"); data(player).remove("RefineTime"); data(player).remove("Notice");
            sync(player);
        }
    }
    @SubscribeEvent public static void reload(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) for (ServerPlayer player : event.getPlayerList().getPlayers()) sync(player);
    }
}
