package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class CultivationModifyScreen extends TechniqueModifyScreen
{
    CultivationType cultivation;

    public CultivationModifyScreen(CultivationType cult, int activeTab)
    {
        super(cult.getPassive(), activeTab);

        cultivation = cult;
    }

    @Override
    protected void drawStats(PoseStack PoseStack, int mouseX, int mouseY)
    {
        TechniqueStatModification modifications = getStatModification();

        stats = new ArrayList<>();
        for (ResourceLocation stat : cultivation.getPassive().getTechniqueStats())
            stats.add(stat);

        for (int stat = 0; stat < stats.size(); stat++)
        {
            ResourceLocation statLocation = stats.get(stat);

            double value = modifications.getStatChange(statLocation);
            drawStatChanges(PoseStack, stat, stats.get(stat), value);

            // Only draw button if this stat can level
            if (selectedTech.getStatChangesPerLevel(statLocation) != null)
                drawButton(PoseStack, stat, mouseX, mouseY);
        }

        drawStats(cultivation, PoseStack, new ArrayList<>(), 0);

        stats = new ArrayList<>();
        for (ResourceLocation stat : cultivation.getPassive().getTechniqueStats())
            stats.add(stat);
    }

    protected void drawStats(CultivationType cultivating, PoseStack PoseStack, ArrayList<ResourceLocation> done, int location)
    {
        stats = new ArrayList<>();

        for (ResourceLocation stat : cultivating.getPassive().getTechniqueStats())
            if (!done.contains(stat))
                stats.add(stat);

        for (int stat = 0; stat < stats.size(); stat++)
        {
            ResourceLocation statLocation = stats.get(stat);
            done.add(statLocation);

            double statValue = cultivating.getPassive().getTechniqueStat(statLocation, Minecraft.getInstance().player);

            // Loop through previous cultivations and add their stat values into existing stats
            CultivationType check = cultivating.getPreviousCultivation();
            while (check != null)
            {
                if (check.getPassive().hasTechniqueStat(statLocation))
                    statValue += check.getPassive().getTechniqueStat(statLocation, Minecraft.getInstance().player);

                check = check.getPreviousCultivation();
            }

            drawStat(PoseStack, stat + location, stats.get(stat), statValue);
        }

        if (cultivating.getPreviousCultivation() != null)
            drawStats(cultivating.getPreviousCultivation(), PoseStack, done, location + stats.size());
    }
}
