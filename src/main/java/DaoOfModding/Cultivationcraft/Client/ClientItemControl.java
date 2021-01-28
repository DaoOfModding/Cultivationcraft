package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartModels;
import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUIs;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUI;
import DaoOfModding.Cultivationcraft.Client.GUI.FlyingSwordContainerScreen;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


public class ClientItemControl
{
    public static IWorld thisWorld;


    public static void init(FMLClientSetupEvent event)
    {
        KeybindingControl.init();

        ScreenManager.registerFactory(Register.ContainerTypeFlyingSword, FlyingSwordContainerScreen::new);

        BodyPartModels.setupModels();
        setupDefaultBodyPartGUIs();
    }

    public static void setupDefaultBodyPartGUIs()
    {
        BodyPartGUIs.addGUI(BodyPartNames.DefaultBody, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultBody + ".png"), 0, 0, 18, 28, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultHead, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultHead + ".png"), 0, -2, 18, 20, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultLeftArm, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftArm + ".png"), 2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultRightArm, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightArm + ".png"), -2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultLeftLeg, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftLeg + ".png"), 1, 2, 8, 30, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultRightLeg, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightLeg + ".png"), -1, 2, 8, 30, true));

        BodyPartGUIs.addGUI(BodyPartNames.jawPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.jawPart + ".png"), 0, -2, 18, 20, true));
        BodyPartGUIs.addGUI(BodyPartNames.reverseJointLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.reverseJointLegPart + "left.png"), 1, 2, 8, 24, true));
        BodyPartGUIs.addGUI(BodyPartNames.reverseJointLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.reverseJointLegPart + "right.png"), -1, 2, 8, 24, true));
    }
}
