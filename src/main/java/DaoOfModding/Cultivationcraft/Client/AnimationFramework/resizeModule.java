package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

public interface resizeModule
{
    public Vector3d getSize();
    public Vector3d getNextRotation();
    public Vector3d getPosition();
    public Vector2f getTextureModifier();
    public float getDelta();
    public resizeModule nextLevel();
    public boolean continueResizing();
}
