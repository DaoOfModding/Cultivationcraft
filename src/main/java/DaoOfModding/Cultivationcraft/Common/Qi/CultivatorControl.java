package DaoOfModding.Cultivationcraft.Common.Qi;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.MovementOverrideTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class CultivatorControl
{
    // Returns the first active attack override for the specified player
    public static int getAttackOverride(Player player)
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

    public static int getTechnique(Player player, Technique techSearch)
    {
        // Get all cultivator techniques and check if any of them are active attack overrides
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        for (int i = 0; i < 9; i ++)
        {
            Technique testTech = techs.getTechnique(i);

            // If this technique is an active attack override then attack with it and cancel the default attack
            if (testTech == techSearch)
                return i;
        }

        return -1;
    }

    // Returns the all active movement overrides for the specified player
    public static ArrayList<MovementOverrideTechnique> getMovementOverride(Player player)
    {
        ArrayList<MovementOverrideTechnique> overrides = new ArrayList<>();

        // Get all cultivator techniques and check if any of them are active attack overrides
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(player);

        for (int i = 0; i < 9; i ++)
        {
            Technique testTech = techs.getTechnique(i);

            // If this technique is an active attack override then attack with it and cancel the default attack
            if (testTech != null && testTech.isActive() && testTech instanceof MovementOverrideTechnique)
                overrides.add((MovementOverrideTechnique)testTech);
        }

        return overrides;
    }
}
