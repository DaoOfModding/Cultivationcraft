package DaoOfModding.Cultivationcraft.Common.Qi.Techniques;

import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericLimbNames;
import DaoOfModding.Cultivationcraft.Client.AnimationFramework.GenericPoses;
import DaoOfModding.Cultivationcraft.Client.Animations.GenericQiPoses;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Freeze;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;

public class IceAuraTechnique extends AuraTechnique
{
    // TODO: Temp
    protected int power = 20;

    public IceAuraTechnique()
    {
        super();

        name = "Ice Aura";
        elementID = Elements.iceElementID;

        icon = new ResourceLocation(Cultivationcraft.MODID, "textures/techniques/icons/iceaura.png");


        pose = GenericQiPoses.HandsBehind.clone();

        pose.addAngle(GenericLimbNames.leftLeg, new Vector3d(Math.toRadians(-90), 0, Math.toRadians(0)), GenericPoses.walkLegPriority + 2);
        pose.addAngle(GenericLimbNames.rightLeg, new Vector3d(Math.toRadians(-90), 0, Math.toRadians(0)), GenericPoses.walkLegPriority + 2);
        pose.addAngle(GenericLimbNames.lowerLeftLeg, new Vector3d(Math.toRadians(90), 0, Math.toRadians(0)), GenericPoses.walkLegPriority + 2);
        pose.addAngle(GenericLimbNames.lowerRightLeg, new Vector3d(Math.toRadians(90), 0, Math.toRadians(0)), GenericPoses.walkLegPriority + 2);
    }

    public void tickServer(TickEvent.PlayerTickEvent event)
    {
        Freeze.FreezeArea(event.player.getEntityWorld(), event.player.getPosition(), getRange(), power);
    }

    @Override
    public boolean isValid(PlayerEntity player)
    {
        return true;
    }
}
