package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.QiLung;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class QiLungs extends Lungs
{
    protected float suffocating = 0;

    public QiLungs()
    {
        lung = new QiLung();
    }

    public void breath(Player player)
    {
        // Disable vanilla breathing
        player.setAirSupply(40);

        Breath breathing = BreathingHandler.getBreath(player);
        breathingColor = breathing.color;

        float amountRemaining = lung.breath(10, breathing, player);

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

        newLung.lung.setCurrent(lungCopy.getCurrent(lung.getBreath()) / 2);
        newLung.breathingColor = lungCopy.breathingColor;

        if (lungCopy instanceof QiLungs)
            newLung.suffocating = ((QiLungs) lungCopy).suffocating;

        return newLung;
    }
}
