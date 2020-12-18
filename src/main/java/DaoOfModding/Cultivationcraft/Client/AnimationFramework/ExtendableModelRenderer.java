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

    protected Vector3d rotationTarget = new Vector3d(0, 0, 0);

    protected ExtendableModelRenderer parent = null;
    protected ExtendableModelRenderer child = null;

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

    public ModelRenderer getChild(int depth)
    {
        if (depth == 0)
            return this;

        if (child == null)
            return null;

        return ((ExtendableModelRenderer)child).getChild(depth - 1);
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
        addBox((float)position.x, (float)position.y, (float)position.z, (float)thisSize.x, (float)thisSize.y, (float)thisSize.z, resizer.getDelta());

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
}
