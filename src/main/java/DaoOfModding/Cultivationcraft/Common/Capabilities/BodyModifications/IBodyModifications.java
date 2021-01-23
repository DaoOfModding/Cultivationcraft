package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;

public interface IBodyModifications
{
    public String getSelection();
    public void setSelection(String selection);

    public void setProgress(int number);
    public void addProgress(int number);
    public int getProgress();

    public boolean hasModification(String limb);
    public boolean hasOption(String limb, String subPosition);
    public BodyPart getModification(String limb);
    public BodyPartOption getOption(String limb, String subPosition);
    public void setModification(BodyPart part);
    public void setOption(BodyPartOption option);
    public HashMap<String, BodyPart> getModifications();
    public HashMap<String, BodyPartOption> getModificationOptions(String limb);
    public HashMap<String, HashMap<String, BodyPartOption>> getModificationOptions();

    public void read(CompoundNBT NBT);
    public CompoundNBT write();

    public void copy(IBodyModifications mod);

    public boolean hasUpdated();
    public void setUpdated(boolean updated);
}
