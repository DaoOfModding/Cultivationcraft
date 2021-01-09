package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts;

import DaoOfModding.Cultivationcraft.Client.Animations.BodyPartNames;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;

public class BodyPart
{
    ArrayList<String> modelIDs = new ArrayList<String>();
    String limbPosition;

    public BodyPart(ArrayList<String> IDs)
    {
        modelIDs = (ArrayList<String>)IDs.clone();

        // Set the limbPosition to be equal to the position of the FIRST modelID
        limbPosition = BodyPartNames.getPartPosition(IDs.get(0));
    }

    public String getPosition()
    {
        return limbPosition;
    }

    public ArrayList<String> getModelIDs()
    {
        return modelIDs;
    }

    // TODO: This
    public boolean canBeForged(PlayerEntity player)
    {
        return true;
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

        BodyPart newPart = new BodyPart(modelIDs);

        return newPart;
    }
}
