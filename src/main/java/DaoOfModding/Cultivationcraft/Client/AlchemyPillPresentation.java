package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Common.Alchemy.PillEffects;
import DaoOfModding.Cultivationcraft.Common.Items.AlchemyPillItem;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = Cultivationcraft.MODID, value = Dist.CLIENT)
public final class AlchemyPillPresentation {
    public static boolean renderCooldown(net.minecraft.client.gui.Font font, ItemStack stack, int x, int y, float blitOffset) {
        Minecraft mc = Minecraft.getInstance();
        float fraction = PillEffects.cooldownFraction(mc.player, stack, mc.getFrameTime());
        if (fraction <= 0) return false;
        // Vanilla's white cooldown sweep, evaluated per stack's effect family.
        // ItemCooldowns is keyed by Item and would incorrectly lock every pill family.
        com.mojang.blaze3d.systems.RenderSystem.disableDepthTest();
        net.minecraft.client.gui.GuiComponent.fill(new com.mojang.blaze3d.vertex.PoseStack(), x,
                y + net.minecraft.util.Mth.floor(16 * (1 - fraction)), x + 16, y + 16, 0x7FFFFFFF);
        return true;
    }

    public static Component name(ItemStack stack) {
        return PillEffects.identified(Minecraft.getInstance().player, stack)
                ? Component.literal(stack.getTag().getString("PillName")) : Component.literal("???");
    }

    public static int color(ItemStack stack, int layer) {
        var tag = stack.getTag();
        if (tag != null && tag.getBoolean(AlchemyPillItem.BLIND_PREVIEW_TAG)) return 0xFF000000;
        // Knowledge hides the name and effects, never the appearance of a real pill.
        return tag != null && tag.contains("Color") ? 0xFF000000 | tag.getInt("Color") : 0xFFFFFFFF;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof AlchemyPillItem)) return;
        var lines = event.getToolTip();
        lines.clear(); // Includes advanced tooltips: no recipe/effect leaks before identification.
        lines.add(name(stack));
        if (!PillEffects.identified(Minecraft.getInstance().player, stack)) return;
        var tag = stack.getTag();
        DecimalFormat format = new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.ROOT));
        String effect = tag.getString("Effect").toLowerCase(Locale.ROOT);
        double amount = tag.getDouble("Amount");
        int duration = tag.getInt("Duration");
        boolean restoration = effect.equals("heal") || effect.equals("heal_over_time") || effect.equals("qi") || effect.equals("qi_over_time");
        double multiplier = restoration ? DaoOfModding.Cultivationcraft.Common.Alchemy.PillPotency.restorationMultiplier(
                Minecraft.getInstance().player, tag.getInt("Tier")) : 1;
        if (restoration) amount *= 100 * multiplier;
        lines.add(Component.translatable("cultivationcraft.pill.tier_purity", tag.getInt("Tier"), tag.getInt("Purity")).withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("cultivationcraft.pill.effect." + effect, format.format(amount), duration));
        if (restoration && multiplier < 1)
            lines.add(Component.translatable("cultivationcraft.pill.realm_potency", format.format(multiplier * 100)).withStyle(ChatFormatting.GRAY));
        ResourceLocation affinity = ResourceLocation.tryParse(tag.getString("Affinity"));
        if (affinity != null && (effect.equals("cultivation") || effect.equals("absorption"))) {
            lines.add(Component.translatable("cultivationcraft.pill.affinity", Component.translatable(affinity.getPath())));
            lines.add(Component.translatable("cultivationcraft.pill.realm_only",
                    DaoOfModding.Cultivationcraft.Common.Alchemy.PillPotency.realmName(tag.getInt("Tier"))).withStyle(ChatFormatting.GRAY));
        }
        if (effect.equals("food")) lines.add(Component.translatable("cultivationcraft.pill.body_only").withStyle(ChatFormatting.GRAY));
        if (effect.equals("qi") || effect.equals("qi_over_time"))
            lines.add(Component.translatable("cultivationcraft.pill.external_only").withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("cultivationcraft.pill.shared_cooldown", tag.getInt("Cooldown")).withStyle(ChatFormatting.GRAY));
    }
}
