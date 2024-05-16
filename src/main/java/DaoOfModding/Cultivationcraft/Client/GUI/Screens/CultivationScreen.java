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
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CultivationScreen extends GenericTabScreen
{
    protected GUIButton stats;
    protected GUIButton breakthroughButton;
    protected GUIButton reset;

    protected int statsXPos = 20;
    protected int statsYPos = 120;
    protected int breakXPos = 60;
    protected int breakYPos = 120;

    protected int nameYPos = 20;

    protected int breakthroughXPos = 20;
    protected int breakthroughYPos = 50;

    protected TextField breakthrough = new TextField();

    public CultivationScreen()
    {
        super(0, Component.translatable("cultivationcraft.gui.cultivation"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/cultivate.png"));

        stats = new GUIButton("STATS", Component.translatable("cultivationcraft.gui.stats").getString());
        breakthroughButton = new GUIButton("BREAKTHROUGH", Component.translatable("cultivationcraft.gui.breakthrough").getString());
        reset = new GUIButton("RESET", Component.translatable("cultivationcraft.gui.reset").getString());

        breakthrough.setSize(120, 60);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        CultivationType cultivation = CultivatorStats.getCultivatorStats(minecraft.player).getCultivation();

        if (cultivation.getScreen().mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        if (stats.mouseClick((int)mouseX, (int)mouseY, buttonPressed))
        {
            Minecraft.getInstance().forceSetScreen(new CultivationModifyScreen(cultivation, 0));
            return true;
        }

        if (cultivation.canBreakthrough(Minecraft.getInstance().player) && breakthroughButton.mouseClick((int)mouseX, (int)mouseY, buttonPressed))
        {
            if (cultivation.clientPreBreakthrough(minecraft.player))
                ClientPacketHandler.sendBreakthroughToServer(false);

            return true;
        }

        if (reset.mouseClick((int)mouseX, (int)mouseY, buttonPressed))
        {
            ClientPacketHandler.sendBreakthroughToServer(true);
            reset.unselect();
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

        String name = cultivation.getName() + ": " + Component.translatable("cultivationcraft.gui.stage").getString() + " " + cultivation.getStage() + "/" + cultivation.getMaxStage();

        font.draw(PoseStack, name, edgeSpacingX + this.xSize/2 - font.width(name)/2, edgeSpacingY + nameYPos, 0);

        font.draw(PoseStack, Component.translatable("cultivationcraft.gui.breakthroughrequirements").getString(), edgeSpacingX + breakthroughXPos, edgeSpacingY + breakthroughYPos - 10, 0);
        breakthrough.setPos(edgeSpacingX + breakthroughXPos, edgeSpacingY + breakthroughYPos);

        breakthrough.setText(cultivation.breakthroughProgress(Minecraft.getInstance().player));

        if (cultivation.canBreakthrough(Minecraft.getInstance().player))
        {
            if (cultivation.hasTribulation(Minecraft.getInstance().player))
            {
                breakthroughButton.setText(Component.translatable("cultivationcraft.gui.tribulation").getString());

                if (cultivation.isTribulating())
                {
                    breakthroughButton.disable();
                    breakthroughButton.unselect();
                }
                else
                    breakthroughButton.enable();
            }
            else
            {
                breakthroughButton.setText(Component.translatable("cultivationcraft.gui.breakthrough").getString());
                breakthrough.setText(breakthrough.getText() + "\n \n" + Component.translatable("cultivationcraft.gui.breakthroughyes").getString());

                breakthroughButton.enable();
            }

            breakthroughButton.setPos(edgeSpacingX + breakXPos, edgeSpacingY + breakYPos);
            breakthroughButton.render(PoseStack, mouseX, mouseY, this);
        }

        breakthrough.render(this, font, PoseStack, mouseX, mouseY);

        cultivation.getScreen().renderButtons(PoseStack, edgeSpacingX, edgeSpacingY, mouseX, mouseY, this);
        cultivation.getScreen().render(PoseStack, partialTicks, edgeSpacingX + 159, edgeSpacingY + 32, this, cultivation, font);

        stats.setPos(edgeSpacingX + statsXPos, edgeSpacingY + statsYPos);
        stats.render(PoseStack, mouseX, mouseY, this);

        reset.setPos(edgeSpacingX + statsXPos, edgeSpacingY + statsYPos + 20);
        reset.render(PoseStack, mouseX, mouseY, this);
    }
}
