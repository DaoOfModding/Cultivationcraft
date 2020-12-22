package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;

public class BodyPart
{
    ArrayList<String> modelIDs = new ArrayList<String>();
    String limbPosition;

    public BodyPart(ArrayList<String> IDs, String position)
    {
        modelIDs = (ArrayList<String>)IDs.clone();
        limbPosition = position;
    }

    public String getPosition()
    {
        return limbPosition;
    }

    public ArrayList<String> getModelIDs()
    {
        return modelIDs;
    }

    public CompoundNBT write()
    {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putString("limbPosition", limbPosition);

        int i = 0;

        for (String ID : modelIDs)
        {
            nbt.putString("modelID" + i, ID);
            i++;
        }

        return nbt;
    }

    public static BodyPart read(CompoundNBT nbt)
    {
        ArrayList<String> modelIDs = new ArrayList<String>();

        int i = 0;
        while (nbt.contains("modelID" + i))
        {
            modelIDs.add(nbt.getString("modelID" + i));
            i++;
        }

        BodyPart newPart = new BodyPart(modelIDs, nbt.getString("limbPosition"));

        return newPart;
    }
}
