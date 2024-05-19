package DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers;

import DaoOfModding.Cultivationcraft.Client.GUI.animatedTexture;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Cultivationcraft;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

public class ExplosionModifier extends TechniqueModifier
{
    public ExplosionModifier()
    {
        ID = new ResourceLocation(Cultivationcraft.MODID, "concept.explosion");
        CATEGORY = MODIFIER_CATEGORY;

        // TEMP
        unlockQuest = new Quest(Quest.DAMAGE_DEALT, 1);
        stabiliseQuest = new Quest(Quest.ELEMENTAL_EFFECT_APPLIED, 10000);

        coreTexture = new animatedTexture(new ResourceLocation(Cultivationcraft.MODID, "textures/cores/explosion.png"), 4, 4);

        allowSameCategory = true;
    }

    @Override
    public void onHitAll(Player owner, Vec3 pos, float damage, ResourceLocation element)
    {
        if (owner.level.isClientSide)
            return;

        // Temp radius calculation
        float radius = 1 + damage / 5f;

        explode(owner, pos, element, radius);
    }

    public void onHitTaken(Player owner, float damage, ResourceLocation defensiveElement)
    {
        if (owner.level.isClientSide)
            return;

        // Temp radius calculation
        float radius = 1 + damage / 5f;

        explode(owner, owner.position(), defensiveElement, radius);
    }

    public void explode(Player owner, Vec3 pos, ResourceLocation element, float radius)
    {
        Explosion explosion = new Explosion(owner.level, owner, new QiDamageSource("explosion.player", owner, element, true), null, pos.x, pos.y, pos.z, radius, false, Explosion.BlockInteraction.BREAK);

        explosion.explode();
        explosion.finalizeExplosion(false);

        for(ServerPlayer serverplayer : ((ServerLevel)owner.level).players())
            if (serverplayer.distanceToSqr(pos) < 4096.0D)
                serverplayer.connection.send(new ClientboundExplodePacket(pos.x, pos.y, pos.z, radius, explosion.getToBlow(), explosion.getHitPlayers().get(serverplayer)));
    }
}
