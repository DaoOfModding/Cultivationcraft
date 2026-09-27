package DaoOfModding.Cultivationcraft.Common.Alchemy;

import DaoOfModding.Cultivationcraft.Common.Items.ItemRegister;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

/** Shared creation path keeps crafted and command-given pills stack-compatible. */
public final class PillStacks {
    public static ItemStack create(ServerLevel level, PillDefinition definition, int purity, ResourceLocation affinity) {
        ItemStack result = new ItemStack(ItemRegister.ALCHEMY_PILL.get());
        var tag = result.getOrCreateTag();
        PillCatalog catalog = PillCatalog.get(level);
        tag.putString("Pill", definition.id().toString());
        tag.putString("Entry", catalog.key(definition));
        tag.putString("PillName", catalog.name(definition, level));
        tag.putInt("Tier", definition.tier());
        tag.putInt("Purity", Mth.clamp(purity, 0, 100));
        tag.putInt("Color", definition.color());
        tag.putString("Effect", definition.effect().name());
        tag.putDouble("Amount", definition.amount());
        tag.putInt("Duration", definition.duration());
        tag.putInt("Cooldown", definition.cooldown());
        if (definition.cultivation()) tag.putString("Affinity", (affinity == null ? Elements.woodElement : affinity).toString());
        return result;
    }

    public static void refreshCooldown(ItemStack stack) {
        if (!stack.hasTag()) return;
        PillDefinition definition = PillDefinition.get(stack.getTag().getString("Pill"));
        if (definition != null && stack.getTag().getInt("Cooldown") != definition.cooldown())
            stack.getTag().putInt("Cooldown", definition.cooldown());
    }

    public static void clearLegacyAnalysis(ItemStack stack) {
        if (!stack.hasTag()) return;
        var tag = stack.getTag();
        tag.remove("AnalyzingPlayer");
        tag.remove("AnalysisTime");
        tag.remove("BindPercent");
        tag.remove("BindRemaining");
    }
}
