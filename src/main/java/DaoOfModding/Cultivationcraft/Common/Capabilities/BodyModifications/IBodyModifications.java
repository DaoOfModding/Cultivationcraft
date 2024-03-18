package DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications;

import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPart;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.ArrayList;
import java.util.HashMap;

@AutoRegisterCapability
public interface IBodyModifications
{
    public void setHealth(float hp);
    public float getHealth();

    public String getSelection();
    public void setSelection(String selection);

    public String getLastForged();
    public void setLastForged(String last);
    public double getQuestProgress();
    public void setQuestProgress(double newProgress);

    public void setProgress(int number);
    public void addProgress(int number);
    public int getProgress();

    public boolean hasModification(String limb);
    public boolean hasModification(String limb, String modificationID);
    public boolean hasOption(String limb, String subPosition);
    public boolean hasOption(String limb, String subPosition, String modificationID);
    public BodyPart getModification(String limb);
    public BodyPartOption getOption(String limb, String subPosition);
    public ArrayList<BodyPart> getBodyPartsOfType(Class type);
    public void setModification(BodyPart part);
    public void setOption(BodyPartOption option);
    public HashMap<String, BodyPart> getModifications();
    public HashMap<String, BodyPartOption> getModificationOptions(String limb);
    public HashMap<String, HashMap<String, BodyPartOption>> getModificationOptions();
    public ArrayList<String> getTags();
    public void removeModification(BodyPart part);
    public void removeOption(BodyPartOption option);

    public void clearModifications();

    public void read(CompoundTag NBT);
    public CompoundTag write();

    public void copy(IBodyModifications mod);

    public boolean hasUpdated();
    public void setUpdated(boolean updated);
}
