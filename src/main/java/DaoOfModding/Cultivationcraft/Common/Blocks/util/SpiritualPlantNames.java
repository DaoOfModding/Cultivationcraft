package DaoOfModding.Cultivationcraft.Common.Blocks.util;

import net.minecraft.util.StringRepresentable;

public enum SpiritualPlantNames implements StringRepresentable {
    SPIRITUAL_GRASS("spiritual_grass"),
    SPIRITUAL_FLOWER("spiritual_flower"),
    SPIRITUAL_ROOT("spiritual_root");

    private final String name;

    private SpiritualPlantNames(String p_61339_) {
        this.name = p_61339_;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}
