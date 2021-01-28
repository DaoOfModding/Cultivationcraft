package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.defaultResizeModule;
import net.minecraft.util.math.vector.Vector3d;

public class QiResizers
{
    public static defaultResizeModule getTeethResizer()
    {
        return new defaultResizeModule(8, new Vector3d(1, 0, 0), new Vector3d(0, 0, 0), new Vector3d(7.8, 1, 0.9), new Vector3d(1, 0, 0), new Vector3d(0.1, 0, 0));
    }

    public static defaultResizeModule getSideTeethResizer()
    {
        return new defaultResizeModule(6, new Vector3d(0, 0, 1), new Vector3d(0, 0, 0), new Vector3d(0.9, 1, 5.8), new Vector3d(0, 0, 1), new Vector3d(0, 0, 0.1));
    }
}
