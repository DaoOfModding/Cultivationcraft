package DaoOfModding.Cultivationcraft.Client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientBlockRegister
{
    // TODO: Redo this maybe?
    /*
    public static void registerBlockRenderers()
    {
        RenderTypeLookup.setRenderLayer(BlockRegister.frozenBlock, RenderType.translucent());
    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event)
    {
        for (BlockState blockState : BlockRegister.frozenBlock.getStateDefinition().getPossibleStates())
            event.getModelRegistry().put(BlockModelShapes.stateToModelLocation(blockState), new FrozenBlockBakedModel());
    }*/
}
