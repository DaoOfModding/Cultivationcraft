package DaoOfModding.Cultivationcraft.Client.Animations;

public class BodyPartLocation
{
    String positionID;
    String positionSubID;

    String modelPositionID;
    String modelPositionSubID;

    String modelID;

    int rotationDepth = 0;

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
}
