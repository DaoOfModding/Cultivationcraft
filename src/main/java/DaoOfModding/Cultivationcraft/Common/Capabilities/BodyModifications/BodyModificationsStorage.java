package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartNames;
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

        nbt.putString("selection", instance.getSelection());
        nbt.putInt("progress", instance.getProgress());

        CompoundNBT modifications = new CompoundNBT();
        for(Map.Entry<String, BodyPart> entry : instance.getModifications().entrySet())
            modifications.putString(entry.getKey(), entry.getValue().getID());

        nbt.put("modifications", modifications);

        return nbt;
    }

    @Override
    public void readNBT(Capability<IBodyModifications> capability, IBodyModifications instance, Direction side, INBT nbt)
    {
        if (!(instance instanceof IBodyModifications))
            throw new IllegalArgumentException("Tried to read Body Modifications from non BodyModifications instance");

        CompoundNBT NBT = (CompoundNBT)nbt;

        instance.setSelection(NBT.getString("selection"));
        instance.setProgress(NBT.getInt("progress"));

        CompoundNBT modifications = NBT.getCompound("modifications");

        for (String limb : modifications.keySet())
            instance.setModification(BodyPartNames.getPart(modifications.getString(limb)));

        instance.setUpdated(false);
    }
}
