package DaoOfModding.Cultivationcraft.Common.Alchemy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DaoOfModding.Cultivationcraft.Common.Blocks.Plants.entity.ProceduralPlantBlockEntity;
import DaoOfModding.Cultivationcraft.Common.Blocks.Plants.world.PlantGenomes;
import DaoOfModding.Cultivationcraft.Common.Blocks.entity.AlchemyCauldronBlockEntity;
import DaoOfModding.Cultivationcraft.Common.Containers.AlchemyQi;
import DaoOfModding.Cultivationcraft.Common.Items.ItemRegister;
import DaoOfModding.Cultivationcraft.Common.Items.ProceduralPlantItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/** One batch consumes all nine input stacks, exactly as counted by the displayed scores. */
public record AlchemyBatch(PillDefinition definition, int[] scores, boolean valid, double impurity, double neutralBonus) {
    public static AlchemyBatch inspect(Container inventory, ServerLevel level) {
        int[] scores = AlchemyQi.totals(inventory, level);
        int total = Arrays.stream(scores).sum();
        int elemental = total - scores[0];
        boolean occupied = false, valid = true;
        int highestPlantTier = 0;
        for (int i = 0; i < AlchemyCauldronBlockEntity.SLOT_COUNT; i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;
            occupied = true;
            int id = ProceduralPlantItem.readSpecies(stack);
            if (!(stack.getItem() instanceof ProceduralPlantItem) || AlchemyQi.contribution(stack) <= 0
                    || PlantGenomes.getById(level, id) == null) { valid = false; continue; }
            highestPlantTier = Math.max(highestPlantTier,
                    ProceduralPlantBlockEntity.growthToTier(AlchemyQi.contribution(stack) / stack.getCount()));
        }
        if (!occupied) return null;
        // A recognisable elemental pair takes precedence, even when under the minimum.
        // Otherwise cultivation is the blind, total-score recipe. This prevents partial
        // healing recipes from silently becoming successful cultivation pills.
        PillDefinition selected = null;
        boolean selectedMeetsScores = false;
        double closest = Double.MAX_VALUE;
        for (PillDefinition candidate : PillDefinition.all()) {
            if (candidate.cultivation()) continue;
            boolean represented = true, meetsScores = true;
            double distance = 0;
            int targetTotal = Arrays.stream(candidate.targets()).sum();
            for (int i = 0; i < scores.length; i++) {
                int target = candidate.targets()[i];
                if (target > 0 && scores[i] == 0) represented = false;
                if (scores[i] < target) meetsScores = false;
                if (i == 0 && target == 0) continue; // Optional neutral stabilizer.
                distance += Math.abs(scores[i] - target) / (double) targetTotal;
            }
            // A complete lower-tier recipe must not lose to an underfilled higher-tier
            // version. Tier eligibility is checked afterwards: stacks cannot fake a tier.
            if (represented && (selected == null || (meetsScores && !selectedMeetsScores)
                    || (meetsScores == selectedMeetsScores && distance < closest))) {
                selected = candidate;
                closest = distance;
                selectedMeetsScores = meetsScores;
            }
        }
        if (selected == null || selected.effect() == PillDefinition.Effect.FOOD) {
            // Highest reached cultivation threshold; below all thresholds, fail the cheapest.
            PillDefinition cultivation = null;
            for (PillDefinition candidate : PillDefinition.all()) {
                if (!candidate.cultivation()) continue;
                if (cultivation == null || (candidate.minimumScore() <= elemental
                        && (cultivation.minimumScore() > elemental || candidate.minimumScore() > cultivation.minimumScore()))
                        || (cultivation.minimumScore() > elemental && candidate.minimumScore() < cultivation.minimumScore()))
                    cultivation = candidate;
            }
            // An optional neutral base must not turn a wood cultivation recipe into
            // Bigu automatically. Prefer whichever of these two formulas fits better.
            if (selected == null) selected = cultivation;
            else if (cultivation != null && elemental >= cultivation.minimumScore()) {
                double neutralAllowance = Math.max(10, cultivation.minimumScore() * .1) * 1.25;
                double distance = (Math.abs(elemental - cultivation.minimumScore())
                        + Math.max(0, scores[0] - neutralAllowance)) / cultivation.minimumScore();
                if (distance < closest) selected = cultivation;
            }
        }
        if (selected == null) return null;
        // Ingredient diversity comes from the recipe's scores, not a global species limit.
        valid &= highestPlantTier >= selected.tier();
        double waste = 0;
        if (selected.cultivation()) {
            valid &= elemental >= selected.minimumScore();
            waste = Math.max(0, elemental - selected.minimumScore() - Math.max(10, selected.minimumScore() * .25));
        } else {
            for (int i = 0; i < scores.length; i++) {
                int target = selected.targets()[i];
                valid &= scores[i] >= target;
                if (i == 0 && target == 0) continue;
                waste += target == 0 ? scores[i] : Math.max(0, scores[i] - target - Math.max(10, target * .25));
            }
        }
        double bonus = 0;
        if (selected.targets()[0] == 0) {
            int required = selected.cultivation() ? selected.minimumScore() : Arrays.stream(selected.targets()).sum();
            double ideal = Math.max(10, required * .1), neutral = scores[0];
            bonus = neutral < ideal * .75 ? neutral / (ideal * .75)
                    : neutral <= ideal * 1.25 ? 1 : Math.max(0, (2 * ideal - neutral) / (.75 * ideal));
            waste += Math.max(0, neutral - ideal * 1.25);
        }
        return new AlchemyBatch(selected, scores, valid, total == 0 ? 1 : Mth.clamp(waste / total, 0, 1), bonus);
    }

    public int purity() { return (int) Math.round(Mth.clamp(95 - 50 * impurity + 5 * neutralBonus, 1, 100)); }

    /** Successful impure batches leave one remnant per complete ten percent lost purity. */
    public int wasteCount() {
        int purity = purity();
        return purity <= 70 ? (100 - purity) / 10 : 0;
    }

    public double failureChance(int cauldronTier) {
        if (!valid) return 100;
        int difficulty = definition.complexity();
        return Mth.clamp(5 + 8 * (difficulty - 1) + 8 * (difficulty - cauldronTier)
                + 6 * impurity - 3 * neutralBonus, 2, 90);
    }

    public ItemStack refine(ServerLevel level, int cauldronTier) {
        if (level.random.nextDouble() * 100 < failureChance(cauldronTier)) return new ItemStack(ItemRegister.ALCHEMY_REMNANTS.get(), 10);
        net.minecraft.resources.ResourceLocation affinity = null;
        if (definition.cultivation()) {
            List<Integer> dominant = new ArrayList<>();
            int highest = 0;
            for (int i = 1; i < scores.length; i++) {
                if (scores[i] > highest) { highest = scores[i]; dominant.clear(); }
                if (scores[i] == highest && highest > 0) dominant.add(i);
            }
            affinity = AlchemyQi.ELEMENTS.get(dominant.get(level.random.nextInt(dominant.size())));
        }
        return PillStacks.create(level, definition, purity(), affinity);
    }
}
