package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class DivineSenseTechnique extends Technique
{
    public DivineSenseTechnique()
    {
        name = "Divine Sense";
        elementID = Elements.noElementID;

        icon = new ResourceLocation("cultivationcraft", "textures/techniques/icons/divinesense.png");
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

    @Override
    public void renderPlayerView()
    {

    }
}
