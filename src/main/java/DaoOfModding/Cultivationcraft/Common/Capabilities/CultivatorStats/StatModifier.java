package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats;

public class StatModifier
{
    private String id;

    private int attack = 0;
    private int attackRange = 0;

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

    public void setAttackRange(int aRange)
    {
        attackRange = aRange;
    }

    public int getAttackRange()
    {
        return attackRange;
    }

    public void combine(StatModifier modifier)
    {
        attack += modifier.getAttack();
        attackRange += modifier.getAttackRange();
    }
}
