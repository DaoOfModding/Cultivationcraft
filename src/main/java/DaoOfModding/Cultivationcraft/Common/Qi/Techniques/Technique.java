package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class Technique
{
    private final ResourceLocation icon;
    private final String name;

    private int elementID;
    private boolean active = false;


    public Technique()
    {
        name = "Example name";
        elementID = Elements.noElementID;

        icon = new ResourceLocation("cultivationcraft", "textures/techniques/icons/example.png");
    }

    public int getElementID()
    {
        return elementID;
    }

    // Returns whether this technique is currently active or not
    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean isActive)
    {
        active = isActive;
    }

    // Returns the icon for this technique
    public ResourceLocation getIcon()
    {
        return icon;
    }

    // Returns the name of this technique
    public String getName()
    {
        return name;
    }

    // Write a techniques data to NBT
    public CompoundNBT writeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putBoolean("active", active);

        return nbt;
    }

    public void writeBuffer(PacketBuffer buffer)
    {
        buffer.writeBoolean(active);
    }

    // Read a Technique stored in NBT and create a technique from it
    public static Technique readNBT(CompoundNBT nbt)
    {
        Technique newTech = new Technique();

        newTech.setActive(nbt.getBoolean("active"));

        return newTech;
    }

    public static Technique readBuffer(PacketBuffer buffer)
    {
        Technique newTech = new Technique();

        newTech.setActive(buffer.readBoolean());

        return newTech;
    }

    public void render() {}
}
