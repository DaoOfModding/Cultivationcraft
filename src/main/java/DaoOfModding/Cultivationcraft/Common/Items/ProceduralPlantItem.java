package DaoOfModding.Cultivationcraft.Common.Items;

import DaoOfModding.Cultivationcraft.Common.Blocks.Plants.entity.ProceduralPlantBlockEntity;
import DaoOfModding.Cultivationcraft.Common.Blocks.Plants.world.ClientPlantCatalog;
import DaoOfModding.Cultivationcraft.Common.Blocks.Plants.world.PlantGenomes;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ProceduralPlantItem extends BlockItem {
    private static final String TAG_SPIRITUAL_GROWTH = "SpiritualGrowth";
    private static final String TAG_LEGACY_AGE = "PlantAge";

    public ProceduralPlantItem(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public Component getName(ItemStack stack) {
        Component name = PlantGenomes.getDisplayName(null, readSpecies(stack));
        return name != null ? name : super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        int species = readSpecies(stack);

        String elementStr = null;
        if (level instanceof ServerLevel server) {
            var g = PlantGenomes.getById(server, species);
            if (g != null) elementStr = g.qiElement().toString();
        } else {
            var e = ClientPlantCatalog.get(species);
            if (e != null) elementStr = e.element;
        }

        if (elementStr != null) {
            var rl = new ResourceLocation(elementStr);
            var elem = Elements.getElement(rl);
            if (elem != null) {
                int elemRGB = (elem.color.getRed() << 16) | (elem.color.getGreen() << 8) | elem.color.getBlue();
                tooltip.add(Component.literal("Element: " + elem.getName()).withStyle(s -> s.withColor(elemRGB)));
            }
        }

        int growth = extractGrowth(stack);
        if (growth >= 0) {
            int dynTier = ProceduralPlantBlockEntity.growthToTier(growth);
            String stars = "⭐".repeat(dynTier);
            tooltip.add(Component.literal("Tier: " + dynTier + "  " + stars).withStyle(s -> s.withColor(0xFFD700)));
            tooltip.add(Component.literal("Growth: " + growth).withStyle(s -> s.withColor(0x7FC8FF)));
        }

        boolean hostFlag = hasHostFlag(stack);
        if (hostFlag) tooltip.add(Component.literal("Hosts Qi Source").withStyle(s -> s.withColor(0x00FFFF)));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        int species = readSpecies(stack);
        boolean host = hasHostFlag(stack);
        int dynTier = 0;
        int growth = extractGrowth(stack);
        if (growth >= 0) {
            dynTier = ProceduralPlantBlockEntity.growthToTier(growth);
        }
        return Optional.of(new DaoOfModding.Cultivationcraft.Client.Tooltip.PlantBadgeTooltipData(species, host, dynTier));
    }

    public static int readSpecies(ItemStack stack) {
        int species = 0;
        if (stack.hasTag() && stack.getTag().contains("BlockStateTag")) {
            var bst = stack.getTag().getCompound("BlockStateTag");
            if (bst.contains("species")) {
                try { species = Integer.parseInt(bst.getString("species")); } catch (NumberFormatException ignored) {}
            }
        }
        return species;
    }

    private boolean hasHostFlag(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("QiHostData")) {
            return true;
        }
        if (stack.hasTag() && stack.getTag().contains("BlockStateTag")) {
            var bst = stack.getTag().getCompound("BlockStateTag");
            return "true".equalsIgnoreCase(bst.getString("host_qi"));
        }
        return false;
    }

    private int extractGrowth(ItemStack stack) {
        if (!stack.hasTag()) {
            return -1;
        }
        var tag = stack.getTag();
        if (tag.contains(TAG_SPIRITUAL_GROWTH)) {
            return tag.getInt(TAG_SPIRITUAL_GROWTH);
        }
        if (tag.contains(TAG_LEGACY_AGE)) {
            return tag.getInt(TAG_LEGACY_AGE);
        }
        return -1;
    }
}
