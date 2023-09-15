package DaoOfModding.Cultivationcraft.Client.GUI.Screens;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CultivationScreen extends GenericTabScreen
{

    public CultivationScreen()
    {
        super(0, Component.translatable("cultivationcraft.gui.cultivation"), new ResourceLocation(Cultivationcraft.MODID, "textures/gui/cultivate.png"));
    }
}
