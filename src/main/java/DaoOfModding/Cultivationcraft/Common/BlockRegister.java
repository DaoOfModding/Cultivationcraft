package DaoOfModding.Cultivationcraft.Common;


/*
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegister
{
    public static FrozenBlock frozenBlock;
    public static BlockEntityType<FrozenBlockEntity> frozenBlockEntityType;


    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent)
    {
        frozenBlock = (FrozenBlock)(new FrozenBlock().setRegistryName(Cultivationcraft.MODID, "frozenblock"));
        blockRegistryEvent.getRegistry().register(frozenBlock);
    }

    @SubscribeEvent
    public static void registerBlockEntity(final RegistryEvent.Register<BlockEntityType<?>> event)
    {
        frozenBlockEntityType = BlockEntityType.Builder.of(FrozenBlockEntity::new, frozenBlock).build(null);
        frozenBlockEntityType.setRegistryName(Cultivationcraft.MODID, "frozenBlockEntity");
        event.getRegistry().register(frozenBlockEntityType);
    }
}*/
