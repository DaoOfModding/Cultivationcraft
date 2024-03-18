package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts.StomachPart;
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

    String lastForged = "";
    double questProgress = 0;
    float health = 0;

    public void setHealth(float hp)
    {
        health = hp;
    }

    public float getHealth()
    {
        return health;
    }

    public String getSelection()
    {
        return selected;
    }

    public void setSelection(String selection)
    {
        selected = selection;

        // If nothing is selected reset progress
        if (selection.compareTo("") == 0)
            setProgress(0);
    }

    public String getLastForged()
    {
        return lastForged;
    }

    public void setLastForged(String last)
    {
        lastForged = last;
    }

    public double getQuestProgress()
    {
        return questProgress;
    }

    public void setQuestProgress(double newProgress)
    {
        questProgress = newProgress;
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

    public ArrayList<BodyPart> getBodyPartsOfType(Class type)
    {
        ArrayList<BodyPart> parts = new ArrayList<>();

        for (BodyPart part : modifications.values())
            if (part.getClass() == type)
                parts.add(part);

        for (HashMap<String, BodyPartOption> optionSearch : options.values())
            for (BodyPart part : optionSearch.values())
                if (part.getClass() == type)
                    parts.add(part);

        return parts;
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
        setUpdated(false);
    }

    public void removeModification(BodyPart part)
    {
        if (!hasModification(part.getPosition(), part.getID()))
            return;

        modifications.remove(part.getPosition(), part);
        removeTags(part);
        setUpdated(false);
    }

    public void removeOption(BodyPartOption option)
    {
        if (!options.containsKey(option.getPosition()))
            return;

        if (!hasOption(option.getPosition(), option.getSubPosition(), option.getID()))
            return;

        options.get(option.getPosition()).remove(option.getSubPosition(), option);
        removeTags(option);
        setUpdated(false);
    }

    protected void addTags(BodyPart part)
    {
        for (String tag : part.getUniqueTags())
            tags.add(tag);
    }

    protected void removeTags(BodyPart part)
    {
        for (String tag : part.getUniqueTags())
            tags.remove(tag);
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

    public void clearModifications()
    {
        modifications.clear();
        options.clear();
        tags.clear();

        progress = 0;
        lastForged = "";
        selected = "";

        setUpdated(false);
    }

    public void read(CompoundTag NBT)
    {
        clearModifications();

        setHealth(NBT.getFloat("health"));
        setSelection(NBT.getString("selection"));
        setLastForged(NBT.getString("last"));
        setProgress(NBT.getInt("progress"));
        setQuestProgress(NBT.getDouble("questprogress"));

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

        nbt.putFloat("health", health);
        nbt.putString("selection", getSelection());
        nbt.putString("last", getLastForged());
        nbt.putInt("progress", getProgress());
        nbt.putDouble("questprogress", getQuestProgress());

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

        lastForged = mod.getLastForged();
        progress = mod.getProgress();
        questProgress = mod.getQuestProgress();
        health = mod.getHealth();

        setUpdated(false);
    }

    // Return a specified players BodyModifications
    public static IBodyModifications getBodyModifications(Player player)
    {
        return player.getCapability(BodyModificationsCapability.INSTANCE).orElseThrow(() -> new IllegalArgumentException("getting player body modifications"));
    }
}
