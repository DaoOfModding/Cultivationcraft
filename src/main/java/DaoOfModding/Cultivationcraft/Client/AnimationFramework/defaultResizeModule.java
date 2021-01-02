package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

public class defaultResizeModule implements resizeModule
{
    int depth;

    Vector3d usedSize;
    Vector3d size;
    Vector3d direction;
    Vector3d rotationPoint;

    Vector3d rotation;
    Vector3d position;
    Vector2f textureModifier;

    public defaultResizeModule(int maxDepth, Vector3d direction, Vector3d position, Vector3d fullSize, Vector3d rotationPoint)
    {
        depth = maxDepth;
        size = fullSize;

        usedSize = new Vector3d(0, 0, 0);

        // Ensure the direction vector is normalized
        this.direction = direction.normalize();
        this.rotationPoint = rotationPoint;
        this.position = position;
    }

    // Return the raw position coordinates for the current model
    public Vector3d getRawPosition()
    {
        return usedSize.mul(direction).add(position);
    }

    public Vector3d getPosition()
    {
        return position;
    }

    public Vector3d getSize()
    {
        // Calculate the size of this model, and the size remaining to make models for
        Vector3d thisSize = size.subtract(direction.mul(size).scale((double)(depth-1)/(double)depth));
        size = size.subtract(direction.mul(size).scale((double)1/(double)depth));

        if (usedSize.length() == 0)
            usedSize = thisSize;
        else
            usedSize = usedSize.add(thisSize.mul(direction));


        // Calculate the new models rotation point vector to be connected to the 'bottom' of this model
        Vector3d midPoint = position.add(thisSize.scale(0.5));
        rotation = midPoint.add(thisSize.scale(0.5).mul(rotationPoint));


        // Calculate the position of the next model
        Vector3d modifier = thisSize.mul(direction);
        position = position.add(modifier).subtract(rotation);


        // TODO: Ensure this texture offset is correct
        textureModifier = new Vector2f((float)(modifier.x + modifier.z), (float)modifier.y);

        return thisSize;
    }

    public Vector3d getNextRotation()
    {
        return rotation;
    }

    public Vector2f getTextureModifier()
    {
        return textureModifier;
    }

    public resizeModule nextLevel()
    {
        depth -= 1;

        return this;
    }

    public boolean continueResizing()
    {
        if (depth == 1)
            return false;

        return true;
    }

    public float getDelta()
    {
        return 0;
    }
}
