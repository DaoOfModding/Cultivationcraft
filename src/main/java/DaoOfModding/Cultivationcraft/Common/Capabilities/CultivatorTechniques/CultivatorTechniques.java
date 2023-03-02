package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques;

import DaoOfModding.Cultivationcraft.Common.Qi.TechniqueControl;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.PassiveTechniques.PassiveTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class CultivatorTechniques implements ICultivatorTechniques
{
    public static final int numberOfTechniques = 9;

    protected Technique[] techniques = new Technique[numberOfTechniques];

    protected ArrayList<PassiveTechnique> passives = new ArrayList<PassiveTechnique>();

    public Technique getTechnique(int slot)
    {
        return techniques[slot];
    }

    public void setTechnique(int slot, Technique tech)
    {
        techniques[slot] = tech;
    }

    public void determinePassives(Player player)
    {
        passives.clear();
        ArrayList<Class> passiveList = TechniqueControl.getPassiveTechniques(player);

        for (Class pTech : passiveList)
        {
            try
            {
                PassiveTechnique passive = (PassiveTechnique)pTech.newInstance();

                passives.add(passive);
            }
            catch (Exception e)
            {
                Cultivationcraft.LOGGER.error(pTech.getName() + " is not a Passive Technique: " + e.getMessage());
            }
        }
    }

    public ArrayList<PassiveTechnique> getPassives()
    {
        return passives;
    }

    public boolean techniqueExists(Technique exist)
    {
        for (Technique tech : techniques)
            if (tech != null && tech.getClass() == exist.getClass())
                return true;

        return false;
    }

    public Technique getTechniqueByName(String name)
    {
        for (int i = 0; i < numberOfTechniques; i++)
            if (techniques[i] != null)
                if (techniques[i].isCalled(name))
                    return techniques[i];

        for (PassiveTechnique passive : passives)
            if (passive.isCalled(name))
                return passive;

        return null;
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
