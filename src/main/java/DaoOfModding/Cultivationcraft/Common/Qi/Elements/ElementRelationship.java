package DaoOfModding.Cultivationcraft.Common.Qi.Elements;

public class ElementRelationship
{
    public int defendingElementID;
    public double modifier;

    public ElementRelationship(int ID, double damageModifier)
    {
        defendingElementID = ID;
        modifier = damageModifier;
    }
}
