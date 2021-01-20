package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public class BodyModifications implements IBodyModifications
{
    String selected = "";
    HashMap<String, BodyPart> modifications = new HashMap<String, BodyPart>();
    boolean hasUpdated = false;


    public String getSelection()
    {
        return selected;
    }

    public void setSelection(String selection)
    {
        selected = selection;
    }

    // Does this player have a modification for the supplied limb?
    public boolean hasModification(String limb)
    {
        return modifications.containsKey(limb);
    }

    public BodyPart getModification(String limb)
    {
        return modifications.get(limb);
    }

    public void setModification(BodyPart part)
    {
        modifications.put(part.getPosition(), part);
        setUpdated(false);
    }

    public HashMap<String, BodyPart> getModifications()
    {
        return modifications;
    }

    public boolean hasUpdated()
    {
        return hasUpdated;
    }

    public void setUpdated(boolean updated)
    {
        hasUpdated = updated;
    }

    // Return a specified players BodyModifications
    public static IBodyModifications getBodyModifications(PlayerEntity player)
    {
        return player.getCapability(BodyModificationsCapability.BODY_MODIFICATIONS_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("getting player body modifications"));
    }
}
