package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class BodyModificationsStorage implements Capability.IStorage<IBodyModifications>
{
    @Override
    public INBT writeNBT(Capability<IBodyModifications> capability, IBodyModifications instance, Direction side)
    {
        return instance.write();
    }

    @Override
    public void readNBT(Capability<IBodyModifications> capability, IBodyModifications instance, Direction side, INBT nbt)
    {
        if (!(instance instanceof IBodyModifications))
            throw new IllegalArgumentException("Tried to read Body Modifications from non BodyModifications instance");

        instance.read((CompoundNBT)nbt);
    }
}
