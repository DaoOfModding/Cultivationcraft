package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;

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
        if (tech != null)
            tech.setSlot(slot);

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
    public static ICultivatorTechniques getCultivatorTechniques(Player player)
    {
        return player.getCapability(CultivatorTechniquesCapability.INSTANCE).orElseThrow(() -> new IllegalArgumentException("getting cultivator techniques"));
    }

    public CompoundTag writeNBT()
    {
        CompoundTag nbt = new CompoundTag();

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
        {
            Technique sending = getTechnique(i);

            if (sending != null)
                nbt.put(Integer.toString(i), sending.writeNBT());
        }

        return nbt;
    }

    public void readNBT(CompoundTag nbt)
    {
        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (nbt.contains(Integer.toString(i)))
                setTechnique(i, Technique.readNBT(nbt.getCompound(Integer.toString(i))));
    }
}
