package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;

public class MovementOverridePartOption extends BodyPartOption
{
    public MovementOverridePartOption(String partID, String position, String subPosition, String displayNamePos, int qiToForge)
    {
        super(partID, position, subPosition, displayNamePos, qiToForge);
    }

    // Called when the keybinding for the specified input is pressed
    // If any of these return true, then vanilla movement for that input will be cancelled
    public boolean overwriteForward()
    {
        return false;
    }

    public boolean overwriteLeft()
    {
        return false;
    }

    public boolean overwriteRight()
    {
        return false;
    }

    public boolean overwriteBackward()
    {
        return false;
    }

    public boolean overwriteJump()
    {
        return false;
    }
}
