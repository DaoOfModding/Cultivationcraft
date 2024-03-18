package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.mlmanimator.Client.Models.defaultResizeModule;
import net.minecraft.world.phys.Vec3;

public class QiResizers
{
    public static defaultResizeModule getTeethResizer(int numberOfTeeth, float length, float height, float spacing)
    {
        return new teethResizeModule(numberOfTeeth, new Vec3(1, 0, 0), new Vec3(1, 0.5, 0.5), new Vec3(length, height, 0.9), new Vec3(1, 0.5, 0.5), new Vec3(spacing, 0, 0));
    }

    public static defaultResizeModule getSideTeethResizer(int numberOfTeeth, float length, float height, float spacing)
    {
        return new teethResizeModule(numberOfTeeth, new Vec3(0, 0, 1), new Vec3(0.5, 0.5, 1), new Vec3(0.9, height, length), new Vec3(0.5, 0.5, 1), new Vec3(0, 0, spacing));
    }
}
