package DaoOfModding.Cultivationcraft.Client;

import DaoOfModding.Cultivationcraft.Client.Renderers.BakedModels.FrozenBlockBakedModel;
import DaoOfModding.Cultivationcraft.Common.BlockRegister;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientBlockRegister
{
    public static void registerBlockRenderers()
    {
        RenderTypeLookup.setRenderLayer(BlockRegister.frozenBlock, RenderType.getTranslucent());
    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event)
    {
        for (BlockState blockState : BlockRegister.frozenBlock.getStateContainer().getValidStates())
            event.getModelRegistry().put(BlockModelShapes.getModelLocation(blockState), new FrozenBlockBakedModel());
    }
}
