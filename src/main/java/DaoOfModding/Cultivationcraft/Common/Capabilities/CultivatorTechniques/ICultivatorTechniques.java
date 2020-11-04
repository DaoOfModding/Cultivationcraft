package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;

public interface ICultivatorTechniques
{
    public Technique getTechnique(int slot);
    public void setTechnique(int slot, Technique tech);
}
