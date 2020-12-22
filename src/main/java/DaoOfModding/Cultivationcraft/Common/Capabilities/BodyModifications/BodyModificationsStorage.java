package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Map;

public class BodyModificationsStorage implements Capability.IStorage<IBodyModifications>
{
    @Override
    public INBT writeNBT(Capability<IBodyModifications> capability, IBodyModifications instance, Direction side)
    {
        CompoundNBT nbt = new CompoundNBT();

        for(Map.Entry<String, BodyPart> entry : instance.getModifications().entrySet())
            nbt.put(entry.getKey(), entry.getValue().write());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IBodyModifications> capability, IBodyModifications instance, Direction side, INBT nbt)
    {
        if (!(instance instanceof IBodyModifications))
            throw new IllegalArgumentException("Tried to read Body Modifications from non BodyModifications instance");

        CompoundNBT NBT = (CompoundNBT)nbt;

        for (String limb : NBT.keySet())
            instance.setModification(BodyPart.read(NBT.getCompound(limb)));

        instance.setUpdated(false);
    }
}
