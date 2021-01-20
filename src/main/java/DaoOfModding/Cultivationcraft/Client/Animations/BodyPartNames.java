package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyPartNames
{
    public static final String TestPart = "test";
    public static final String startingEyesPart = "QiSight";

    public static final String DefaultLeftArm = "armleft";
    public static final String DefaultRightArm = "armright";
    public static final String DefaultRightLeg = "legright";
    public static final String DefaultLeftLeg = "legleft";
    public static final String DefaultHead = "head";
    public static final String DefaultBody = "body";

    public static final String headPosition = "HEAD";
    public static final String bodyPosition = "BODY";
    public static final String armPosition = "ARM";
    public static final String legPosition = "LEG";

    public static final String eyeSubPosition = "EYE";

    private static ArrayList<BodyPart> parts = new ArrayList<BodyPart>();
    private static HashMap<String, String> displayNames = new HashMap<String, String>();
    private static HashMap<String, HashMap<String, String>> subPartdisplayNames = new HashMap<String, HashMap<String, String>>();

    public static void init()
    {
        addDisplayName(headPosition, "cultivationcraft.gui.headpart");
        addDisplayName(bodyPosition, "cultivationcraft.gui.bodypart");
        addDisplayName(armPosition, "cultivationcraft.gui.armpart");
        addDisplayName(legPosition, "cultivationcraft.gui.legpart");
        addSubPartDisplayName(headPosition, eyeSubPosition, "cultivationcraft.gui.headpart.eye");

        //addSubPartDisplayName(armPosition, "test", "cultivationcraft.test");

        //ArrayList<String> testList = new ArrayList<String>();
        //testList.add(BodyPartModelNames.TestPartModel);
        //addPart(new BodyPart(TestPart, testList, armPosition, "test"));

        addPart(new BodyPart(startingEyesPart, new ArrayList<String>(), headPosition, eyeSubPosition, "cultivationcraft.gui.headpart.eye.QiSight"));
    }

    public static String getDisplayName(String position)
    {
        return new TranslationTextComponent(displayNames.get(position)).getString();
    }

    public static String getDisplayName(String position, String subPosition)
    {
        return new TranslationTextComponent(subPartdisplayNames.get(position).get(subPosition)).getString();
    }

    public static void addDisplayName(String position, String name)
    {
        displayNames.put(position, name);
    }

    public static void addSubPartDisplayName(String position, String subPosition, String name)
    {
        if (!subPartdisplayNames.containsKey(position))
            subPartdisplayNames.put(position, new HashMap<String, String>());

        subPartdisplayNames.get(position).put(subPosition, name);
    }

    public static void addPart(BodyPart part)
    {
        parts.add(part);
    }

    public static BodyPart getPart(String part)
    {
        for (BodyPart searchPart : parts)
            if (searchPart.getID().compareTo(part) == 0)
                return searchPart;

        return null;
    }

    public static ArrayList<BodyPart> getParts()
    {
        return parts;
    }
}
