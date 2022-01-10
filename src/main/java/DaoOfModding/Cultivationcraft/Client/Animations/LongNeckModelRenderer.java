package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.mlmanimator.Client.Models.ExtendableModelRenderer;

public class LongNeckModelRenderer extends ExtendableModelRenderer
{
    int parentDepth;

    public LongNeckModelRenderer(int textureOffsetXIn, int textureOffsetYIn, int depth)
    {
        super(textureOffsetXIn, textureOffsetYIn);

        parentDepth = depth;
    }

    @Override
    public void rotate(float xRotation, float yRotation, float zRotation)
    {
        xRotation = xRotation / ((float)parentDepth+1);
        yRotation = yRotation / ((float)parentDepth+1);
        zRotation = zRotation / ((float)parentDepth+1);

        this.xRot = xRotation;
        this.yRot = yRotation;
        this.zRot = zRotation;

        int traverse = parentDepth;

        ExtendableModelRenderer traversing = this;

        while (traverse > 0)
        {
            traversing = traversing.getParent();
            traversing.rotate(xRotation, yRotation, zRotation);

            traverse--;
        }
    }
}
