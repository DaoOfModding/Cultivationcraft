package DaoOfModding.Cultivationcraft.Network.Packets;


import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.AttackTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import DaoOfModding.Cultivationcraft.Network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class AttackPacket extends Packet
{
    protected UUID player = null;
    protected HitResult.Type targetType = HitResult.Type.MISS;
    protected Vec3 targetPos = null;
    protected UUID target = null;
    protected int techSlot = 0;
    protected Direction direction = Direction.UP;

    public AttackPacket(UUID playerID, HitResult.Type type, Vec3 pos, UUID targetUUID, Direction dir, int slot)
    {
        player = playerID;
        targetType = type;
        targetPos = pos;
        target = targetUUID;
        techSlot = slot;

        if (dir != null)
            direction = dir;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        if (player != null)
        {
            buffer.writeUUID(player);
            buffer.writeEnum(targetType);
            buffer.writeInt(techSlot);

            if (targetPos != null) {
                buffer.writeDouble(targetPos.x);
                buffer.writeDouble(targetPos.y);
                buffer.writeDouble(targetPos.z);

                buffer.writeEnum(direction);

                if (targetType == HitResult.Type.ENTITY)
                    buffer.writeUUID(target);
            }
        }
    }

    public static AttackPacket decode(FriendlyByteBuf buffer)
    {
        AttackPacket returnValue = new AttackPacket(null, HitResult.Type.MISS, null, null, Direction.UP,0);

        try
        {
            // Read in the send values
            UUID readingPlayer = buffer.readUUID();
            HitResult.Type readingType = buffer.readEnum(HitResult.Type.class);
            int slot = buffer.readInt();

            Vec3 readingPos = null;
            UUID readingTargetID = null;
            Direction readingDir = null;

            // Only read the target position if there is a target
            if (readingType != HitResult.Type.MISS) {
                readingPos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());

                readingDir = buffer.readEnum(Direction.class);

                // Only read the target ID if target is an entity
                if (readingType == HitResult.Type.ENTITY)
                    readingTargetID = buffer.readUUID();
            }

            return new AttackPacket(readingPlayer, readingType, readingPos, readingTargetID, readingDir, slot);

        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e)
        {
            Cultivationcraft.LOGGER.warn("Exception while reading Attack message: " + e);
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
                Cultivationcraft.LOGGER.warn("Client sent attack message for other player");
            else
                ctx.enqueueWork(() -> processServerPacket());
        }
        else
            ctx.enqueueWork(() -> processClientPacket());
    }


    // Process received packet on the Server
    protected void processServerPacket()
    {
        // Grab the player entity based on the read UUID
        Player ownerEntity = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(player);

        // Do nothing if the specified player doesn't exist
        if (ownerEntity == null)
            return;

        Technique tech = CultivatorTechniques.getCultivatorTechniques(ownerEntity).getTechnique(techSlot);

        // Do nothing if specified technique isn't an attack technique
        if (!(tech instanceof AttackTechnique))
            return;

        // Send packet to all clients
        PacketHandler.sendAttackToClient(player, targetType, targetPos, target, direction, techSlot);

        if (targetType == HitResult.Type.ENTITY)
        {
            double range = ((AttackTechnique)tech).getRange(ownerEntity);

            // Create a large bounding box at the specified position then search for a list of entities at that location
            AABB scan = new AABB(targetPos.x - range * 2, targetPos.y - range * 2, targetPos.z - range * 2, targetPos.x + range * 2, targetPos.y + range * 2, targetPos.z + range * 2);

            List<Entity> entities = ownerEntity.getCommandSenderWorld().getEntitiesOfClass(Entity.class, scan);

            // If entities have been found, search through and try to find one with the targetID
            if (!entities.isEmpty())
                for (Entity testEntity : entities)
                    if (testEntity.getUUID().equals(target))
                    {
                        ((AttackTechnique)tech).attackEntity(ownerEntity, testEntity);
                        return;
                    }
        }
        else if (targetType == HitResult.Type.BLOCK)
        {
            BlockPos pos = new BlockPos((int)targetPos.x, (int)targetPos.y, (int)targetPos.z);
            BlockState block = ownerEntity.getCommandSenderWorld().getBlockState(pos);

            if (block != null)
                ((AttackTechnique)tech).attackBlock(ownerEntity, block, pos, direction);
        }
        else
            ((AttackTechnique)tech).attackNothing(ownerEntity);
    }

    protected void processClientPacket()
    {
        // Do nothing if this is a packet for the current player
        if (player.compareTo(genericClientFunctions.getPlayer().getUUID()) == 0)
            return;

        Player pEntity = Minecraft.getInstance().level.getPlayerByUUID(player);

        // Do nothing if the player entity is not found
        if (pEntity == null)
            return;

        Technique tech = CultivatorTechniques.getCultivatorTechniques(pEntity).getTechnique(techSlot);

        // Do nothing if specified technique isn't an attack technique
        if (!(tech instanceof AttackTechnique))
            return;

        // Play attack animation for the specified player
        ((AttackTechnique)tech).attackAnimation(pEntity);
    }
}
