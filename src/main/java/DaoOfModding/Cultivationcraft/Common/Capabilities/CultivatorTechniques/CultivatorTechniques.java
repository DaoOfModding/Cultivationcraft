package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStatsCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.entity.player.PlayerEntity;

public class CultivatorTechniques implements ICultivatorTechniques
{
    private Technique[] techniques = new Technique[10];

    public Technique getTechnique(int slot)
    {
        return techniques[slot];
    }

    public void setTechnique(int slot, Technique tech)
    {
        techniques[slot] = tech;
    }


    // Return a specified players CultivatorTechniques
    public static ICultivatorTechniques getCultivatorTechniques(PlayerEntity player) {
        return player.getCapability(CultivatorTechniquesCapability.CULTIVATOR_TECHINQUES_CAPABILITY_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("getting cultivator techniques"));
    }
}
