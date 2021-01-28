package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Map;

public class BodyModifications implements IBodyModifications
{
    String selected = "";
    HashMap<String, BodyPart> modifications = new HashMap<String, BodyPart>();
    HashMap<String, HashMap<String, BodyPartOption>> options = new HashMap<String, HashMap<String, BodyPartOption>>();
    boolean hasUpdated = false;
    int progress = 0;

    public String getSelection()
    {
        return selected;
    }

    public void setSelection(String selection)
    {
        selected = selection;
    }

    public void setProgress(int number)
    {
        progress = number;
    }

    public void addProgress(int number)
    {
        progress += number;
    }

    public int getProgress()
    {
        return progress;
    }

    // Does this player have a modification for the supplied limb?
    public boolean hasModification(String limb)
    {
        return modifications.containsKey(limb);
    }

    // Does this player have the specified modification for this limb?
    public boolean hasModification(String limb, String modificationID)
    {
        if (modifications.containsKey(limb))
            if (modifications.get(limb).getID().compareTo(modificationID) == 0)
                return true;

        return false;
    }

    public boolean hasOption(String limb, String subPosition)
    {
        if (options.containsKey(limb))
            if (options.get(limb).containsKey(subPosition))
                return true;

        return false;
    }


    public boolean hasOption(String limb, String subPosition, String modificationID)
    {
        if (options.containsKey(limb))
            if (options.get(limb).containsKey(subPosition))
                if (options.get(limb).get(subPosition).getID().compareTo(modificationID) == 0)
                    return true;

        return false;
    }

    public BodyPart getModification(String limb)
    {
        return modifications.get(limb);
    }

    public BodyPartOption getOption(String limb, String subPosition)
    {
        return options.get(limb).get(subPosition);
    }

    public void setModification(BodyPart part)
    {
        modifications.put(part.getPosition(), part);
        setUpdated(false);
    }

    public void setOption(BodyPartOption option)
    {
        if (!options.containsKey(option.getPosition()))
            options.put(option.getPosition(), new HashMap<String, BodyPartOption>());

        options.get(option.getPosition()).put(option.getSubPosition(), option);
    }

    public HashMap<String, BodyPart> getModifications()
    {
        return modifications;
    }

    public HashMap<String, BodyPartOption> getModificationOptions(String limb)
    {
        if (!options.containsKey(limb))
            options.put(limb, new HashMap<String, BodyPartOption>());

        return options.get(limb);
    }

    public HashMap<String, HashMap<String, BodyPartOption>> getModificationOptions()
    {
        return options;
    }


    public boolean hasUpdated()
    {
        return hasUpdated;
    }

    public void setUpdated(boolean updated)
    {
        hasUpdated = updated;
    }


    public void read(CompoundNBT NBT)
    {
        setSelection(NBT.getString("selection"));
        setProgress(NBT.getInt("progress"));

        CompoundNBT modifications = NBT.getCompound("modifications");

        for (String limb : modifications.keySet())
            setModification(BodyPartNames.getPart(modifications.getString(limb)));

        CompoundNBT options = NBT.getCompound("options");

        for (String limb : options.keySet())
        {
            CompoundNBT option = options.getCompound(limb);

            for (String part : option.keySet())
                setOption(BodyPartNames.getOption(option.getString(part)));
        }

        setUpdated(false);
    }

    public CompoundNBT write()
    {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putString("selection", getSelection());
        nbt.putInt("progress", getProgress());

        CompoundNBT modifications = new CompoundNBT();
        for(Map.Entry<String, BodyPart> entry : getModifications().entrySet())
            modifications.putString(entry.getKey(), entry.getValue().getID());

        nbt.put("modifications", modifications);

        CompoundNBT options = new CompoundNBT();
        for(Map.Entry<String, HashMap<String, BodyPartOption>> entry : getModificationOptions().entrySet())
        {
            CompoundNBT subOptions = new CompoundNBT();

            for(Map.Entry<String, BodyPartOption> entry2 : entry.getValue().entrySet())
                subOptions.putString(entry.getKey(), entry2.getValue().getID());

            options.put(entry.getKey(), subOptions);
        }
        nbt.put("options", options);

        return nbt;
    }

    public void copy(IBodyModifications mod)
    {
        setSelection(mod.getSelection());
        modifications = mod.getModifications();
        options = mod.getModificationOptions();
        progress = mod.getProgress();

        setUpdated(false);
    }

    // Return a specified players BodyModifications
    public static IBodyModifications getBodyModifications(PlayerEntity player)
    {
        return player.getCapability(BodyModificationsCapability.BODY_MODIFICATIONS_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("getting player body modifications"));
    }
}
