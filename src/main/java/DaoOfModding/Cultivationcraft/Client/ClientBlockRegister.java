package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Renderers.BlockEntityRenderers.FrozenBlockEntityRenderer;
import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientBlockRegister {
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

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockRegister.FROZEN_BLOCK_ENTITY.get()
                , FrozenBlockEntityRenderer::new);
    }
}
