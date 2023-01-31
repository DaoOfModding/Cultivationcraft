package DaoOfModding.Cultivationcraft.Network.Packets;


import DaoOfModding.Cultivationcraft.Common.Qi.BodyParts.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class QuestPacket extends Packet
{
    UUID player;
    double amount;

    public QuestPacket(UUID playerID, double increase)
    {
        player = playerID;
        amount = increase;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeUUID(player);
        buffer.writeDouble(amount);
    }

    public static QuestPacket decode(FriendlyByteBuf buffer)
    {
        QuestPacket returnValue = new QuestPacket(null, 0);

        try
        {
            // Read in the send values
            UUID readingPlayer = buffer.readUUID();
            double readingAmount = buffer.readDouble();

            return new QuestPacket(readingPlayer, readingAmount);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading quest message: " + e);
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
            if (ctx.getSender().getUUID().compareTo(player) != 0)
                Cultivationcraft.LOGGER.warn("Client sent quest message for other player");
            else
                ctx.enqueueWork(() -> processPacket());
        }
        else
        {
            if (Minecraft.getInstance().player.getUUID().compareTo(player) != 0)
                Cultivationcraft.LOGGER.warn("Server sent quest message for other player");
            else
                ctx.enqueueWork(() -> processPacketClient());
        }
    }

    // Process received packet on the Server
    protected void processPacket()
    {
        // Grab the player entity based on the read UUID
        Player ownerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(player);

        QuestHandler.progressQuest(ownerEntity, amount);
    }

    // Process received packet on the client
    protected void processPacketClient()
    {
        QuestHandler.progressQuest(Minecraft.getInstance().player, amount);
    }
}
