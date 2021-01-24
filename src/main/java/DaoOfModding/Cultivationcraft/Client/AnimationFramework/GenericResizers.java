package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector3d;

public class GenericResizers
{
    public static resizeModule getLeftArmResizer()
    {
        return new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(-1, -2, -2), new Vector3d(4, 12, 4), new Vector3d(1, 1, 0));
    }

    public static resizeModule getRightArmResizer()
    {
        return new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(-3, -2, -2), new Vector3d(4, 12, 4), new Vector3d(-1, 1, 0));
    }

    public static resizeModule getLeftLegResizer()
    {
        return new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(-2, 0, -2), new Vector3d(4, 12, 4), new Vector3d(0, 1, -1));
    }

    public static resizeModule getRightLegResizer()
    {
        return new defaultResizeModule(2, new Vector3d(0, 1, 0), new Vector3d(-2, 0, -2), new Vector3d(4, 12, 4), new Vector3d(0, 1, -1));
    }

    public static resizeModule getHeadResizer()
    {
        return new defaultResizeModule(1, new Vector3d(0, 1, 0), new Vector3d(-4, -8, -4), new Vector3d(8, 8, 8), new Vector3d(0, 1, 1));
    }
}
