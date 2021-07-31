package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.entity.player.PlayerEntity;

public class CultivatorTechniques implements ICultivatorTechniques
{
    public static final int numberOfTechniques = 9;

    private Technique[] techniques = new Technique[numberOfTechniques];

    public Technique getTechnique(int slot)
    {
        return techniques[slot];
    }

    public void setTechnique(int slot, Technique tech)
    {
        techniques[slot] = tech;
    }

    public boolean techniqueExists(Technique exist)
    {
        for (Technique tech : techniques)
            if (tech != null && tech.getClass() == exist.getClass())
                return true;

        return false;
    }

    // Return a specified players CultivatorTechniques
    public static ICultivatorTechniques getCultivatorTechniques(PlayerEntity player)
    {
        return player.getCapability(CultivatorTechniquesCapability.CULTIVATOR_TECHINQUES_CAPABILITY_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("getting cultivator techniques"));
    }
}
