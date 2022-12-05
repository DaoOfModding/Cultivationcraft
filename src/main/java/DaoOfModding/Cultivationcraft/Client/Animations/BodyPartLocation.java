package DaoOfModding.Cultivationcraft.Client.Animations;

import net.minecraft.world.phys.Vec3;

public class BodyPartLocation
{
    String positionID;
    String positionSubID;

    String modelPositionID;
    String modelPositionSubID;

    String modelID;

    int rotationDepth = 0;

    Vec3 posOverride;
    Vec3 rotationpointOverride;
    Vec3 fixedPosOverride;

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

    public void adjustPos(Vec3 pos)
    {
        posOverride = pos;
    }

    public void adjustRotationPoint(Vec3 rotationPoint)
    {
        rotationpointOverride = rotationPoint;
    }

    public void adjustFixedPos(Vec3 fixedPos)
    {
        fixedPosOverride = fixedPos;
    }
}
