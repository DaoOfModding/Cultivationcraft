package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.nbt.CompoundTag;

public interface ICultivatorTechniques
{
    public Technique getTechnique(int slot);
    public void setTechnique(int slot, Technique tech);

    public boolean techniqueExists(Technique exist);

    public CompoundTag writeNBT();
    public void readNBT(CompoundTag nbt);
}
