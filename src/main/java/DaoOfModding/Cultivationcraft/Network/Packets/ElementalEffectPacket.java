package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class ElementalEffectPacket extends Packet
{
    ResourceLocation el;
    ResourceKey<Level> lvl;
    BlockPos blockPos;

    public ElementalEffectPacket(ResourceLocation element, ResourceKey<Level> level, BlockPos pos)
    {
        el = element;
        lvl = level;
        blockPos = pos;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeResourceLocation(el);
        buffer.writeResourceKey(lvl);
        buffer.writeBlockPos(blockPos);
    }

    public static ElementalEffectPacket decode(FriendlyByteBuf buffer)
    {
        ElementalEffectPacket returnValue = new ElementalEffectPacket(null, null, null);

        try
        {
            // Read in the send values
            ResourceLocation element = buffer.readResourceLocation();
            ResourceKey<Level> level = buffer.readResourceKey(Registry.DIMENSION_REGISTRY);
            BlockPos pos = buffer.readBlockPos();

            return new ElementalEffectPacket(element, level, pos);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading ElementalEffectPacket message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived.isServer())
        {
            ctx.enqueueWork(() -> processPacket());
        }
        else
        {
            Cultivationcraft.LOGGER.warn("Server sent ElementalEffectPacket message");
        }
    }

    // Process received packet on the Server
    protected void processPacket()
    {
        Level level = ServerLifecycleHooks.getCurrentServer().getLevel(lvl);

        Elements.getElement(el).effectBlock(level, blockPos);
    }
}
