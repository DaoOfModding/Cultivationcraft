package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.mlmanimator.Client.AnimationFramework.resizeModule;
import DaoOfModding.mlmanimator.Client.Models.defaultResizeModule;
import net.minecraft.util.math.vector.Vector3d;

public class teethResizeModule extends defaultResizeModule
{
    public teethResizeModule(int maxDepth, Vector3d direction, Vector3d position, Vector3d fullSize, Vector3d rotationPoint, Vector3d spacing)
    {
        super(maxDepth, direction, position, fullSize, rotationPoint, spacing, 0.0F);
    }

    @Override
    public resizeModule nextLevel()
    {
        --this.depth;
        return this;
    }
}
