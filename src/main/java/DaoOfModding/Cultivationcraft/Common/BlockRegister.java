package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenBlock;
import DaoOfModding.Cultivationcraft.Common.Blocks.FrozenTileEntity;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegister
{
    public static FrozenBlock frozenBlock;
    public static TileEntityType<FrozenTileEntity> frozenTileEntityType;

    public static void registerBlockRenderers()
    {
        RenderTypeLookup.setRenderLayer(frozenBlock, RenderType.getSolid());
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent)
    {
        frozenBlock = (FrozenBlock)(new FrozenBlock().setRegistryName(Cultivationcraft.MODID, "frozenblock"));
        blockRegistryEvent.getRegistry().register(frozenBlock);
    }

    @SubscribeEvent
    public static void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event)
    {
        frozenTileEntityType = TileEntityType.Builder.create(FrozenTileEntity::new, frozenBlock).build(null);
        frozenTileEntityType.setRegistryName(Cultivationcraft.MODID, "frozentileentity");
        event.getRegistry().register(frozenTileEntityType);
    }
}
