package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Blocks.entity.FrozenBlockEntity;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FrozenItemStackPacket extends Packet {
    private final ItemStack itemStack;
    private final BlockPos pos;

    public FrozenItemStackPacket(ItemStack itemStack, BlockPos pos) {
        this.itemStack = itemStack;
        this.pos = pos;
    }

    /*    public FrozenItemStackPacket(FriendlyByteBuf buf) {
     *//*
        List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
*//*
        itemStack = new ItemStackHandler(collection.size());
        for (int i = 0; i < collection.size(); i++) {
            itemStackHandler.insertItem(i, collection.get(i), false);
        }
        itemStack = buf.readItem();
        this.pos = buf.readBlockPos();
    }*/

    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(itemStack.getItem().getDefaultInstance());
        buf.writeBlockPos(pos);
    }

    public static FrozenItemStackPacket decode(FriendlyByteBuf buf) {
        FrozenItemStackPacket returnValue = new FrozenItemStackPacket(null, null);
        try {
            ItemStack itemStack = buf.readItem();
            BlockPos pos = buf.readBlockPos();
            return new FrozenItemStackPacket(itemStack, pos);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            Cultivationcraft.LOGGER.warn("Exception while reading FrozenItemStackPacket message: " + e);
            return returnValue;
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FrozenBlockEntity blockEntity) {
                blockEntity.setItemStack(this.itemStack);
            }
        });
    }
}
