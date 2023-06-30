package DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyForgeParts;

import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.BodyModifications;
import DaoOfModding.Cultivationcraft.Common.Capabilities.BodyModifications.IBodyModifications;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartNames;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.BodyPartOption;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lung.Lung;
import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Lungs.Lungs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class LungPart extends BodyPartOption
{
    Lungs lungType = new Lungs();
    HashMap<ResourceLocation, Lung> lungs = new HashMap<ResourceLocation, Lung>();
    ResourceLocation location = null;

    public LungPart(String partID, String position, String subPosition, String displayNamePos)
    {
        super(partID, position, subPosition, displayNamePos);
    }

    public void setLungType(Lungs newLung)
    {
        lungType = newLung;
    }

    public Lungs getLungType()
    {
        return lungType;
    }

    public void setLung(ResourceLocation location, Lung lung)
    {
        lungs.put(location, lung);
    }

    public Lung getLung(ResourceLocation location)
    {
        if (lungs.containsKey(location))
            return lungs.get(location);

        return null;
    }

    public void setNeededLungLocation(ResourceLocation newLocation)
    {
        location = newLocation;
    }

    @Override
    public boolean canBeForged(Player player)
    {
        if (!super.canBeForged(player))
            return false;

        if (location == null)
            return true;

        IBodyModifications modifications = BodyModifications.getBodyModifications(player);

        if (!modifications.hasOption(BodyPartNames.bodyPosition, BodyPartNames.lungSubPosition))
            return false;

        if (!((LungPart) modifications.getOption(BodyPartNames.bodyPosition, BodyPartNames.lungSubPosition)).lungType.hasConnectionAt(location))
            return false;

        return true;
    }
}
