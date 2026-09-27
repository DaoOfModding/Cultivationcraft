package DaoOfModding.Cultivationcraft.Common.Alchemy;

import DaoOfModding.Cultivationcraft.Common.Containers.AlchemyQi;
import DaoOfModding.Cultivationcraft.Common.Blocks.Plants.entity.ProceduralPlantBlockEntity;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import java.util.*;

/** Recipe/effect parameters are data, shared by all procedural pill items. */
public record PillDefinition(ResourceLocation id, Effect effect, int qi, int[] targets, int minimumScore,
                             double amount, int duration, int cooldown, int color, List<String> names, int complexity, int tier) {
    public enum Effect { HEAL, HEAL_OVER_TIME, QI, QI_OVER_TIME, CULTIVATION, ABSORPTION, FOOD }
    private static Map<ResourceLocation, PillDefinition> definitions = Map.of();
    public static Collection<PillDefinition> all() { return definitions.values(); }
    public static PillDefinition get(String id) {
        ResourceLocation key = ResourceLocation.tryParse(id);
        return key == null ? null : definitions.get(key);
    }
    public static PillDefinition fromStack(ItemStack stack) {
        if (!stack.hasTag()) return null;
        var tag = stack.getTag();
        PillDefinition recipe = get(tag.getString("Pill"));
        if (recipe == null) return null;
        // Effects remain fixed when crafted; cooldowns follow the current balancing data.
        try {
            Effect effect = Effect.valueOf(tag.getString("Effect"));
            double amount = tag.getDouble("Amount");
            int duration = tag.getInt("Duration"), cooldown = recipe.cooldown();
            int tier = tag.contains("Tier") ? tag.getInt("Tier") : recipe.tier;
            if (tier < 1 || tier > 3 || !Double.isFinite(amount) || amount <= 0 || duration < 0 || cooldown <= 0
                    || ((effect == Effect.HEAL_OVER_TIME || effect == Effect.QI_OVER_TIME || effect == Effect.ABSORPTION) && duration == 0)) return null;
            return new PillDefinition(recipe.id, effect, recipe.qi, recipe.targets, recipe.minimumScore,
                    amount, duration, cooldown, tag.getInt("Color"), recipe.names, recipe.complexity,
                    tier);
        } catch (IllegalArgumentException invalid) { return null; }
    }
    public boolean cultivation() { return effect == Effect.CULTIVATION || effect == Effect.ABSORPTION; }
    public String group() { return group(effect); }
    public static String group(Effect effect) {
        return switch (effect) {
            case HEAL, HEAL_OVER_TIME -> "healing";
            case QI, QI_OVER_TIME -> "qi";
            case CULTIVATION, ABSORPTION -> "cultivation";
            case FOOD -> "food";
        };
    }

    public static class Loader extends SimpleJsonResourceReloadListener {
        public Loader() { super(new Gson(), "alchemy/pills"); }
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
            Map<ResourceLocation, PillDefinition> loaded = new TreeMap<>();
            files.forEach((id, json) -> {
                JsonObject object = json.getAsJsonObject();
                int[] targets = new int[AlchemyQi.ELEMENTS.size()];
                JsonObject scores = object.getAsJsonObject("elements");
                for (int i = 0; i < targets.length; i++) {
                    String path = AlchemyQi.ELEMENTS.get(i).getPath();
                    String element = path.substring(path.lastIndexOf('.') + 1);
                    if (scores.has(element)) targets[i] = scores.get(element).getAsInt();
                    if (targets[i] < 0) throw new JsonParseException("Negative target: " + id);
                }
                List<String> names = new ArrayList<>();
                object.getAsJsonArray("names").forEach(name -> names.add(name.getAsString()));
                int tier = object.has("tier") ? object.get("tier").getAsInt() : 1;
                PillDefinition definition = new PillDefinition(id, Effect.valueOf(object.get("effect").getAsString()),
                        object.get("refinement_qi").getAsInt(), targets, object.get("minimum_score").getAsInt(),
                        object.get("amount").getAsDouble(), object.get("duration_seconds").getAsInt(),
                        object.get("cooldown_seconds").getAsInt(), Integer.parseInt(object.get("color").getAsString(), 16), List.copyOf(names),
                        object.has("complexity") ? object.get("complexity").getAsInt() : tier, tier);
                if (names.isEmpty() || names.stream().anyMatch(String::isBlank) || definition.qi <= 0
                        || tier < 1 || tier > 3 || definition.complexity < 1 || definition.complexity > 4
                        || definition.minimumScore < 0 || definition.amount <= 0 || !Double.isFinite(definition.amount)
                        || definition.duration < 0 || definition.cooldown <= 0
                        || ((definition.effect == Effect.HEAL_OVER_TIME || definition.effect == Effect.QI_OVER_TIME
                             || definition.effect == Effect.ABSORPTION) && definition.duration == 0)
                        || (definition.cultivation() ? definition.minimumScore == 0 : Arrays.stream(targets).sum() == 0))
                    throw new JsonParseException("Invalid pill definition: " + id);
                int requiredScore = definition.cultivation() ? definition.minimumScore : Arrays.stream(targets).sum();
                if (ProceduralPlantBlockEntity.growthToTier(requiredScore) < tier)
                    throw new JsonParseException("Recipe scores are below the required plant tier: " + id);
                loaded.put(id, definition);
            });
            definitions = Collections.unmodifiableMap(loaded);
            Cultivationcraft.LOGGER.info("Loaded {} alchemy pill definitions: {}", loaded.size(), loaded.keySet());
            if (loaded.isEmpty())
                Cultivationcraft.LOGGER.error("No alchemy pills found under data/<namespace>/alchemy/pills; recipe jade slips cannot be used.");
        }
    }
}
