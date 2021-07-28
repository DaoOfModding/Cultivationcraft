package DaoOfModding.Cultivationcraft.Client.GUI;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStatsCapability;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.ClientPacketHandler;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

public class ScreenTabControl
{
    private static final int[] TAB_BAR_X_POS = {0, 43, 86, 129};
    private static final int[] TAB_BAR_Y_POS = {0, 0, 0, 0};

    private static final int TAB_BAR_X_SIZE = 41;
    private static final int TAB_BAR_Y_SIZE = 12;

    private static final int TAB_BAR_U = 214;
    private static final int TAB_BAR_V = 0;

    public static void highlightTabs(MatrixStack matrixStack, int tabSelected, int mouseX, int mouseY, int screenX, int screenY, Screen drawTo)
    {
        mouseX = mouseX - screenX;
        mouseY = mouseY - screenY;

        // Loop through each tab, checking if the mouse is over it and highlighting it if so
        for (int i = 0; i < TAB_BAR_X_POS.length; i++)
            if (tabSelected != i)
                if (mouseOver(mouseX, mouseY, i))
                    drawTo.blit(matrixStack, screenX + TAB_BAR_X_POS[i], screenY + TAB_BAR_Y_POS[i], TAB_BAR_U, TAB_BAR_V, TAB_BAR_X_SIZE, TAB_BAR_Y_SIZE);
    }

    public static boolean mouseClick(int mouseX, int mouseY, int screenX, int screenY, int buttonPressed)
    {
        // If not a left click, ignore
        if (buttonPressed != 0)
            return false;

        mouseX = mouseX - screenX;
        mouseY = mouseY - screenY;

        // Loop through each tab, checking if the mouse is over it and switching to it if so
        for (int i = 0; i < TAB_BAR_X_POS.length; i++)
            if (mouseOver(mouseX, mouseY, i))
            {
                if (i == 1)
                    Minecraft.getInstance().forceSetScreen(new TechniqueScreen());
                else if (i == 2)
                {
                    int cultivationType = CultivatorStats.getCultivatorStats(Minecraft.getInstance().player).getCultivationType();

                    if (cultivationType == CultivationTypes.QI_CONDENSER)
                        ClientPacketHandler.sendKeypressToServer(Register.keyPresses.FLYINGSWORDSCREEN);
                    else if (cultivationType == CultivationTypes.BODY_CULTIVATOR)
                        Minecraft.getInstance().forceSetScreen(new BodyforgeScreen());
                }

                return true;
            }

        return false;
    }

    public static boolean mouseOver(int mouseX, int mouseY, int tab)
    {
        if (mouseX >= TAB_BAR_X_POS[tab] && mouseX <= TAB_BAR_X_POS[tab] + TAB_BAR_X_SIZE &&
            mouseY >= TAB_BAR_Y_POS[tab] && mouseY <= TAB_BAR_Y_POS[tab] + TAB_BAR_Y_SIZE)
            return true;

        return false;
    }
}
