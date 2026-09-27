package DaoOfModding.Cultivationcraft.Common.Refinement;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Items.ItemRegister;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.WeakHashMap;

/** Consumable refinement: one stone per five-second cycle, with no progress tags on stacks. */
public final class SpiritStoneRefinement implements RefinementHandler {
    private static final long DURATION = 5_000_000_000L;
    private final Map<Player, Long> timers = new WeakHashMap<>();

    @Override public boolean accepts(ItemStack stack) { return stack.is(ItemRegister.SPIRIT_STONE.get()); }
    @Override public boolean flowsTowardPlayer() { return true; }

    @Override
    public boolean isActive(Player player, ItemStack stack) {
        if (stack.isEmpty() || !(player.getFoodData() instanceof QiFoodStats food)) return false;
        var stats = CultivatorStats.getCultivatorStats(player);
        if (stats.getCultivationType() != CultivationTypes.QI_CONDENSER) return false;
        // Qi restoration remains available even when this stage's cultivation is full.
        if (food.getTrueFoodLevel() < food.getMaxFood()) return true;
        var cultivation = stats.getCultivation();
        int progress = cultivation.statsCanLevel()
                ? cultivation.getTechLevelProgressWithoutPrevious(cultivation.getPassive().getClass().toString())
                : cultivation.getQiLevelProgress();
        // Do not consume stones when neither reward can be used.
        return progress < cultivation.getMaxTechLevelWithoutPrevious();
    }

    @Override public float progress(Player player, ItemStack stack) { return timers.getOrDefault(player, 0L) / (float) DURATION; }
    @Override public float remainingSeconds(Player player, ItemStack stack) { return 5 * (1 - progress(player, stack)); }
    @Override public void onDeselected(ServerPlayer player) { timers.remove(player); }

    @Override
    public void tick(ServerPlayer player, ItemStack stack, long elapsedNanos) {
        if (!isActive(player, stack)) { timers.remove(player); return; }
        long time = timers.getOrDefault(player, 0L) + Math.max(0, Math.min(elapsedNanos, 250_000_000L));
        if (time < DURATION) { timers.put(player, time); return; }
        timers.remove(player);
        // Generic Qi accepts the cultivator's affinity without changing their element focus.
        CultivatorStats.getCultivatorStats(player).getCultivation().progressCultivation(player, 5, Elements.anyElement);
        QiFoodStats food = (QiFoodStats) player.getFoodData();
        food.setFoodLevel(Math.min(food.getMaxFood(), food.getTrueFoodLevel() + 50));
        stack.shrink(1);
        PacketHandler.sendCultivatorStatsToClient(player);
        player.containerMenu.broadcastChanges();
    }
}
