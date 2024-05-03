package DaoOfModding.Cultivationcraft.Common.Blocks.custom;

import net.minecraft.world.level.block.BushBlock;

public class SpiritualPlant extends BushBlock {
    public final String name;
    public final int stemColor;
    public final int foliageColor;
    public final int fruitsColor;

    public final int spiritualPlantType = 0; // This is a dummy line and need to be changed to the right property types
    public final int spiritualPlantStage = 0; // This is a dummy line and need to be changed to the right property types
    public final int spiritualPlantGrowth = 0; // This is a dummy line and need to be changed to the right property types

    public final int spiritualPlantQi;

    public final String Properties = null; // This is a dummy line and need to be changed to the right property types
    public final String Alchemy_properties = null; // This is a dummy line and need to be changed to the right property types

    public SpiritualPlant(Properties properties, String name, int stemColor, int foliageColor, int fruitsColor, int spiritualPlantQi) {
        super(properties);
        this.name = name;
        this.stemColor = stemColor;
        this.foliageColor = foliageColor;
        this.fruitsColor = fruitsColor;
        this.spiritualPlantQi = spiritualPlantQi;
    }

}
