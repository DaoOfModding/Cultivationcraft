package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.QiLung;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class QiLungs extends Lungs
{
    protected float suffocating = 0;

    public QiLungs()
    {
        setLung(0, new QiLung());
    }

    public void breath(Player player)
    {
        // Disable vanilla breathing
        player.setAirSupply(40);

        Breath breathing = BreathingHandler.getBreath(player);
        breathingColor = breathing.color;

        float amountRemaining = 10;

        // Go through each lung trying to use breath / amount of lungs
        for (int i = 0; i < lung.length; i++)
            amountRemaining -= lung[i].getLung().breath(amountRemaining / lung.length, breathing, player);

        // If there is still breath left, go through each lung again and try to use all of the breath
        if (amountRemaining > 0)
            for (int i = 0; i < lung.length; i++)
                amountRemaining -= lung[i].getLung().breath(amountRemaining, breathing, player);

        if (amountRemaining > 0)
            suffocate(amountRemaining);
        else
            clearSuffocate();

        if (getSuffocating() < 0)
        {
            player.hurt(DamageSource.DROWN, getSuffocating() / -20F);
        }
    }

    public void suffocate(float amount)
    {
        suffocating -= amount;
    }

    public void clearSuffocate()
    {
        suffocating = 0;
    }

    public float getSuffocating()
    {
        return suffocating;
    }

    public QiLungs copy(Lungs lungCopy)
    {
        QiLungs newLung = new QiLungs();

        for (int i = 0; i < getLungAmount(); i++)
            newLung.setLung(i, getConnection(i).getLung());

        newLung.lung[0].getLung().setCurrent(lungCopy.getCurrent(lung[0].getLung().getBreath()) / 2);
        newLung.breathingColor = lungCopy.breathingColor;

        if (lungCopy instanceof QiLungs)
            newLung.suffocating = ((QiLungs) lungCopy).suffocating;

        return newLung;
    }
}
