package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.QiLung;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.PlayerHealthManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

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
        breathingColor = breathing.getColor();

        int Respiration = EnchantmentHelper.getRespiration(player);
        Float amountRemaining = 10.0f / (Respiration + 1);

        if (!canBreath(breathing))
        {
            float toTry = amountRemaining / lung.length;

            // Go through each lung trying to breath the amount remaining equally
            for (int i = 0; i < lung.length; i++)
                amountRemaining -= lung[i].getLung().breath(toTry, breathing, player);

            // If there is still breath left, go through each lung again and try to use all of the breath
            if (amountRemaining > 0)
                for (int i = 0; i < lung.length; i++)
                    amountRemaining -= lung[i].getLung().breath(amountRemaining, breathing, player);
        }
        else
        {
            float amount = 0;

            for (int i = 0; i < lung.length; i++)
                if (lung[i].getLung().canBreath(breathing))
                    amount++;

            float toTry = amountRemaining / amount;

            // Go through each lung that can breath trying to breath the amount remaining equally
            for (int i = 0; i < lung.length; i++)
                if (lung[i].getLung().canBreath(breathing))
                    amountRemaining -= lung[i].getLung().breath(toTry, breathing, player);

            // If there is still breath left, go through each lung again and try to use all of the breath
            if (amountRemaining > 0)
                for (int i = 0; i < lung.length; i++)
                    if (lung[i].getLung().canBreath(breathing))
                        amountRemaining -= lung[i].getLung().breath(amountRemaining, breathing, player);
        }

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

    public QiLungs copy(Player player)
    {
        Lungs lungCopy = PlayerHealthManager.getLungs(player);
        QiLungs newLung = new QiLungs();

        for (int i = 0; i < getLungAmount(); i++)
        {
            newLung.setLung(i, getConnection(i).getLung());
            newLung.getConnection(i).calculateCapacity(player);
        }

        newLung.lung[0].getLung().setCurrent(lungCopy.getCurrent(lung[0].getLung().getBreath()) / 2);
        newLung.breathingColor = lungCopy.breathingColor;

        if (lungCopy instanceof QiLungs)
            newLung.suffocating = ((QiLungs) lungCopy).suffocating;

        return newLung;
    }
}
