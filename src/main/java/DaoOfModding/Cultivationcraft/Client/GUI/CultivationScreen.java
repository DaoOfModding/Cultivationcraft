package DaoOfModding.Cultivationcraft.Client.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public class CultivationScreen extends Screen
{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 150;

    public CultivationScreen() {
        super(new TranslationTextComponent("Test"));
    }
}
