package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.*;

public class ExtendableModelRenderer extends ModelRenderer
{
    // STUPID BASE MINECRAFT, STOP MAKING EVERYTHING PRIVATE
    protected int textureWidth = 64;
    protected int textureHeight = 32;
    protected int textureOffsetX;
    protected int textureOffsetY;

    protected ExtendableModelRenderer parent = null;
    protected ExtendableModelRenderer child = null;

    protected Vector3d[] points = new Vector3d[8];
    protected Vector3d[] rotatedPoints = new Vector3d[8];

    protected double minHeight = 0;

    protected Vector3d rotatedRotationPoint = new Vector3d(0, 0, 0);


    public ExtendableModelRenderer(Model model)
    {
        super(model);

        textureWidth = model.textureWidth;
        textureHeight = model.textureHeight;
    }

    public ExtendableModelRenderer(Model model, int texOffX, int texOffY)
    {
        super(model, texOffX, texOffY);

        textureWidth = model.textureWidth;
        textureHeight = model.textureHeight;
        textureOffsetX = texOffX;
        textureOffsetY = texOffY;
    }

    public ExtendableModelRenderer(int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn)
    {
        super(textureWidthIn, textureHeightIn, textureOffsetXIn, textureOffsetYIn);

        textureWidth = textureWidthIn;
        textureHeight = textureHeightIn;
        textureOffsetX = textureOffsetXIn;
        textureOffsetY = textureOffsetYIn;
    }

    public void setParent(ExtendableModelRenderer parent)
    {
        this.parent = parent;
    }

    public ExtendableModelRenderer getChild(int depth)
    {
        if (depth == 0)
            return this;

        if (child == null)
            return null;

        return child.getChild(depth - 1);
    }

    // Extend the model, creating depth amount of boxes equaling a total of fullSize extending towards direction
    // Each Model will rotate around the midpoint of the previous model, a 1 or -1 in the rotationPoint will move that point to the edge of the specified side
    public void extend(resizeModule resizer)
    {
        Vector3d rawPosition = resizer.getRawPosition();
        Vector3d position = resizer.getPosition();
        Vector3d thisSize = resizer.getSize();
        Vector3d rotation = resizer.getNextRotation();
        Vector2f texModifier = resizer.getTextureModifier();

        // Add a box of the appropriate size to this model
        generateCube(position, (float)thisSize.x, (float)thisSize.y, (float)thisSize.z, resizer.getDelta());

        // Return this model if at max depth
        if (!resizer.continueResizing())
            return;


        // Create the next model and add it as a child of this one
        ExtendableModelRenderer newModel = new ExtendableModelRenderer(textureWidth, textureHeight, textureOffsetX + (int)texModifier.x, textureOffsetY + (int)texModifier.y);
        newModel.setRotationPoint((float)rotation.x, (float)rotation.y, (float)rotation.z);
        newModel.mirror = this.mirror;
        newModel.setParent(this);

        child = newModel;
        addChild(newModel);

        // Continue the extension
        newModel.extend(resizer.nextLevel());
    }

    // Generate the cube for this model
    public void generateCube(Vector3d pos, float width, float height, float depth, float delta)
    {
        float x1 = (float)pos.x;
        float y1 = (float)pos.y;
        float z1 = (float)pos.z;

        float x2 = x1 + width;
        float y2 = y1 + height;
        float z2 = z1 + depth;

        points[0] = new Vector3d(x1, y1, z1);
        points[1] = new Vector3d(x1, y1, z2);
        points[2] = new Vector3d(x1, y2, z1);
        points[3] = new Vector3d(x1, y2, z2);
        points[4] = new Vector3d(x2, y1, z1);
        points[5] = new Vector3d(x2, y1, z2);
        points[6] = new Vector3d(x2, y2, z1);
        points[7] = new Vector3d(x2, y2, z2);

        addBox((float)pos.x, (float)pos.y, (float)pos.z, width, height, depth, delta);
    }

    // Get the minimum height of any point on this model
    public double getMinHeight()
    {
        return minHeight;
    }

    public void calculateMinHeight(Vector3d rotation)
    {
        double min = Double.MAX_VALUE * -1;

        rotatePoints(rotation, rotatedRotationPoint);

        for (Vector3d point : rotatedPoints)
            if (point.getY() > min)
                min = point.getY();

        minHeight = min;

        // Conduct this rotation on all children
        ExtendableModelRenderer children = child;

        while (children != null)
        {
            child.adjustRotations(rotation, rotatedRotationPoint);
            children = children.child;
        }
    }

    // Rotate the model based on the specified rotation and rotationPoint
    protected void adjustRotations(Vector3d rotation, Vector3d rotationPoint)
    {
        rotatedRotationPoint = rotateAroundPoint(rotatedRotationPoint, rotation, rotationPoint);

        rotatePoints(rotation, rotationPoint);
    }

    // Rotate the models points based on the specified rotation and rotationPoint
    protected void rotatePoints(Vector3d rotation, Vector3d rotationPoint)
    {
        for (int i = 0; i < 8; i++)
            rotatedPoints[i] = rotateAroundPoint(rotatedPoints[i], rotation, rotationPoint);
    }

    // Reset any adjustments height calculations
    public void resetHeightAdjustment()
    {
        rotatedRotationPoint = new Vector3d(rotationPointX, rotationPointY, rotationPointZ);
        rotatedPoints = points.clone();
    }

    // Rotate supplied vector around rotationPoint by specified rotation
    protected Vector3d rotateAroundPoint(Vector3d point, Vector3d rotation, Vector3d rotationPoint)
    {
        Vector3d rotated = point;

        rotated = rotated.add(rotationPoint);

        if (rotation.z != 0)
            rotated = rotated.rotatePitch((float)rotation.z);
        
        if (rotation.y != 0)
            rotated = rotated.rotateYaw((float)rotation.y);

        if (rotation.x != 0)
            rotated = rotated.rotateRoll((float)rotation.x);


        return rotated;
    }
}
