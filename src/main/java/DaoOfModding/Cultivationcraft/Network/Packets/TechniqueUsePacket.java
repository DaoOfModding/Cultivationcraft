package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TechniqueUsePacket extends Packet
{
    private int slotNumber;
    private boolean isKeyDown;
    private PlayerEntity player;

    public TechniqueUsePacket(int slot, boolean keyDown)
    {
        slotNumber = slot;
        isKeyDown = keyDown;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeInt(slotNumber);
        buffer.writeBoolean(isKeyDown);
    }

    public static TechniqueUsePacket decode(PacketBuffer buffer)
    {
        TechniqueUsePacket returnValue = new TechniqueUsePacket(-1, false);

        try
        {
            // Read in the sent values
            int readSlot = buffer.readInt();
            boolean readKeyDown = buffer.readBoolean();

            return new TechniqueUsePacket(readSlot, readKeyDown);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading TechniqueUse message: " + e);
            return returnValue;
        }
    }

    // Read the packet received over the network
    public void handle(Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
        ctx.setPacketHandled(true);

        if (sideReceived.isClient())
        {
            Cultivationcraft.LOGGER.warn("TechniqueUsePacket was received by client - This should not happen");
            return;
        }

        // Check to ensure that the packet has valid data values
        if (slotNumber < 0)
        {
            Cultivationcraft.LOGGER.warn("TechniqueUsePacket was invalid: " + this.toString());
            return;
        }

        player = ctx.getSender();

        ctx.enqueueWork(() -> processPacket(ctx.getSender()));
    }

    // Process received packet on the Server
    private void processPacket(ServerPlayerEntity sender)
    {
        // Send the key press to the technique used
        Technique tech = CultivatorTechniques.getCultivatorTechniques(player).getTechnique(slotNumber);

        if (tech != null)
            tech.useKeyPressed(isKeyDown, player);

        PacketHandler.sendCultivatorTechniquesToClient((ServerPlayerEntity)player);
    }
}

