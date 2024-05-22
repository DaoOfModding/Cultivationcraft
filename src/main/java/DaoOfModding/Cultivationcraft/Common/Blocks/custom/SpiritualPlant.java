package DaoOfModding.Cultivationcraft.Common.Blocks.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SpiritualPlant extends BushBlock {
    //public static final EnumProperty<SpiritualPlantNames> PLANT_NAME = EnumProperty.create("plant_name", SpiritualPlantNames.class);
    public static final IntegerProperty STEM_COLOR = IntegerProperty.create("stem_color", 0, 10000);
    public static final IntegerProperty FOLIAGE_COLOR = IntegerProperty.create("foliage_color", 0, 10000);
    public static final IntegerProperty FRUITS_COLOR = IntegerProperty.create("fruits_color", 0, 10000);

    public static final IntegerProperty QI = IntegerProperty.create("plant_qi", 0, 1000);

    public final int spiritualPlantType = 0; // This is a dummy line and need to be changed to the right property types
    public final int spiritualPlantStage = 0; // This is a dummy line and need to be changed to the right property types
    public final int spiritualPlantGrowth = 0; // This is a dummy line and need to be changed to the right property types

    public final String Properties = null; // This is a dummy line and need to be changed to the right property types
    public final String Alchemy_properties = null; // This is a dummy line and need to be changed to the right property types

    public SpiritualPlant(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                //.setValue(PLANT_NAME, SpiritualPlantNames.SPIRITUAL_GRASS)
                .setValue(STEM_COLOR, 10)
                .setValue(FOLIAGE_COLOR, 20)
                .setValue(FRUITS_COLOR, 30)
                .setValue(QI, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STEM_COLOR, FOLIAGE_COLOR, FRUITS_COLOR, QI);
    }

    public void PopulateBlockState(int qi) {
        this.defaultBlockState().setValue(QI, qi);
    }

}
