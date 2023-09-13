package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

public class TechniqueModifyScreen extends GenericTabScreen
{
    protected final int statXPos = 20;
    protected final int statYPos = 30;
    protected final int statYSpacing = 10;

    protected int techniqueNumber;
    protected Technique selectedTech;

    protected ArrayList<ResourceLocation> stats = new ArrayList<>();

    public TechniqueModifyScreen(int tech)
    {
        super(1, Component.translatable("cultivationcraft.gui.technique"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/blank.png"));

        techniqueNumber = tech;

        updateStats();
    }

    protected void updateStats()
    {
        selectedTech = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer()).getTechnique(techniqueNumber);

        stats = new ArrayList<>();

        for (ResourceLocation stat : selectedTech.getTechniqueStats())
            stats.add(stat);
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        drawGuiForgroundLayer(PoseStack, partialTicks, mouseX, mouseY);
    }

    protected void drawGuiForgroundLayer(PoseStack PoseStack, float partialTicks, int mouseX, int mouseY)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        for (int stat = 0; stat < stats.size(); stat++)
        {
            font.draw(PoseStack, Component.translatable(stats.get(stat).getPath()).getString(), edgeSpacingX + statXPos, edgeSpacingY + statYPos + statYSpacing*stat, Color.black.getRGB());
        }
    }
}
