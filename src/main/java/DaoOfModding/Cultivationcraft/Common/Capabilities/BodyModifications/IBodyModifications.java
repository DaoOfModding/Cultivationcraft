package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;

import java.util.HashMap;

public interface IBodyModifications
{
    public String getSelection();
    public void setSelection(String selection);

    public boolean hasModification(String limb);
    public BodyPart getModification(String limb);
    public void setModification(BodyPart part);
    public HashMap<String, BodyPart> getModifications();

    public boolean hasUpdated();
    public void setUpdated(boolean updated);
}
