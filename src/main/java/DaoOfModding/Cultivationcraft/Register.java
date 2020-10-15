package DaoOfModding.Cultivationcraft;

import DaoOfModding.Cultivationcraft.Common.FlyingSwordEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Register
{
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, "cultivationcraft");

    public static RegistryObject<EntityType<FlyingSwordEntity>> FLYINGSWORD = ENTITY_TYPES.register("flyingsword", () ->
                                                                                EntityType.Builder.<FlyingSwordEntity>create(FlyingSwordEntity::new, EntityClassification.MISC)
                                                                                .build("flyingsword"));

}
