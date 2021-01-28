package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.*;

import java.util.ArrayList;

public class ExtendableModelRenderer extends ModelRenderer
{
    private int textureWidth = 64;
    private int textureHeight = 32;
    private int textureOffsetX;
    private int textureOffsetY;

    private ExtendableModelRenderer parent = null;
    private ArrayList<ExtendableModelRenderer> child = new ArrayList<ExtendableModelRenderer>();

    private Vector3f[] points = new Vector3f[8];

    private float minHeight = 0;

    private boolean look = false;

    private ResourceLocation customTexture = null;


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

    // Move all children from this model to another
    public void fosterChildren(ExtendableModelRenderer toMove)
    {
        for (ExtendableModelRenderer fosterChild : child)
            toMove.addChild(fosterChild);

        child.clear();
    }

    public void setCustomTexture(ResourceLocation newLocation)
    {
        customTexture = newLocation;
    }

    // Set whether this model should be looking in the direction of the player
    public void setLooking(boolean isLooking)
    {
        look = isLooking;
    }

    public Boolean isLooking()
    {
        return look;
    }

    public ExtendableModelRenderer getParent()
    {
        return parent;
    }

    public void setParent(ExtendableModelRenderer parent)
    {
        this.parent = parent;
    }

    public ArrayList<ExtendableModelRenderer> getChildren()
    {
        return child;
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

        addChild(newModel);

        // Continue the extension
        newModel.extend(resizer.nextLevel());
    }

    @Override
    public void addChild(ModelRenderer c)
    {
        super.addChild(c);

        child.add((ExtendableModelRenderer)c);
        ((ExtendableModelRenderer)c).setParent(this);
    }

    // SERIOUSLY vanilla minecraft, why does this not exist?
    public void removeChild(ExtendableModelRenderer toRemove)
    {
        child.remove(toRemove);

        // AWFUL hack to make this model no longer render, since there is NO WAY to remove a child from the ModelRenderer's childModels list
        toRemove.showModel = false;

        toRemove.setParent(null);
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

        points[0] = new Vector3f(x1, y1, z1);
        points[1] = new Vector3f(x1, y1, z2);
        points[2] = new Vector3f(x1, y2, z1);
        points[3] = new Vector3f(x1, y2, z2);
        points[4] = new Vector3f(x2, y1, z1);
        points[5] = new Vector3f(x2, y1, z2);
        points[6] = new Vector3f(x2, y2, z1);
        points[7] = new Vector3f(x2, y2, z2);

        addBox((float)pos.x, (float)pos.y, (float)pos.z, width, height, depth, delta);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        // Grab the vertex builder based on the texture to use for this model
        if (customTexture == null)
            bufferIn = MultiLimbedRenderer.getVertexBuilder();
        else
            bufferIn = MultiLimbedRenderer.getVertexBuilder(customTexture);

        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    // Get the minimum height of any point on this model
    public float getMinHeight()
    {
        return minHeight;
    }

    public void calculateMinHeight(MatrixStack matrixStackIn)
    {
        matrixStackIn.push();

        float min = Float.MAX_VALUE * -1;

        rotateMatrix(matrixStackIn);

        Matrix4f rotator = matrixStackIn.getLast().getMatrix();

        for (Vector3f point : points)
        {
            Vector4f vector4f = new Vector4f(point.getX(), point.getY(), point.getZ(), 1.0F);
            vector4f.transform(rotator);

            if (vector4f.getY() > min)
                min = vector4f.getY();
        }

        minHeight = min;

        // Calculate the min height of children
        for (ExtendableModelRenderer testChild : child)
            testChild.calculateMinHeight(matrixStackIn);

        matrixStackIn.pop();
    }

    public void rotateMatrix(MatrixStack matrixStackIn) {
        matrixStackIn.translate((double)(this.rotationPointX), (double)(this.rotationPointY), (double)(this.rotationPointZ));
        if (this.rotateAngleZ != 0.0F) {
            matrixStackIn.rotate(Vector3f.ZP.rotation(this.rotateAngleZ));
        }

        if (this.rotateAngleY != 0.0F) {
            matrixStackIn.rotate(Vector3f.YP.rotation(this.rotateAngleY));
        }

        if (this.rotateAngleX != 0.0F) {
            matrixStackIn.rotate(Vector3f.XP.rotation(this.rotateAngleX));
        }

    }
}
