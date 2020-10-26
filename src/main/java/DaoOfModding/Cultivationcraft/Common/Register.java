package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Common.Capabilities.ChunkQiSources.ChunkQiSourcesCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStatsCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordBind.FlyingSwordBindCapability;
import DaoOfModding.Cultivationcraft.Common.Capabilities.FlyingSwordContainerItemStack.FlyingSwordContainerItemStackCapability;
import DaoOfModding.Cultivationcraft.Common.Containers.FlyingSwordContainer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Register
{
    public enum keyPresses { FLYINGSWORDSCREEN }

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, "cultivationcraft");

    public static RegistryObject<EntityType<FlyingSwordEntity>> FLYINGSWORD = ENTITY_TYPES.register("flyingsword", () ->
                                                                                EntityType.Builder.<FlyingSwordEntity>create(FlyingSwordEntity::new, EntityClassification.MISC)
                                                                                        .size(0.5f, 0.5f)
                                                                                        .setUpdateInterval(3)
                                                                                        .build("flyingsword"));

    public static ContainerType<FlyingSwordContainer> ContainerTypeFlyingSword;

    public static void registerCapabilities()
    {
        CultivatorStatsCapability.register();
        FlyingSwordContainerItemStackCapability.register();
        FlyingSwordBindCapability.register();
        ChunkQiSourcesCapability.register();
    }
}
