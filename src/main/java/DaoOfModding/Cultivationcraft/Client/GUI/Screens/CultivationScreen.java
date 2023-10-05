package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.GUIButton;
import DaoOfModding.Cultivationcraft.Client.GUI.TechniqueIcons;
import DaoOfModding.Cultivationcraft.Client.GUI.TextField;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CultivationScreen extends GenericTabScreen
{
    protected GUIButton stats;

    protected int statsXPos = 20;
    protected int statsYPos = 100;

    protected int breakthroughXPos = 20;
    protected int breakthroughYPos = 30;

    protected TextField breakthrough = new TextField();

    public CultivationScreen()
    {
        super(0, Component.translatable("cultivationcraft.gui.cultivation"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/cultivate.png"));

        stats = new GUIButton("STATS", Component.translatable("cultivationcraft.gui.stats").getString());

        breakthrough.setSize(120, 60);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        CultivationType cultivation = CultivatorStats.getCultivatorStats(minecraft.player).getCultivation();

        if (stats.mouseClick((int)mouseX, (int)mouseY, buttonPressed))
        {
            Minecraft.getInstance().forceSetScreen(new CultivationModifyScreen(cultivation, 0));
            return true;
        }

        return false;
    }


        @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        drawGuiForgroundLayer(PoseStack, partialTicks, mouseX, mouseY);
    }

    protected void drawGuiForgroundLayer(PoseStack PoseStack, float partialTicks, int mouseX, int mouseY)
    {
        CultivationType cultivation = CultivatorStats.getCultivatorStats(minecraft.player).getCultivation();

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        font.draw(PoseStack, Component.translatable("cultivationcraft.gui.breakthrough").getString(), edgeSpacingX + breakthroughXPos, edgeSpacingY + breakthroughYPos - 10, 0);
        breakthrough.setPos(edgeSpacingX + breakthroughXPos, edgeSpacingY + breakthroughYPos);

        if (cultivation.canBreakthrough(Minecraft.getInstance().player))
            breakthrough.setText(Component.translatable("cultivationcraft.gui.breakthroughyes").getString());
        else
            breakthrough.setText(cultivation.breakthroughProgress(Minecraft.getInstance().player));

        breakthrough.render(this, font, PoseStack, mouseX, mouseY);

        stats.setPos(edgeSpacingX + statsXPos, edgeSpacingY + statsYPos);
        stats.render(PoseStack, mouseX, mouseY, this);
    }
}