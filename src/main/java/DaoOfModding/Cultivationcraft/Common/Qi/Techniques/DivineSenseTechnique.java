package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.Renderer;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class DivineSenseTechnique extends Technique
{
    public DivineSenseTechnique()
    {
        name = new TranslationTextComponent("cultivationcraft.technique.divinesense").getString();
        elementID = Elements.noElementID;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/divinesense.png");
        setOverlay(new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/overlays/divinesense.png"));
    }

    @Override
    public CompoundNBT writeNBT()
    {
        CompoundNBT nbt = super.writeNBT();

        return nbt;
    }

    @Override
    public void useKeyPressed(boolean keyDown)
    {
        // Toggle skill when key pressed
        if (!keyDown)
        {
            active = !active;
            Renderer.QiSourcesVisable = active;
        }
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        return true;
    }

    @Override
    public boolean allowMultiple()
    {
        return false;
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
