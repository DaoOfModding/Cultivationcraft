package DaoOfModding.Cultivationcraft.Common.Alchemy;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.FoodStats.QiFoodStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cultivationcraft.MODID)
public final class PillEffects {
    public static CompoundTag data(Player player) { return CultivatorStats.getCultivatorStats(player).getPillData(); }
    public static boolean identified(Player player, ItemStack stack) {
        if (player == null || !stack.hasTag()) return false;
        String entry = stack.getTag().getString("Entry");
        return !entry.isEmpty() && (data(player).getBoolean("Known:" + entry) || data(player).getBoolean("Recipe:" + entry));
    }

    /** Uses the shared refinement timing/progress display; never binds, transforms, or consumes the pill. */
    public static void refinePill(Player player, ItemStack stack, long elapsedNanos) {
        var tag = stack.getOrCreateTag();
        if (tag.getString("Entry").isEmpty() || PillDefinition.get(tag.getString("Pill")) == null) return;
        PillStacks.clearLegacyAnalysis(stack);
        CompoundTag progressData = data(player);
        String entry = tag.getString("Entry");
        if (!entry.equals(progressData.getString("AnalyzingEntry"))) {
            progressData.putString("AnalyzingEntry", entry);
            progressData.putLong("AnalysisTime", 0);
        }
        long progress = identified(player, stack) ? 5_000_000_000L
                : Math.min(5_000_000_000L, progressData.getLong("AnalysisTime") + Math.max(0, Math.min(elapsedNanos, 250_000_000L)));
        progressData.putLong("AnalysisTime", progress);
        if (progress == 5_000_000_000L && !identified(player, stack)) {
            progressData.putBoolean("Known:" + entry, true);
            PacketHandler.sendCultivatorStatsToClient(player);
        }
    }

    public static int analysisProgress(Player player, ItemStack stack) {
        if (identified(player, stack)) return 1000;
        return stack.hasTag() && stack.getTag().getString("Entry").equals(data(player).getString("AnalyzingEntry"))
                ? (int) Math.min(1000, data(player).getLong("AnalysisTime") / 5_000_000L) : 0;
    }

    public static float cooldownFraction(Player player, ItemStack stack, float partialTick) {
        if (player == null || !stack.hasTag()) return 0;
        try {
            String group = PillDefinition.group(PillDefinition.Effect.valueOf(stack.getTag().getString("Effect")));
            CompoundTag data = data(player);
            long duration = data.getLong("CooldownLength:" + group);
            if (duration <= 0) duration = (long) stack.getTag().getInt("Cooldown") * 20;
            return duration <= 0 ? 0 : net.minecraft.util.Mth.clamp(
                    (data.getLong("Cooldown:" + group) - now(player) - partialTick) / duration, 0, 1);
        } catch (IllegalArgumentException invalid) { return 0; }
    }

    private static long now(Player player) {
        return player instanceof ServerPlayer server ? server.server.overworld().getGameTime() : player.level.getGameTime();
    }

    public static double absorptionBonus(Player player) {
        CompoundTag data = data(player);
        if (!PillPotency.canCultivate(player, absorptionTier(data))) return 0;
        return player.hasEffect(AlchemyEffects.QI_ABSORPTION.get()) && data.getLong("AbsorptionUntil") > now(player) ? data.getDouble("AbsorptionAmount") : 0;
    }

    private static int absorptionTier(CompoundTag data) {
        return data.contains("AbsorptionTier") ? data.getInt("AbsorptionTier") : 1;
    }

    public static boolean consume(ServerPlayer player, ItemStack stack) {
        if (!stack.hasTag()) return false;
        CompoundTag tag = stack.getTag();
        PillDefinition definition = PillDefinition.fromStack(stack);
        if (definition == null || tag.getString("Entry").isEmpty()) return false;
        var stats = CultivatorStats.getCultivatorStats(player);
        var cultivation = stats.getCultivation();
        CompoundTag data = data(player);
        long time = now(player);
        String cooldown = "Cooldown:" + definition.group();
        if (data.getLong(cooldown) > time) return reject(player, "cooldown", (data.getLong(cooldown) - time + 19) / 20);
        if (definition.cultivation() && !PillPotency.canCultivate(player, definition.tier()))
            return reject(player, "realm_only", PillPotency.realmName(definition.tier()));
        if (definition.effect() == PillDefinition.Effect.FOOD && stats.getCultivationType() != CultivationTypes.BODY_CULTIVATOR)
            return reject(player, "body_only");
        if (definition.group().equals("qi") && (stats.getCultivationType() != CultivationTypes.QI_CONDENSER
                || !(player.getFoodData() instanceof QiFoodStats))) return reject(player, "external_only");
        ResourceLocation affinity = ResourceLocation.tryParse(tag.getString("Affinity"));
        if (definition.cultivation() && (affinity == null || affinity.equals(Elements.noElement) || !cultivation.canCultivate(affinity)))
            return reject(player, "affinity_mismatch");
        double amount = definition.amount();
        if (definition.group().equals("healing") || definition.group().equals("qi"))
            amount *= PillPotency.restorationMultiplier(player, definition.tier());
        // Cooldowns are per effect family and persisted in the player's capability.
        // Purity describes batch quality; potency changes need separate balancing.
        switch (definition.effect()) {
            case HEAL, HEAL_OVER_TIME -> {
                if (player.getHealth() >= player.getMaxHealth()) return reject(player, "full");
                float hp = (float) (amount * player.getMaxHealth());
                if (definition.effect() == PillDefinition.Effect.HEAL) player.heal(hp);
                else schedule(data, "Healing", hp, definition.duration(), time);
            }
            case QI, QI_OVER_TIME -> {
                QiFoodStats food = (QiFoodStats) player.getFoodData();
                if (food.getTrueFoodLevel() >= food.getMaxFood()) return reject(player, "full");
                float qi = (float) (amount * food.getMaxFood());
                if (definition.effect() == PillDefinition.Effect.QI) restoreQi(player, qi);
                else {
                    showStatus(player, AlchemyEffects.QI_RESTORATION.get(), definition.duration() * 20 + 1, 0);
                    schedule(data, "Qi", qi, definition.duration(), time);
                }
            }
            case CULTIVATION -> {
                float supplied = (float) amount;
                float unused = cultivation.progressCultivation(player, supplied, affinity);
                if (unused >= supplied) return reject(player, "full");
            }
            case ABSORPTION -> {
                showStatus(player, AlchemyEffects.QI_ABSORPTION.get(), definition.duration() * 20,
                        Math.max(0, (int) definition.amount() - 1));
                data.putDouble("AbsorptionAmount", definition.amount());
                data.putInt("AbsorptionTier", definition.tier());
                data.putLong("AbsorptionUntil", time + Math.max(1, definition.duration() * 20L));
            }
            case FOOD -> {
                int max = player.getFoodData() instanceof QiFoodStats food ? food.getMaxFood() : 20;
                double current = player.getFoodData() instanceof QiFoodStats food ? food.getTrueFoodLevel() : player.getFoodData().getFoodLevel();
                if (current >= max) return reject(player, "full");
                player.getFoodData().eat((int) definition.amount(), .5f); // Four hunger and four saturation.
            }
        }
        data.putBoolean("StatusEffectsMigrated", true);
        data.putLong(cooldown, time + (long) definition.cooldown() * 20);
        data.putLong("CooldownLength:" + definition.group(), (long) definition.cooldown() * 20);
        if (!player.getAbilities().instabuild) stack.shrink(1);
        PacketHandler.sendCultivatorStatsToClient(player);
        return true;
    }

    private static void showStatus(Player player, MobEffect effect, int ticks, int amplifier) {
        // Replace the timer without a hidden effect that could resume after expiry.
        player.forceAddEffect(new MobEffectInstance(effect, Math.max(1, ticks), amplifier, false, false, true), player);
    }

    private static void reconcileStatus(Player player, CompoundTag data, long time) {
        // Upgrade existing saves once. Afterwards curing a status must cancel its benefit.
        if (!data.getBoolean("StatusEffectsMigrated")) {
            if (data.getLong("QiEnd") > time && data.getInt("QiPulses") > 0) {
                showStatus(player, AlchemyEffects.QI_RESTORATION.get(), (int) (data.getLong("QiEnd") - time + 1), 0);
            }
            if (data.getLong("AbsorptionUntil") > time) {
                long end = data.getLong("AbsorptionUntil");
                double amount = data.getDouble("AbsorptionAmount");
                showStatus(player, AlchemyEffects.QI_ABSORPTION.get(), (int) (end - time), Math.max(0, (int) amount - 1));
            }
            data.putBoolean("StatusEffectsMigrated", true);
        }
        reconcileOne(player, AlchemyEffects.QI_RESTORATION.get(), data.getLong("QiEnd") - time + 1);
        if (!PillPotency.canCultivate(player, absorptionTier(data))
                && player.hasEffect(AlchemyEffects.QI_ABSORPTION.get())) player.removeEffect(AlchemyEffects.QI_ABSORPTION.get());
        reconcileOne(player, AlchemyEffects.QI_ABSORPTION.get(), data.getLong("AbsorptionUntil") - time);
        if (!player.hasEffect(AlchemyEffects.QI_RESTORATION.get())) data.putInt("QiPulses", 0);
        if (!player.hasEffect(AlchemyEffects.QI_ABSORPTION.get())) data.remove("AbsorptionUntil");
    }

    private static void reconcileOne(Player player, MobEffect type, long remaining) {
        MobEffectInstance effect = player.getEffect(type);
        if (effect == null) return;
        if (remaining <= 0) player.removeEffect(type);
        // Vanilla timers pause offline; these pill timers use world time.
        else if (effect.getDuration() > remaining + 1)
            showStatus(player, type, (int) remaining, effect.getAmplifier());
    }

    @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOWEST)
    public static void removed(MobEffectEvent.Remove event) {
        if (event.isCanceled() || !(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getEffect() == AlchemyEffects.QI_RESTORATION.get()) data(player).putInt("QiPulses", 0);
        if (event.getEffect() == AlchemyEffects.QI_ABSORPTION.get()) data(player).remove("AbsorptionUntil");
    }

    private static boolean reject(Player player, String key, Object... args) {
        player.displayClientMessage(Component.translatable("cultivationcraft.pill." + key, args), true);
        return false;
    }

    private static void schedule(CompoundTag data, String effect, float amount, int seconds, long time) {
        data.putFloat(effect + "PerPulse", amount / seconds);
        data.putInt(effect + "Pulses", seconds);
        data.putLong(effect + "Next", time + 20);
        data.putLong(effect + "End", time + seconds * 20L);
    }

    private static void restoreQi(Player player, float amount) {
        if (CultivatorStats.getCultivatorStats(player).getCultivationType() == CultivationTypes.QI_CONDENSER
                && player.getFoodData() instanceof QiFoodStats food)
            food.setFoodLevel(Math.min(food.getMaxFood(), food.getTrueFoodLevel() + amount));
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player) || !player.isAlive()) return;
        CompoundTag data = data(player);
        long time = now(player);
        reconcileStatus(player, data, time);
        for (String effect : new String[]{"Healing", "Qi"}) {
            int pulses = data.getInt(effect + "Pulses");
            if (pulses <= 0 || time < data.getLong(effect + "Next")) continue;
            if (time > data.getLong(effect + "End")) { data.putInt(effect + "Pulses", 0); continue; }
            float amount = data.getFloat(effect + "PerPulse");
            if (effect.equals("Healing")) player.heal(amount);
            else restoreQi(player, amount);
            data.putInt(effect + "Pulses", pulses - 1);
            data.putLong(effect + "Next", time + 20);
        }
    }

    @SubscribeEvent
    public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        // Knowledge and cooldowns survive death; temporary benefits do not.
        if (event.isEndConquered()) return;
        CompoundTag data = data(event.getEntity());
        data.remove("AbsorptionUntil");
        data.remove("HealingPulses");
        data.remove("QiPulses");
        PacketHandler.sendCultivatorStatsToClient(event.getEntity());
    }
}
