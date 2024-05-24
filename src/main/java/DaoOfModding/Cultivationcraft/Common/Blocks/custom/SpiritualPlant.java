package DaoOfModding.Cultivationcraft.Common.Blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class SpiritualPlant extends BushBlock {
    protected ResourceLocation plantName;
    protected ResourceLocation element;
    protected int qi;

    public static final IntegerProperty QI = IntegerProperty.create("plant_qi", 0, 1000);

    public final String Properties = null; // This is a dummy line and need to be changed to the right property types
    public final String Alchemy_properties = null; // This is a dummy line and need to be changed to the right property types

    public SpiritualPlant(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(QI, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(QI);
    }

    public ResourceLocation getElement() {
        return element;
    }

    public void setElement(ResourceLocation element) {
        this.element = element;
    }

    public ResourceLocation getPlantName() {
        return plantName;
    }

    public void setPlantName(ResourceLocation plantName) {
        this.plantName = plantName;
    }

    public CompoundTag SerializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (plantName != null) {
            nbt.putString("plantname", plantName.toString());
        }
        if (element != null) {
            nbt.putString("element", element.toString());
        }
        return nbt;
    }

    public void DeserializeNBT(CompoundTag nbt) {
        if (nbt.contains("plantname")) {
            plantName = new ResourceLocation(nbt.getString("plantname"));
        }
        if (nbt.contains("element")) {
            element = new ResourceLocation(nbt.getString("element"));
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        System.out.println("Plant used!");
        System.out.println("Plant Element : " + element + " Plant Name : " + plantName);
        return super.use(blockState, level, pos, player, interactionHand, blockHitResult);
    }
}