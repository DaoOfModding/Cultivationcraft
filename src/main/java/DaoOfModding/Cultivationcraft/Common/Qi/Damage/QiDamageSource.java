package DaoOfModding.Cultivationcraft.Common.Qi.Damage;

import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class QiDamageSource extends DamageSource
{
    public final ResourceLocation damageElement;
    // Internal damage bypasses armor, external does not
    protected boolean internal = false;

    protected Entity entity = null;
    protected Vec3 sourcePos;

    protected boolean doStatusEffect = true;

    public QiDamageSource(String msgId, ResourceLocation element, Boolean statusEffect)
    {
        super(msgId);

        damageElement = element;

        // Bypass armor if this damage source has an element
        if (damageElement != null || damageElement.compareTo(Elements.noElement) != 0)
            bypassArmor();

        doStatusEffect = statusEffect;
    }

    public QiDamageSource(String msgId, Entity attacker, ResourceLocation element, Boolean statusEffect)
    {
        super(msgId);
        entity = attacker;
        damageElement = element;
        doStatusEffect = statusEffect;
    }

    public static QiDamageSource playerAttack(Player player, ResourceLocation element, Boolean statusEffect)
    {
        return new QiDamageSource("player", player, element, statusEffect);
    }

    public static QiDamageSource playerAttack(Player player, ResourceLocation element, String source, Boolean statusEffect)
    {
        return new QiDamageSource(source, player, element, statusEffect);
    }

    public QiDamageSource(DamageSource source)
    {
        super(source.msgId);

        ResourceLocation element = Elements.noElement;

        DamageSourceToDamageSource(this, source);

        if (source.isExplosion() || source.isFire())
            element = Elements.fireElement;

        if (source.isFire())
            doStatusEffect = false;

        if (getMsgId().compareTo(DamageSource.DRAGON_BREATH.getMsgId()) == 0)
            element = Elements.fireElement;
        else if (getMsgId().compareTo(DamageSource.LIGHTNING_BOLT.getMsgId()) == 0)
            element = Elements.lightningElement;
        else if (getMsgId().compareTo(DamageSource.CACTUS.getMsgId()) == 0)
            element = Elements.woodElement;
        else if (getMsgId().compareTo(DamageSource.SWEET_BERRY_BUSH.getMsgId()) == 0)
            element = Elements.woodElement;
        else if (getMsgId().compareTo(DamageSource.FREEZE.getMsgId()) == 0)
            element = Elements.iceElement;
        else if (getMsgId().compareTo(DamageSource.FALLING_STALACTITE.getMsgId()) == 0)
            element = Elements.earthElement;
        else if (getMsgId().compareTo(DamageSource.STALAGMITE.getMsgId()) == 0)
            element = Elements.earthElement;
        else if (getMsgId().compareTo(DamageSource.STARVE.getMsgId()) == 0)
            setInternal();
        else if (getMsgId().compareTo(DamageSource.DROWN.getMsgId()) == 0)
            setInternal();
        else if (getMsgId().compareTo(DamageSource.IN_WALL.getMsgId()) == 0)
            setInternal();

        damageElement = element;

        // Bypass armor if this damage source has an element
        if (damageElement.compareTo(Elements.noElement) != 0)
            bypassArmor();
    }

    public ResourceLocation getElement()
    {
        return damageElement;
    }

    public QiDamageSource setInternal()
    {
        internal = true;

        this.bypassArmor();
        this.bypassEnchantments();
        this.bypassMagic();

        return this;
    }

    public boolean isInternal()
    {
        return internal;
    }

    public boolean doStatusEffect()
    {
        return doStatusEffect;
    }

    public static void DamageSourceToDamageSource(QiDamageSource to, DamageSource from)
    {
        if (from.isBypassArmor())
            to.bypassArmor();

        if (from.isExplosion())
            to.setExplosion();

        if (from.isFire())
            to.setIsFire();

        if (from.isFall())
            to.setIsFall();

        if (from.isBypassInvul())
            to.bypassInvul();

        if (from.isBypassEnchantments())
            to.bypassEnchantments();

        if (from.isBypassMagic())
            to.bypassMagic();

        if (from.isDamageHelmet())
            to.damageHelmet();

        if (from.isMagic())
            to.setMagic();

        if (from.isNoAggro())
            to.setNoAggro();

        if (from.isProjectile())
            to.setProjectile();

        to.entity = from.getEntity();

        try
        {
            to.sourcePos = from.getSourcePosition();
        }
        // Fixing crashes caused by other mods dying when from.getSourcePosition() is called
        catch (NullPointerException e)
        {
            if (to.entity == null)
                to.sourcePos = null;
            else
                to.sourcePos = to.entity.position();
        }

        if (to.isFire())
            to.doStatusEffect = false;
    }

    @Override
    public Vec3 getSourcePosition()
    {
        if (sourcePos == null && entity != null)
            sourcePos = entity.position();

        return sourcePos;
    }

    public Entity getEntity()
    {
        return entity;
    }
}
