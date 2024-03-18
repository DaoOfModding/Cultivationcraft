package DaoOfModding.Cultivationcraft.Network.Packets;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import DaoOfModding.Cultivationcraft.StaminaHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class StaminaUsePacket extends Packet
{
    protected float stamina;
    protected UUID entity;

    public StaminaUsePacket(float staminaUse, UUID player)
    {
        stamina = staminaUse;
        entity = player;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeFloat(stamina);
        buffer.writeUUID(entity);
    }

    public static StaminaUsePacket decode(FriendlyByteBuf buffer)
    {
        StaminaUsePacket returnValue = new StaminaUsePacket(0, null);

        try
        {
            // Read in the sent values
            float stam = buffer.readFloat();
            UUID player = buffer.readUUID();

            return new StaminaUsePacket(stam, player);

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

        if (sideReceived.isServer())
        {
            Cultivationcraft.LOGGER.warn("StaminaUsePacket was received by server - This should not happen");
            return;
        }

        ctx.enqueueWork(() -> processPacket());
    }

    // Process received packet on the client
    protected void processPacket()
    {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(entity);

        if (player != null)
            StaminaHandler.updateStamina(player, stamina);
    }
}

