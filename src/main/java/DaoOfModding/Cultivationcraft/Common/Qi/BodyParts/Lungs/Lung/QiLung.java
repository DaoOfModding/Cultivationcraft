package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Breath;
import net.minecraft.world.entity.player.Player;

public class QiLung extends Lung
{
    // Tries to breath, returns how much breath is remaining to be used
    public float breath(float amount, Breath breath, Player player)
    {
        if (canBreath != breath)
        {
            if (current < amount)
            {
                float remaining = amount - current;
                current = 0;

                return remaining;
            }
            else
            {
                current -= amount;

                return 0;
            }
        }

        current += amount;

        if (current > capacity)
            current = capacity;

        return 0;
    }
}
