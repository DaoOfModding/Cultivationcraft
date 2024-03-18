package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public interface ICultivatorTechniques
{
    public Technique getTechnique(int slot);
    public void setTechnique(int slot, Technique tech);

    public boolean techniqueExists(Technique exist);

    public CompoundTag writeNBT();
    public void readNBT(CompoundTag nbt);

    public boolean canBreath(Player player);

    public void determinePassives(Player player);
    public ArrayList<PassiveTechnique> getPassives();

    public Technique getTechniqueByName(String name);
}
