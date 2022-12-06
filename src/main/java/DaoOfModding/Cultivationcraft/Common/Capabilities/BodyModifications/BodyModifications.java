package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BodyModifications implements IBodyModifications
{
    String selected = "";
    HashMap<String, BodyPart> modifications = new HashMap<String, BodyPart>();
    HashMap<String, HashMap<String, BodyPartOption>> options = new HashMap<String, HashMap<String, BodyPartOption>>();
    ArrayList<String> tags = new ArrayList<String>();
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
        addTags(part);
        setUpdated(false);
    }

    public void setOption(BodyPartOption option)
    {
        if (!options.containsKey(option.getPosition()))
            options.put(option.getPosition(), new HashMap<String, BodyPartOption>());

        options.get(option.getPosition()).put(option.getSubPosition(), option);
        addTags(option);
    }

    protected void addTags(BodyPart part)
    {
        for (String tag : part.getUniqueTags())
            tags.add(tag);
    }

    public ArrayList<String> getTags()
    {
        return tags;
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

    private void clearModifications()
    {
        modifications.clear();
        options.clear();
    }

    public void read(CompoundTag NBT)
    {
        clearModifications();

        setSelection(NBT.getString("selection"));
        setProgress(NBT.getInt("progress"));

        CompoundTag modifications = NBT.getCompound("modifications");

        for (String limb : modifications.getAllKeys())
        {
            BodyPart part = BodyPartNames.getPart(modifications.getString(limb));
            setModification(part);
            addTags(part);
        }

        CompoundTag options = NBT.getCompound("options");

        for (String limb : options.getAllKeys())
        {
            CompoundTag option = options.getCompound(limb);

            for (String part : option.getAllKeys())
            {
                BodyPartOption optionPart = BodyPartNames.getOption(option.getString(part));
                setOption(optionPart);
                addTags(optionPart);
            }
        }

        setUpdated(false);
    }

    public CompoundTag write()
    {
        CompoundTag nbt = new CompoundTag();

        nbt.putString("selection", getSelection());
        nbt.putInt("progress", getProgress());

        CompoundTag modifications = new CompoundTag();
        for(Map.Entry<String, BodyPart> entry : getModifications().entrySet())
            modifications.putString(entry.getKey(), entry.getValue().getID());

        nbt.put("modifications", modifications);

        CompoundTag options = new CompoundTag();
        for(Map.Entry<String, HashMap<String, BodyPartOption>> entry : getModificationOptions().entrySet())
        {
            CompoundTag subOptions = new CompoundTag();

            for(Map.Entry<String, BodyPartOption> entry2 : entry.getValue().entrySet())
                subOptions.putString(entry2.getKey(), entry2.getValue().getID());

            options.put(entry.getKey(), subOptions);
        }
        nbt.put("options", options);

        return nbt;
    }

    public void copy(IBodyModifications mod)
    {
        setSelection(mod.getSelection());
        modifications = (HashMap<String, BodyPart>)mod.getModifications().clone();
        options = (HashMap<String, HashMap<String, BodyPartOption>>)mod.getModificationOptions().clone();
        tags = (ArrayList<String>)mod.getTags().clone();

        progress = mod.getProgress();

        setUpdated(false);
    }

    // Return a specified players BodyModifications
    public static IBodyModifications getBodyModifications(Player player)
    {
        return player.getCapability(BodyModificationsCapability.INSTANCE).orElseThrow(() -> new IllegalArgumentException("getting player body modifications"));
    }
}
