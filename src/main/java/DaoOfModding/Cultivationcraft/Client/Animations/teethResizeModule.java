package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.mlmanimator.Client.AnimationFramework.resizeModule;
import DaoOfModding.mlmanimator.Client.Models.defaultResizeModule;
import net.minecraft.world.phys.Vec3;

public class teethResizeModule extends defaultResizeModule
{
    public teethResizeModule(int maxDepth, Vec3 direction, Vec3 position, Vec3 fullSize, Vec3 rotationPoint, Vec3 spacing)
    {
        super(maxDepth, direction, position, fullSize, rotationPoint, spacing);
    }

    @Override
    public resizeModule nextLevel()
    {
        --this.depth;
        return this;
    }
}
