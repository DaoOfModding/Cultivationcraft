package DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Qi.CultivationTypes;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import DaoOfModding.Cultivationcraft.Network.Packets.Packet;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class CultivatorTypePacket extends Packet
{
        protected UUID player;
        protected int cultivationType;

        public CultivatorTypePacket(UUID playerID, int type)
        {
            player = playerID;
            cultivationType = type;
        }

        @Override
        public void encode(FriendlyByteBuf buffer)
        {
            if (player != null)
            {
                buffer.writeUUID(player);
                buffer.writeInt(cultivationType);
            }
        }

        public static CultivatorTypePacket decode(FriendlyByteBuf buffer)
        {
            CultivatorTypePacket returnValue = new CultivatorTypePacket(null, CultivationTypes.NO_CULTIVATION);

            try
            {
                // Read in the send values
                UUID readingPlayer = buffer.readUUID();
                int type = buffer.readInt();

                return new CultivatorTypePacket(readingPlayer, type);

            }
            catch (IllegalArgumentException | IndexOutOfBoundsException e)
            {
                Cultivationcraft.LOGGER.warn("Exception while reading CultivatorTypePacket message: " + e);
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
                    Cultivationcraft.LOGGER.warn("Client sent target message for other player");
                else
                    ctx.enqueueWork(() -> processServerPacket());
            }
        }


        // Process received packet on the Server
        protected void processServerPacket()
        {
            // Grab the player entity based on the read UUID
            Player ownerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(player);

            if (CultivatorStats.getCultivatorStats(ownerEntity).getCultivationType() != CultivationTypes.NO_CULTIVATION)
            {
                Cultivationcraft.LOGGER.warn("Tried to change player cultivation when cultivation type already selected");
                return;
            }

            // Change the player's cultivation type to the new type
            CultivatorStats.getCultivatorStats(ownerEntity).setCultivationType(cultivationType);
            // Send the new player stats to the clients
            PacketHandler.sendCultivatorStatsToClient(ownerEntity);
        }
}
