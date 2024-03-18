package DaoOfModding.Cultivationcraft.Client.Animations;

import DaoOfModding.Cultivationcraft.Client.Textures.TextureList;
import DaoOfModding.mlmanimator.Client.Models.ExtendableModelRenderer;
import DaoOfModding.mlmanimator.Client.Models.GenericTextureValues;
import net.minecraft.client.model.geom.builders.UVPair;

public class GenericCultivatorTextureValues
{
    public static void addGenericBodyLayers(ExtendableModelRenderer body)
    {
        addGenericBodyLayers(body, 0, 0);
    }

    public static void addGenericBodyLayers(ExtendableModelRenderer body, int u, int v)
    {
        GenericTextureValues.addGenericBodyLayers(body, u, v);
        addCultivatorBodyLayers(body, u, v);
    }

    public static void addCultivatorBodyLayers(ExtendableModelRenderer body, int u, int v)
    {
        body.addLayer(new UVPair(GenericTextureValues.chest.u() + (float)u, GenericTextureValues.chest.v() + (float)v), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 6);
        body.addLayer(new UVPair(GenericTextureValues.jacket.u() + (float)u, GenericTextureValues.jacket.v() + (float)v), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 6);
    }

    public static void addGenericHeadLayers(ExtendableModelRenderer head)
    {
        addGenericHeadLayers(head, 0, 0);
    }

    public static void addGenericHeadLayers(ExtendableModelRenderer head, int u, int v)
    {
        GenericTextureValues.addGenericHeadLayers(head, u, v);
        addCultivatorHeadLayers(head, u, v);
    }

    public static void addCultivatorHeadLayers(ExtendableModelRenderer head, int u, int v)
    {
        head.addLayer(new UVPair(GenericTextureValues.head.u() + u, GenericTextureValues.head.v() + v), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 6);
        head.addLayer(new UVPair(GenericTextureValues.hat.u() + u, GenericTextureValues.hat.v() + v), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 6);
    }

    public static void addGenericLeftArmLayers(ExtendableModelRenderer leftArm)
    {
        addGenericLeftArmLayers(leftArm, 0, 0);
    }

    public static void addGenericLeftArmLayers(ExtendableModelRenderer leftArm, int u, int v)
    {
        GenericTextureValues.addGenericLeftArmLayers(leftArm, u, v);
        addCultivatorLeftArmLayers(leftArm, u, v);
    }

    public static void addCultivatorLeftArmLayers(ExtendableModelRenderer leftArm, int u, int v)
    {
        leftArm.addLayer(new UVPair(GenericTextureValues.leftArm.u() + u, GenericTextureValues.leftArm.v() + v), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 6);
        leftArm.addLayer(new UVPair(GenericTextureValues.leftSleeve.u() + u, GenericTextureValues.leftSleeve.v() + v), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 6);
    }

    public static void addGenericRightArmLayers(ExtendableModelRenderer rightArm)
    {
        addGenericRightArmLayers(rightArm, 0, 0);
    }

    public static void addGenericRightArmLayers(ExtendableModelRenderer rightArm, int u, int v)
    {
        GenericTextureValues.addGenericRightArmLayers(rightArm, u, v);
        addCultivatorRightArmLayers(rightArm, u, v);
    }

    public static void addCultivatorRightArmLayers(ExtendableModelRenderer rightArm, int u, int v)
    {
        rightArm.addLayer(new UVPair(GenericTextureValues.rightArm.u() + u, GenericTextureValues.rightArm.v() + v), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 6);
        rightArm.addLayer(new UVPair(GenericTextureValues.rightSleeve.u() + u, GenericTextureValues.rightSleeve.v() + v), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 6);
    }

    public static void addGenericLeftLegLayers(ExtendableModelRenderer leftLeg)
    {
        addGenericLeftLegLayers(leftLeg, 0, 0);
    }

    public static void addGenericLeftLegLayers(ExtendableModelRenderer leftLeg, int u, int v)
    {
        GenericTextureValues.addGenericLeftLegLayers(leftLeg, u, v);
        addCultivatorLeftLegLayers(leftLeg, u, v);
    }

    public static void addCultivatorLeftLegLayers(ExtendableModelRenderer leftLeg, int u, int v)
    {
        leftLeg.addLayer(new UVPair(GenericTextureValues.leftLeg.u() + u, GenericTextureValues.leftLeg.v() + v), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 6);
        leftLeg.addLayer(new UVPair(GenericTextureValues.leftPants.u() + u, GenericTextureValues.leftPants.v() + v), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 6);
    }

    public static void addGenericRightLegLayers(ExtendableModelRenderer rightLeg)
    {
        addGenericRightLegLayers(rightLeg, 0, 0);
    }

    public static void addGenericRightLegLayers(ExtendableModelRenderer rightLeg, int u, int v)
    {
        GenericTextureValues.addGenericRightLegLayers(rightLeg, u, v);
        addCultivatorRightLegLayers(rightLeg, u, v);
    }

    public static void addCultivatorRightLegLayers(ExtendableModelRenderer rightLeg, int u, int v)
    {
        rightLeg.addLayer(new UVPair(GenericTextureValues.rightLeg.u() + u, GenericTextureValues.rightLeg.v() + v), GenericTextureValues.skin_Size, 0.0F, TextureList.skin, false, 6);
        rightLeg.addLayer(new UVPair(GenericTextureValues.rightPants.u() + u, GenericTextureValues.rightPants.v() + v), GenericTextureValues.skin_Size, 0.5F, TextureList.skin, false, 6);
    }
}
