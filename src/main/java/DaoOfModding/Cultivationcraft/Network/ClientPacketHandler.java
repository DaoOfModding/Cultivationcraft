package DaoOfModding.Cultivationcraft.Network;

import DaoOfModding.Cultivationcraft.Client.genericClientFunctions;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Register;
import DaoOfModding.Cultivationcraft.Network.Packets.*;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.CultivatorTypePacket;
import DaoOfModding.Cultivationcraft.Network.Packets.CultivatorStats.TechniqueStatSelectionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class ClientPacketHandler
{
    public static void sendKeypressToServer(Register.keyPresses keyPress)
    {
        keypressPacket pack = new keypressPacket(keyPress);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendTechStatToServer(Class tech, ResourceLocation stat)
    {
        TechniqueStatSelectionPacket pack = new TechniqueStatSelectionPacket(tech.toString(), stat);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendBreakthroughToServer(Boolean downgrade)
    {
        BreakthroughPacket pack = new BreakthroughPacket(downgrade, "");
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendBreakthroughToServer(String cultivation)
    {
        BreakthroughPacket pack = new BreakthroughPacket(false, cultivation);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendTechniqueUseToServer(int slot, boolean keyDown)
    {
        TechniqueUsePacket pack = new TechniqueUsePacket(slot, keyDown);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendFlyingSwordConversionToServer(int heldItemID)
    {
        ConvertToFlyingPacket pack = new ConvertToFlyingPacket(heldItemID);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendAttackToServer(UUID playerID, HitResult.Type type, Vec3 pos, UUID targetID, Direction direction, int slot)
    {
        AttackPacket pack = new AttackPacket(playerID, type, pos, targetID, direction, slot);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendExternalBloodTick(double x, double y, double z, boolean onGround)
    {
        ExternalBloodTickPacket pack = new ExternalBloodTickPacket(x, y, z, onGround);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendElementalEffectToServer(ResourceLocation element, ResourceKey<Level> level, BlockPos pos)
    {
        ElementalEffectPacket pack = new ElementalEffectPacket(element, level, pos);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendTechniqueInfoToServer(UUID playerID, int info, String langLocation)
    {
        TechniqueInfoPacket packet = new TechniqueInfoPacket(langLocation, info, playerID);

        PacketHandler.channel.sendToServer(packet);
    }

    public static void sendQuestProgressToServer(UUID player, double amount)
    {
        QuestPacket packet = new QuestPacket(player, amount);

        PacketHandler.channel.sendToServer(packet);
    }

    public static void sendQuestCancelToServer()
    {
        QuestCancelPacket packet = new QuestCancelPacket();
        PacketHandler.channel.sendToServer(packet);
    }

    public static void sendCultivationTypeToServer(UUID player, int type)
    {
        CultivatorTypePacket packet = new CultivatorTypePacket(player, type);
        PacketHandler.channel.sendToServer(packet);
    }

    public static void sendPartInfoToServer(UUID playerID, int info, String partID, String limbID)
    {
        PartInfoPacket packet = new PartInfoPacket(partID, limbID, info, playerID);

        PacketHandler.channel.sendToServer(packet);
    }

    public static void sendCultivatorTechniquesToServer()
    {
        ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(genericClientFunctions.getPlayer());

        CultivatorTechniquesPacket pack = new CultivatorTechniquesPacket(genericClientFunctions.getPlayer().getUUID(), techs);
        PacketHandler.channel.sendToServer(pack);
    }

    public static void sendBodyForgeSelectionToServer(String selection)
    {
        BodyForgeSelectionPacket pack = new BodyForgeSelectionPacket(selection);
        PacketHandler.channel.sendToServer(pack);
    }
}
