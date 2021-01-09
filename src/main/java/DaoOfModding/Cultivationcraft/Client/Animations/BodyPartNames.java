package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyPartNames
{
    public static String TestPart = "test";

    public static String DefaultLeftArm = "armleft";
    public static String DefaultRightArm = "armright";
    public static String DefaultRightLeg = "legright";
    public static String DefaultLeftLeg = "legleft";
    public static String DefaultHead = "head";
    public static String DefaultBody = "body";

    public static String headPosition = "HEAD";
    public static String bodyPosition = "BODY";
    public static String armPosition = "ARM";
    public static String legPosition = "LEG";

    private static ArrayList<BodyPart> parts = new ArrayList<BodyPart>();
    private static HashMap<String, String> displayNames = new HashMap<String, String>();
    private static HashMap<String, String> partPositions = new HashMap<String, String>();

    public static void init()
    {
        addDisplayName(headPosition, "cultivationcraft.gui.headpart");
        addDisplayName(bodyPosition, "cultivationcraft.gui.bodypart");
        addDisplayName(armPosition, "cultivationcraft.gui.armpart");
        addDisplayName(legPosition, "cultivationcraft.gui.legpart");

        addPartPosition(TestPart, armPosition);

        ArrayList<String> testList = new ArrayList<String>();
        testList.add(BodyPartNames.TestPart);
        addPart(new BodyPart(testList));
    }

    public static String getDisplayName(String position)
    {
        return new TranslationTextComponent(displayNames.get(position)).getString();
    }

    public static void addDisplayName(String position, String name)
    {
        displayNames.put(position, name);
    }

    public static void addPart(BodyPart part)
    {
        parts.add(part);
    }

    public static BodyPart getPart(String part)
    {
        for (BodyPart searchPart : parts)
            if (searchPart.getModelIDs().contains(part))
                return searchPart;

        return null;
    }

    public static ArrayList<BodyPart> getParts()
    {
        return parts;
    }

    public static String getPartPosition(String part)
    {
        return partPositions.get(part);
    }

    public static void addPartPosition(String part, String position)
    {
        partPositions.put(part, position);
    }
}
