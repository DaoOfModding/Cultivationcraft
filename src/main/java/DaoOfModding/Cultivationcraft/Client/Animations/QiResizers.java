package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.mlmanimator.Client.Models.defaultResizeModule;
import net.minecraft.util.math.vector.Vector3d;

public class QiResizers
{
    public static defaultResizeModule getTeethResizer(int numberOfTeeth, float length, float height, float spacing)
    {
        return new defaultResizeModule(numberOfTeeth, new Vector3d(1, 0, 0), new Vector3d(0, 0, 0), new Vector3d(length, height, 0.9), new Vector3d(1, 0, 0), new Vector3d(spacing, 0, 0));
    }

    public static defaultResizeModule getSideTeethResizer(int numberOfTeeth, float length, float height, float spacing)
    {
        return new defaultResizeModule(numberOfTeeth, new Vector3d(0, 0, 1), new Vector3d(0, 0, 0), new Vector3d(0.9, height, length), new Vector3d(0, 0, 1), new Vector3d(0, 0, spacing));
    }
}
