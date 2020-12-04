package DaoOfModding.Cultivationcraft.Client.AnimationFramework;

import DaoOfModding.Cultivationcraft.Cultivationcraft;
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

    // Extend the model, creating depth amount of boxes equaling a total of fullSize extending towards direction
    // Each Model will rotate around the midpoint of the previous model, a 1 or -1 in the rotationPoint will move that point to the edge of the specified side
    // Return the ModelRender at the end of the extension
    public ModelRenderer extend(int depth, Vector3d direction, Vector3d position, Vector3d fullSize, Vector3d rotationPoint, float delta)
    {
        // TODO: Fix delta to not be weird as hell

        // Ensure the direction vector is normalized
        direction = direction.normalize();

        // Calculate the size of this model, and the size remaining to make models for
        Vector3d thisSize = fullSize.subtract(direction.mul(fullSize).scale((double)(depth-1)/(double)depth));
        Vector3d remainingSize = fullSize.subtract(direction.mul(fullSize).scale((double)1/(double)depth));

        // Add a box of the appropriate size to this model
        addBox((float)position.x, (float)position.y, (float)position.z, (float)thisSize.x, (float)thisSize.y, (float)thisSize.z, delta);

        // Return this model if at max depth
        if (depth <= 1)
            return this;

        // Calculate the new models rotation point vector to be connected to the 'bottom' of this model
        Vector3d midPoint = position.add(thisSize.scale(0.5));
        Vector3d rotation = midPoint.add(thisSize.scale(0.5).mul(rotationPoint));


        Vector3d modifier = thisSize.mul(direction);

        // Calculate the position of the next model
        position = position.add(modifier);

        // Create the next model and add it as a child of this one
        // TODO: Ensure this texture offset is correct
        ExtendableModelRenderer newModel = new ExtendableModelRenderer(textureWidth, textureHeight, textureOffsetX + (int)modifier.x + (int)modifier.z, textureOffsetY + (int)modifier.y);
        newModel.setRotationPoint((float)rotation.x, (float)rotation.y, (float)rotation.z);
        newModel.mirror = this.mirror;

        addChild(newModel);

        // Continue the extension
        return newModel.extend(depth-1, direction, position.subtract(rotation), remainingSize, rotationPoint, delta);
    }
}
