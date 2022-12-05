package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

public class MovementOverrideTechnique extends Technique
{
    public MovementOverrideTechnique()
    {
        super();

        multiple = false;
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
