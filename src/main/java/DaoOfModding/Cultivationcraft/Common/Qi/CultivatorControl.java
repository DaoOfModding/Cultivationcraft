package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.MovementOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.entity.player.PlayerEntity;

public class CultivatorControl
{
    // Returns the first active attack override for the specified player
    public static int getAttackOverride(PlayerEntity player)
    {
        // Get all cultivator techniques and check if any of them are active attack overrides
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        for (int i = 0; i < 9; i ++)
        {
            Technique testTech = techs.getTechnique(i);

            // If this technique is an active attack override then attack with it and cancel the default attack
            if (testTech != null && testTech.isActive() && testTech instanceof AttackOverrideTechnique)
                return i;
        }

        return -1;
    }

    // Returns the first active movement override for the specified player
    public static int getMovementOverride(PlayerEntity player)
    {
        // Get all cultivator techniques and check if any of them are active attack overrides
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        for (int i = 0; i < 9; i ++)
        {
            Technique testTech = techs.getTechnique(i);

            // If this technique is an active attack override then attack with it and cancel the default attack
            if (testTech != null && testTech.isActive() && testTech instanceof MovementOverrideTechnique)
                return i;
        }

        return -1;
    }
}
