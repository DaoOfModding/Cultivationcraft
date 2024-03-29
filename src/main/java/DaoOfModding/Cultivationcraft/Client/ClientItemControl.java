package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUIs;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Client.GUI.BodyPartGUI;
import DaoOfModding.Cultivationcraft.Client.GUI.Screens.FlyingSwordContainerScreen;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Common.Register;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


public class ClientItemControl
{
    public static Boolean hasLoaded = false;

    public static void init(FMLClientSetupEvent event)
    {
        MenuScreens.register(Register.ContainerTypeFlyingSword.get(), FlyingSwordContainerScreen::new);

        setupDefaultBodyPartGUIs();
    }

    public static void setupDefaultBodyPartGUIs()
    {
        // TODO: Setup to display parts for options
        // TODO: Setup to adjust part location based on connections

        BodyPartGUIs.addGUI(BodyPartNames.DefaultBody, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultBody + ".png"), 0, 0, 18, 28, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultHead, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultHead + ".png"), 0, -2, 18, 20, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultLeftArm, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftArm + ".png"), 2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultRightArm, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightArm + ".png"), -2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultLeftLeg, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftLeg + ".png"), 1, 2, 8, 30, true));
        BodyPartGUIs.addGUI(BodyPartNames.DefaultRightLeg, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightLeg + ".png"), -1, 2, 8, 30, true));

        BodyPartGUIs.addGUI(BodyPartNames.reinforcedBodyPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultBody + ".png"), 0, 0, 18, 28, true));
        BodyPartGUIs.addGUI(BodyPartNames.expandingBodyPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultBody + ".png"), 0, 0, 18, 28, true));
        BodyPartGUIs.addGUI(BodyPartNames.shortBodyPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.shortBodyPart + ".png"), 0, 0, 18, 14, true));

        BodyPartGUIs.addGUI(BodyPartNames.reinforcedLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftLeg + ".png"), 1, 2, 8, 30, true));
        BodyPartGUIs.addGUI(BodyPartNames.reinforcedLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightLeg + ".png"), -1, 2, 8, 30, true));

        BodyPartGUIs.addGUI(BodyPartNames.reinforcedArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftArm + ".png"), 2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.reinforcedArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightArm + ".png"), -2, -1, 11, 29, true));

        BodyPartGUIs.addGUI(BodyPartNames.shortArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.shortArmPart + "left.png"), 2, -1, 11, 15, true));
        BodyPartGUIs.addGUI(BodyPartNames.shortArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.shortArmPart + "right.png"), -2, -1, 11, 15, true));

        BodyPartGUIs.addGUI(BodyPartNames.flipperArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.flipperArmPart + "left.png"), 2, -1, 3, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.flipperArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.flipperArmPart + "right.png"), -2, -1, 3, 29, true));

        BodyPartGUIs.addGUI(BodyPartNames.longArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.longArmPart + "left.png"), 2, -1, 11, 39, true));
        BodyPartGUIs.addGUI(BodyPartNames.longArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.longArmPart + "right.png"), -2, -1, 11, 39, true));

        BodyPartGUIs.addGUI(BodyPartNames.bulkyArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.bulkyArmPart + "left.png"), 2, -1, 16, 24, true));
        BodyPartGUIs.addGUI(BodyPartNames.bulkyArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.bulkyArmPart + "right.png"), -2, -1, 16, 24, true));

        BodyPartGUIs.addGUI(BodyPartNames.glideArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.glideArmPart + "left.png"), 2, -1, 11, 29, true));
        BodyPartGUIs.addGUI(BodyPartNames.glideArmPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.glideArmPart + "right.png"), -2, -1, 11, 29, true));

        BodyPartGUIs.addGUI(BodyPartNames.jawPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.jawPart + ".png"), 0, -2, 18, 20, true));
        BodyPartGUIs.addGUI(BodyPartNames.longNeckPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.longNeckPart + ".png"), 0, -2, 18, 35, true));

        BodyPartGUIs.addGUI(BodyPartNames.feetPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.feetPart + "left.png"), 1, 2, 8, 8, true));
        BodyPartGUIs.addGUI(BodyPartNames.feetPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.feetPart + "right.png"), -1, 2, 8, 8, true));
        BodyPartGUIs.addGUI(BodyPartNames.largeLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.largeLegPart + "left.png"), 2, -1, 16, 60, true));
        BodyPartGUIs.addGUI(BodyPartNames.largeLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.largeLegPart + "right.png"), -2, -1, 16, 60, true));
        BodyPartGUIs.addGUI(BodyPartNames.reverseJointLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.reverseJointLegPart + "left.png"), 1, 2, 8, 24, true));
        BodyPartGUIs.addGUI(BodyPartNames.reverseJointLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.reverseJointLegPart + "right.png"), -1, 2, 8, 24, true));
        BodyPartGUIs.addGUI(BodyPartNames.singleLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightLeg + ".png"), 0, 2, 8, 24, true));
        BodyPartGUIs.addGUI(BodyPartNames.hexaLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.hexaLegPart + "left.png"), -1.6f, 2, 19, 19, true));
        BodyPartGUIs.addGUI(BodyPartNames.hexaLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.hexaLegPart + "right.png"), 1.65f, 2, 19, 19, true));
        BodyPartGUIs.addGUI(BodyPartNames.jetLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftLeg + ".png"), 1, 2, 8, 30, true));
        BodyPartGUIs.addGUI(BodyPartNames.jetLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightLeg + ".png"), -1, 2, 8, 30, true));
        BodyPartGUIs.addGUI(BodyPartNames.airJumpLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultLeftLeg + ".png"), 1, 2, 8, 30, true));
        BodyPartGUIs.addGUI(BodyPartNames.airJumpLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.DefaultRightLeg + ".png"), -1, 2, 8, 30, true));
        BodyPartGUIs.addGUI(BodyPartNames.longLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.longLegPart + "left.png"), 1, 2, 8, 45, true));
        BodyPartGUIs.addGUI(BodyPartNames.longLegPart, new BodyPartGUI(new ResourceLocation(Cultivationcraft.MODID, "textures/gui/bodyparts/" + BodyPartNames.longLegPart + "right.png"), -1, 2, 8, 45, true));

    }
}
