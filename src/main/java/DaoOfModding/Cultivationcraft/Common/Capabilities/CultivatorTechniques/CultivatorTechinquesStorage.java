package DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class CultivatorTechinquesStorage implements Capability.IStorage<ICultivatorTechniques>
{

    @Override
    public INBT writeNBT(Capability<ICultivatorTechniques> capability, ICultivatorTechniques instance, Direction side)
    {

        CompoundNBT nbt = new CompoundNBT();

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
        {
            Technique sending = instance.getTechnique(i);

            if (sending != null)
                nbt.put(Integer.toString(i), sending.writeNBT());
        }

        return nbt;
    }

    @Override
    public void readNBT(Capability<ICultivatorTechniques> capability, ICultivatorTechniques instance, Direction side, INBT nbt)
    {
        if (!(instance instanceof ICultivatorTechniques))
            throw new IllegalArgumentException("Tried to read Cultivator Techniques from non CultivatorTechniques instance");

        for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
            if (((CompoundNBT)nbt).contains(Integer.toString(i)))
                instance.setTechnique(i, Technique.readNBT(((CompoundNBT)nbt).getCompound(Integer.toString(i))));
    }
}
