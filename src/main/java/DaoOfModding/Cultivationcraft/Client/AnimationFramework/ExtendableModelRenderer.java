package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

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

    protected double minHeight = 0;


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
        Vector3d position = resizer.getPosition();
        Vector3d thisSize = resizer.getSize();
        Vector3d rotation = resizer.getNextRotation();
        Vector2f texModifier = resizer.getTextureModifier();

        // Add a box of the appropriate size to this model
        generateCube((float)position.x, (float)position.y, (float)position.z, (float)thisSize.x, (float)thisSize.y, (float)thisSize.z, resizer.getDelta());

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
    public void generateCube(float x, float y, float z, float width, float height, float depth, float delta)
    {
        float x2 = x + width;
        float y2 = y + height;
        float z2 = z + depth;

        points[0] = new Vector3d(x, y, z);
        points[1] = new Vector3d(x, y, z2);
        points[2] = new Vector3d(x, y2, z);
        points[3] = new Vector3d(x, y2, z2);
        points[4] = new Vector3d(x2, y, z);
        points[5] = new Vector3d(x2, y, z2);
        points[6] = new Vector3d(x2, y2, z);
        points[7] = new Vector3d(x2, y2, z2);

        addBox(x, y, z, width, height, depth, delta);
    }

    // Get the minimum height of any point on this model
    public double getMinHeight()
    {
        return minHeight;
    }

    public void calculateMinHeight(Vector3d rotation)
    {
        double min = Double.MAX_VALUE;

        for (Vector3d point : points)
        {
            Vector3d rotated = point;
            rotated = rotated.subtract(rotationPointX, 0, 0);
            rotated = rotated.rotateRoll((float)rotation.x);
            rotated = rotated.add(rotationPointX, 0, 0);

            rotated = rotated.add(0, rotationPointY, 0);
            rotated = rotated.rotateYaw((float)rotation.y);
            rotated.subtract(0, rotationPointY, 0);

            rotated = rotated.subtract(0, 0, rotationPointZ);
            rotated = rotated.rotatePitch((float)rotation.z);
            rotated = rotated.add(0, 0, rotationPointZ);

            if (rotated.getY() < min)
                min = rotated.getY();
        }

        minHeight = min;
    }
}
