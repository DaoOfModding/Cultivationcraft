package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class DivineSenseTechnique extends Technique
{
    public DivineSenseTechnique()
    {
        super();
    }

    @Override
    public CompoundNBT writeNBT()
    {
        CompoundNBT nbt = super.writeNBT();

        return nbt;
    }

    @Override
    public void readNBTData(CompoundNBT nbt)
    {
        super.readNBTData(nbt);
    }

    @Override
    public void writeBuffer(PacketBuffer buffer)
    {
        super.writeBuffer(buffer);
    }

    @Override
    public void readBufferData(PacketBuffer buffer)
    {
        super.readBufferData(buffer);
    }
}
