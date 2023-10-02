package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Client.GUI.GUIButton;
import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Cultivation.CultivationType;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.TechniqueStatModification;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

public class TechniqueModifyScreen extends GenericTabScreen
{
    protected final int inspirationXPos = 30;
    protected final int inspirationYPos = 20;

    protected final int statXPos = 20;
    protected final int statYPos = 40;
    protected final int statYSpacing = 10;

    protected final int statXPosFromRight = 80;
    protected final int buttonXPosFromRight = 20;

    protected Technique selectedTech;

    protected ArrayList<ResourceLocation> stats = new ArrayList<>();
    protected GUIButton[] buttons;

    public TechniqueModifyScreen(Technique tech, int activeTab)
    {
        super(activeTab, Component.translatable("cultivationcraft.gui.technique"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/stats.png"));

        selectedTech = tech;

        updateStats();
    }

    protected void updateStats()
    {
        stats = new ArrayList<>();

        for (ResourceLocation stat : selectedTech.getTechniqueStats())
            stats.add(stat);

        int buttonNumber = stats.size();

        buttons = new GUIButton[buttonNumber];

        for (int i = 0; i < buttonNumber; i++)
            buttons[i] = new GUIButton("TechStat" + i," ");

        ResourceLocation selected = CultivatorStats.getCultivatorStats(Minecraft.getInstance().player).getTechniqueFocus(selectedTech.getClass());
        buttons[resourceLocationToNumber(selected)].select();
    }

    protected int resourceLocationToNumber(ResourceLocation location)
    {
        if (location == null)
        {
            CultivatorStats.getCultivatorStats(Minecraft.getInstance().player).setTechniqueFocus(selectedTech.getClass().toString(), stats.get(0));
            return 0;
        }

        int number = 0;

        int i = 0;
        for (ResourceLocation check : stats)
        {
            if (check.compareTo(location) == 0)
                return i;

            i++;
        }

        return number;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonPressed)
    {
        if (super.mouseClicked(mouseX, mouseY, buttonPressed))
            return true;

        for (int i = 0; i < buttons.length; i++)
        {
            if (buttons[i].mouseClick((int) mouseX, (int) mouseY, buttonPressed))
            {
                for (int j = 0; j < buttons.length; j++)
                    buttons[j].unselect();

                buttons[i].select();

                ClientPacketHandler.sendTechStatToServer(selectedTech.getClass(), stats.get(i));

                return true;
            }
        }

        return false;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks)
    {
        super.render(PoseStack, mouseX, mouseY, partialTicks);

        drawGuiForgroundLayer(PoseStack, partialTicks, mouseX, mouseY);
    }

    protected ResourceLocation updateFocus()
    {
        ResourceLocation focus = selectedTech.getFirstStatChange();
        CultivatorStats.getCultivatorStats(Minecraft.getInstance().player).setTechniqueFocus(selectedTech.getClass().toString(), focus);

        updateStats();

        return focus;
    }

    protected TechniqueStatModification getStatModification()
    {
        ResourceLocation focus = CultivatorStats.getCultivatorStats(Minecraft.getInstance().player).getTechniqueFocus(selectedTech.getClass());

        if (focus == null)
            focus = updateFocus();

        TechniqueStatModification modifications = selectedTech.getStatChangesPerLevel(focus);

        if (modifications == null)
            modifications = selectedTech.getStatChangesPerLevel(updateFocus());

        return modifications;
    }

    protected void drawGuiForgroundLayer(PoseStack PoseStack, float partialTicks, int mouseX, int mouseY)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        CultivationType cultivation = CultivatorStats.getCultivatorStats(Minecraft.getInstance().player).getCultivation();

        int maxInspiration = cultivation.getMaxTechLevel();
        int currentInspiration = cultivation.getTechLevelProgress(selectedTech.getClass());

        String inspiration = currentInspiration + "/" + maxInspiration;

        font.draw(PoseStack, inspiration, edgeSpacingX + this.xSize - inspirationXPos - font.width(inspiration), edgeSpacingY + inspirationYPos, Color.white.getRGB());

        drawStats(PoseStack, mouseX, mouseY);
    }

    protected void drawStats(PoseStack PoseStack, int mouseX, int mouseY)
    {
        TechniqueStatModification modifications = getStatModification();

        for (int stat = 0; stat < stats.size(); stat++)
        {
            double statValue = selectedTech.getTechniqueStat(stats.get(stat), Minecraft.getInstance().player);
            drawStat(PoseStack, stat, stats.get(stat), statValue);

            double value = modifications.getStatChange(stats.get(stat));
            drawStatChanges(PoseStack, stat, stats.get(stat), value);

            // Only draw button if this stat can level
            if (selectedTech.getStatChangesPerLevel(stats.get(stat)) != null)
                drawButton(PoseStack, stat, mouseX, mouseY);
        }
    }

    protected void drawStat(PoseStack PoseStack, int pos, ResourceLocation stat, double statValue)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        font.draw(PoseStack, Component.translatable(stat.getPath()).getString(), edgeSpacingX + statXPos, edgeSpacingY + statYPos + statYSpacing*pos, Color.black.getRGB());
        font.draw(PoseStack, "" + statValue, edgeSpacingX + this.xSize - statXPosFromRight - font.width("" + statValue), edgeSpacingY + statYPos + statYSpacing*pos, Color.WHITE.getRGB());
    }

    protected void drawButton(PoseStack PoseStack, int pos, int mouseX, int mouseY)
    {
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        buttons[pos].setPos(edgeSpacingX + this.xSize - buttonXPosFromRight - buttons[pos].width, edgeSpacingY + statYPos + statYSpacing*pos - 2);
        buttons[pos].render(PoseStack, mouseX, mouseY, this);
    }

    protected void drawStatChanges(PoseStack PoseStack, int pos, ResourceLocation stat, double value)
    {
        if (value != 0)
        {
            int edgeSpacingX = (this.width - this.xSize) / 2;
            int edgeSpacingY = (this.height - this.ySize) / 2;

            String amount = " (";
            Color col = Color.red;

            if (value > 0)
                amount += "+";

            boolean reversed = DefaultTechniqueStatIDs.isReversedNegative(stat);

            if ((!reversed && value > 0) || (reversed && value < 0))
                col = Color.GREEN;


            amount += value + ")";

            font.draw(PoseStack, amount, edgeSpacingX + this.xSize - statXPosFromRight, edgeSpacingY + statYPos + statYSpacing*pos, col.getRGB());
        }
    }
}
