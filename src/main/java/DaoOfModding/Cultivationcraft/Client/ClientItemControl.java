package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUIs;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUI;
import DaoOfModding.Cultivationcraft.Client.GUI.Screens.FlyingSwordContainerScreen;
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

        ScreenManager.register(Register.ContainerTypeFlyingSword, FlyingSwordContainerScreen::new);

        setupDefaultBodyPartGUIs();
    }

    public static void setupDefaultBodyPartGUIs()
    {
        // TODO: Setup to display parts for options
        // TODO: Setupd to adjust part location based on connections

        BodyPartGUIs.addGUI(BodyPartNames.DefaultBody, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultBody + ".png"), 0, 0, 18, 28, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultHead, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultHead + ".png"), 0, -2, 18, 20, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultLeftArm, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftArm + ".png"), 2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultRightArm, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightArm + ".png"), -2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultLeftLeg, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftLeg + ".png"), 1, 2, 8, 30, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultRightLeg, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightLeg + ".png"), -1, 2, 8, 30, true));

        BodyPartGUIs.addGUI(BodyPartNames.reinforcedBodyPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultBody + ".png"), 0, 0, 18, 28, true));
        BodyPartGUIs.addGUI(BodyPartNames.expandingBodyPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultBody + ".png"), 0, 0, 18, 28, true));
        BodyPartGUIs.addGUI(BodyPartNames.shortBodyPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.shortBodyPart + ".png"), 0, 0, 18, 14, true));

        BodyPartGUIs.addGUI(BodyPartNames.reinforcedArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftArm + ".png"), 2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.reinforcedArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightArm + ".png"), -2, -1, 11, 29, true));

        BodyPartGUIs.addGUI(BodyPartNames.shortArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.shortArmPart + "left.png"), 2, -1, 11, 15, true));
        BodyPartGUIs.addGUI(BodyPartNames.shortArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.shortArmPart + "right.png"), -2, -1, 11, 15, true));

        BodyPartGUIs.addGUI(BodyPartNames.glideArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.glideArmPart + "left.png"), 2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.glideArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.glideArmPart + "right.png"), -2, -1, 11, 29, true));

        BodyPartGUIs.addGUI(BodyPartNames.jawPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.jawPart + ".png"), 0, -2, 18, 20, true));
        BodyPartGUIs.addGUI(BodyPartNames.longNeckPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.longNeckPart + ".png"), 0, -2, 18, 35, true));

        BodyPartGUIs.addGUI(BodyPartNames.feetPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.feetPart + "left.png"), 1, 2, 8, 8, true));
        BodyPartGUIs.addGUI(BodyPartNames.feetPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.feetPart + "right.png"), -1, 2, 8, 8, true));
        BodyPartGUIs.addGUI(BodyPartNames.reverseJointLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.reverseJointLegPart + "left.png"), 1, 2, 8, 24, true));
        BodyPartGUIs.addGUI(BodyPartNames.reverseJointLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.reverseJointLegPart + "right.png"), -1, 2, 8, 24, true));
        BodyPartGUIs.addGUI(BodyPartNames.singleLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightLeg + ".png"), 0, 2, 8, 24, true));
        BodyPartGUIs.addGUI(BodyPartNames.hexaLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.hexaLegPart + "left.png"), -1.6f, 2, 19, 19, true));
        BodyPartGUIs.addGUI(BodyPartNames.hexaLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.hexaLegPart + "right.png"), 1.65f, 2, 19, 19, true));
        BodyPartGUIs.addGUI(BodyPartNames.jetLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftLeg + ".png"), 1, 2, 8, 30, true));
        BodyPartGUIs.addGUI(BodyPartNames.jetLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightLeg + ".png"), -1, 2, 8, 30, true));

    }
}
