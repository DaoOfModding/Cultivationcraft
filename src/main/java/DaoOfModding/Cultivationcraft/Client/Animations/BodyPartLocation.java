package DaoOfModding.Cultivationcraft.Client.Animations;

import net.minecraft.util.math.vector.Vector3d;

public class BodyPartLocation
{
    String positionID;
    String positionSubID;

    String modelPositionID;
    String modelPositionSubID;

    String modelID;

    int rotationDepth = 0;

    Vector3d posOverride;
    Vector3d rotationpointOverride;
    Vector3d fixedPosOverride;

    // Only works for BASE position atm
    public BodyPartLocation(String newPositionID, String newPositionsubID, String newModelID)
    {
        positionID = newPositionID;
        positionSubID = newPositionsubID;

        modelID = newModelID;

        // TODO: allow adjusting rotationPoint on models
    }

    // Only works for BASE position atm
    public BodyPartLocation(String newPositionID, String newPositionsubID, String newModelPositionID, String newModelPositionSubID)
    {
        positionID = newPositionID;
        positionSubID = newPositionsubID;

        modelPositionID = newModelPositionID;
        modelPositionSubID = newModelPositionSubID;

        // TODO: allow adjusting rotationPoint on models
    }

    public void adjustDepth(int newDepth)
    {
        rotationDepth = newDepth;
    }

    public void adjustPos(Vector3d pos)
    {
        posOverride = pos;
    }

    public void adjustRotationPoint(Vector3d rotationPoint)
    {
        rotationpointOverride = rotationPoint;
    }

    public void adjustFixedPos(Vector3d fixedPos)
    {
        fixedPosOverride = fixedPos;
    }
}
