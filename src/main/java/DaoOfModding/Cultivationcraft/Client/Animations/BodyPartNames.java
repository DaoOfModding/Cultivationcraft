package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyPartNames
{
    // TODO: Move BodyPart classes to a more appropriate place (in common)

    // PARTS
    public static final String jawPart = "jaw";
    public static final String reverseJointLegPart = "rjleg";

    // OPTIONS
    public static final String startingEyesPart = "QiSight";

    // DEFAULTS
    public static final String DefaultLeftArm = "armleft";
    public static final String DefaultRightArm = "armright";
    public static final String DefaultRightLeg = "legright";
    public static final String DefaultLeftLeg = "legleft";
    public static final String DefaultHead = "head";
    public static final String DefaultBody = "body";

    // POSITIONS
    public static final String headPosition = "HEAD";
    public static final String bodyPosition = "BODY";
    public static final String armPosition = "ARM";
    public static final String legPosition = "LEG";

    // SUB-POSITIONS
    public static final String basePosition = "BASE";

    public static final String eyeSubPosition = "EYE";

    private static ArrayList<BodyPart> parts = new ArrayList<BodyPart>();
    private static ArrayList<BodyPartOption> options = new ArrayList<BodyPartOption>();
    private static HashMap<String, String> displayNames = new HashMap<String, String>();
    private static HashMap<String, HashMap<String, String>> subPartDisplayNames = new HashMap<String, HashMap<String, String>>();

    public static void init()
    {
        addDisplayName(headPosition, "cultivationcraft.gui.headpart");
        addDisplayName(bodyPosition, "cultivationcraft.gui.bodypart");
        addDisplayName(armPosition, "cultivationcraft.gui.armpart");
        addDisplayName(legPosition, "cultivationcraft.gui.legpart");

        addDisplayName(basePosition, "cultivationcraft.gui.generic.base");

        addSubPartDisplayName(headPosition, eyeSubPosition, "cultivationcraft.gui.headpart.eye");

        setupHeadParts();
        setupHeadOptions();

        setupLegParts();
    }

    private static void setupHeadParts()
    {
        ArrayList<String> jawList = new ArrayList<String>();
        jawList.add(BodyPartModelNames.jawModel);

        addPart(new BodyPart(jawPart, jawList, headPosition, "cultivationcraft.gui.headpart.jaw", 1000));
    }

    private static void setupHeadOptions()
    {
        addOption(new BodyPartOption(startingEyesPart, headPosition, eyeSubPosition, "cultivationcraft.gui.headpart.eye.QiSight", 1000));
    }

    private static void setupLegParts()
    {
        ArrayList<String> rjLegList = new ArrayList<String>();
        rjLegList.add(BodyPartModelNames.reverseJointRightLegModel);
        rjLegList.add(BodyPartModelNames.reverseJointLeftLegModel);

        BodyPart rjLegPart = new BodyPart(reverseJointLegPart, rjLegList, legPosition, "cultivationcraft.gui.legpart.reversejoint", 1000);
        rjLegPart.getStatChanges().setJumpHeight(5);

        addPart(rjLegPart);
    }

    public static String getDisplayName(String position)
    {
        return new TranslationTextComponent(displayNames.get(position)).getString();
    }

    public static String getDisplayName(String position, String subPosition)
    {
        if (subPosition.compareTo(basePosition) == 0)
            return getDisplayName(subPosition);

        return new TranslationTextComponent(subPartDisplayNames.get(position).get(subPosition)).getString();
    }

    public static void addDisplayName(String position, String name)
    {
        displayNames.put(position, name);
    }

    public static void addSubPartDisplayName(String position, String subPosition, String name)
    {
        if (!subPartDisplayNames.containsKey(position))
            subPartDisplayNames.put(position, new HashMap<String, String>());

        subPartDisplayNames.get(position).put(subPosition, name);
    }

    public static void addPart(BodyPart part)
    {
        parts.add(part);
    }

    public static void addOption(BodyPartOption part)
    {
        options.add(part);
    }

    public static BodyPart getPart(String part)
    {
        for (BodyPart searchPart : parts)
            if (searchPart.getID().compareTo(part) == 0)
                return searchPart;

        return null;
    }

    public static BodyPartOption getOption(String part)
    {
        for (BodyPartOption searchPart : options)
            if (searchPart.getID().compareTo(part) == 0)
                return searchPart;

        return null;
    }

    public static ArrayList<BodyPart> getParts()
    {
        return parts;
    }

    public static ArrayList<BodyPartOption> getOptions()
    {
        return options;
    }
}
