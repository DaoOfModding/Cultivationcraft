package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath.Breath;
import net.minecraft.world.entity.player.Player;

public class QiLung extends Lung
{
    public QiLung()
    {
        capacity = 300;
    }

    // Tries to breath, returns how much breath has been used
    public float breath(float amount, Breath breath, Player player)
    {
        if (!canBreath(breath))
        {
            if (current < amount)
            {
                float remaining = current;
                current = 0;

                return remaining;
            }
            else
            {
                current -= amount;

                return amount;
            }
        }

        // Fill up lungs twice as fast as they empty
        current += amount * 2;

        if (current > capacity)
            current = capacity;

        return amount;
    }
}
