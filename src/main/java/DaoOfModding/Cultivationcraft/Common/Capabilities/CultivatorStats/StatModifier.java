package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

public class StatModifier
{
    protected String id;

    protected int attack = 0;
    protected double attackRange = 0;

    public StatModifier(String newID)
    {
        id = newID;
    }

    public void addAttack(int newAttack)
    {
        attack = newAttack;
    }

    public int getAttack()
    {
        return attack;
    }

    public void setAttackRange(double aRange)
    {
        attackRange = aRange;
    }

    public double getAttackRange()
    {
        return attackRange;
    }

    public void combine(StatModifier modifier)
    {
        attack += modifier.getAttack();
        attackRange += modifier.getAttackRange();
    }
}
