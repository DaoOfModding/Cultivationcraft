package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Client.Renderer;
import DaoOfModding.Cultivationcraft.Client.Renderers.QiGlowRenderer;
import DaoOfModding.Cultivationcraft.Common.Advancements.CultivationAdvancements;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.CultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorTechniques.ICultivatorTechniques;
import DaoOfModding.Cultivationcraft.Common.Qi.Damage.QiDamageSource;
import DaoOfModding.Cultivationcraft.Common.Qi.Elements.Elements;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.Quest;
import DaoOfModding.Cultivationcraft.Common.Qi.Quests.QuestHandler;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.QiCondenserTechniques.FlyingSwordFormationTechnique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.Technique;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueModifiers.TechniqueModifier;
import DaoOfModding.Cultivationcraft.Common.Qi.Techniques.TechniqueStats.DefaultTechniqueStatIDs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FlyingSwordEntity extends ItemEntity
{
    protected static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(FlyingSwordEntity.class, EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<Float> decaySpeed = SynchedEntityData.defineId(FlyingSwordEntity.class, EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Float> movementX = SynchedEntityData.defineId(FlyingSwordEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> movementY = SynchedEntityData.defineId(FlyingSwordEntity.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> movementZ = SynchedEntityData.defineId(FlyingSwordEntity.class, EntityDataSerializers.FLOAT);

    protected final double idleDistance = 3;

    protected int age = 0;
    public Vec3 direction = new Vec3(1, 0, 0);
    public Vec3 movement = new Vec3(0, 0 ,0);
    protected Player owner = null;
    protected ICultivatorStats stats = null;
    protected FlyingSwordFormationTechnique formation = null;

    protected Vec3 target = new Vec3(0, 0, 0);

    protected boolean recall = false;

    private final float defaultspeed = 0.02f;
    private final float defaultmaxSpeed = 1;
    private final double defaultTurnSpeed = 0.2;
    private final float defaultdamage = 0.25f;
    private final float defaultRange = 10;

    private boolean canPickup = false;

    // Testing thing
    protected boolean orbit = false;

    public FlyingSwordEntity(EntityType<? extends ItemEntity> type, Level worldIn)
    {
        super(type, worldIn);
        init();
    }

    public FlyingSwordEntity(Level worldIn, double x, double y, double z, ItemStack stack)
    {
        super(worldIn, x, y, z, stack);
        init();
    }

    public Player getOwningPlayer()
    {
        return owner;
    }

    protected void init()
    {
        this.setNeverPickUp();
        this.setInvulnerable(true);

        if (Misc.enableHarvest)
            this.noPhysics = true;

        generateDecay();
    }

    public float getControlRange()
    {
        if (formation != null)
            return (float)formation.getTechniqueStat(DefaultTechniqueStatIDs.range, owner);

        return defaultRange;
    }

    public double getTurnSpeed()
    {
        if (formation != null)
            return formation.getTechniqueStat(FlyingSwordFormationTechnique.flyingswordturnspeed, owner);

        return defaultTurnSpeed;
    }

    public float getSpeed()
    {
        if (formation != null)
            return (float)formation.getTechniqueStat(FlyingSwordFormationTechnique.flyingswordspeed, owner) / 100f;

        return defaultspeed;
    }

    public float getDamageModifier()
    {
        if (formation == null)
            return defaultdamage;

        float damage = (float)formation.getTechniqueStat(DefaultTechniqueStatIDs.damage, owner);

        for (TechniqueModifier mod : CultivatorStats.getCultivatorStats(owner).getCultivation().getModifiers())
            damage *= mod.getDamageMultiplier(formation);

        return damage;
    }

    public float getMaxSpeed()
    {
        if (formation != null)
            return (float)formation.getTechniqueStat(FlyingSwordFormationTechnique.flyingswordmaxspeed, owner) / 100f;

        return defaultmaxSpeed;
    }

    @Override
    public boolean ignoreExplosion()
    {
        return true;
    }

    protected void generateDecay()
    {
        if (!level.isClientSide)
        {
            Random test = new Random();
            test.setSeed(getItem().hashCode());

            getEntityData().set(decaySpeed, test.nextFloat() * 0.1f);
        }
    }

    // Check if owner is null, try to update if it is
    protected void updateOwner()
    {
        // If owner has disconnected, clear the owner instance
        if (owner != null && stats != null)
            if (stats.isDisconnected() || !owner.isAlive())
                owner = null;

        if (owner == null)
        {
            UUID ownerID = getItem().getTag().getUUID("Owner");

            if (ownerID != null)
            {
                owner = this.level.getPlayerByUUID(ownerID);

                // if owner exists, grab this cultivator's stats
                if (owner != null)
                {
                    movement = owner.getLookAngle().normalize();
                    direction = owner.getLookAngle().normalize();
                    stats = CultivatorStats.getCultivatorStats(owner);

                    storeMovement();
                }
            }
        }

        if (owner != null)
        {
            ICultivatorTechniques techs = CultivatorTechniques.getCultivatorTechniques(owner);

            // If no formation has been set for this sword, find the first active one
            if (formation == null)
            {
                for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                    if (techs.getTechnique(i) != null && techs.getTechnique(i).isActive() && techs.getTechnique(i) instanceof FlyingSwordFormationTechnique)
                    {
                        formation = (FlyingSwordFormationTechnique) techs.getTechnique(i);
                        return;
                    }
            }
            // Otherwise ensure that the set formation still exists
            else
            {
                for (int i = 0; i < CultivatorTechniques.numberOfTechniques; i++)
                    if (techs.getTechnique(i) != formation)
                        return;

                formation = null;
            }
        }
    }

    // Sets the owner to null in order to update it later
    public void clearOwner()
    {
        owner = null;
    }

    protected void calculatePitchAndYaw()
    {
        // TODO: Not turning around correctly when going in reverse?

        // Store the previous pitch and yaw
        xRotO = getXRot();
        yRotO = getYRot();

        // Calculate pitch and yaw based on direction

        // Subtracting quarter of PI to align pitch in the right direction
        setXRot((float) Math.asin(direction.y) - (float)(Math.PI / 4.0));

        // Subtracting half of PI to align yaw in the right direction
        setYRot((float) Math.atan2(direction.x, direction.z) - (float)(Math.PI / 2.0));
    }

    // Turn item towards specified direction
    protected void turnTowards(Vec3 newDirection)
    {
        Double angle = Math.asin(direction.cross(newDirection).length());

        if (angle != 0)
        {
            double theta = getTurnSpeed();
            if (theta > Math.abs(angle))
                theta = angle;

            Vec3 u = direction.cross(newDirection).normalize();

            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);

            Vec3 rotatedDirection = u.scale(u.dot(direction));
            rotatedDirection = rotatedDirection.add(u.cross(direction).cross(u).scale(cosTheta));
            rotatedDirection = rotatedDirection.add(u.cross(direction).scale(sinTheta));

            if (rotatedDirection.length() != 0)
                direction = rotatedDirection;
        }
    }

    // Lower velocity by a specified amount
    protected void moveDecay()
    {
        movement = movement.scale(0.85 + getDecaySpeed());
    }


    // Move flying sword forwards in specified direction
    protected void moveForwards()
    {
        boolean canceled = false;

        for (TechniqueModifier mod : CultivatorStats.getCultivatorStats(owner).getCultivation().getModifiers())
            if (mod.flyingSwordMovementOverride(this, target))
                canceled = true;

        if (canceled)
            return;

        Vec3 toMove = movement.add(direction.scale(getSpeed()));

        // If the movement vector is going faster than the item's max speed, lower it to the max speed
        if (toMove.length() > getMaxSpeed())
            toMove = toMove.normalize().scale(getMaxSpeed());

        movement = toMove;
    }

    protected void moveToTarget()
    {
        Vec3 targetPos = formation.getTarget().position();

        for (TechniqueModifier mod : CultivatorStats.getCultivatorStats(owner).getCultivation().getModifiers())
            targetPos = mod.flyingSwordTargetOverride(this, formation.getTarget(), targetPos);

        target = targetPos;

        // Check how far away the item is to the target
        double distX = getX() - targetPos.x;
        double distY = getY() - targetPos.y;
        double distZ = getZ() - targetPos.z;

        Vec3 targetDir = new Vec3(-distX, -distY, -distZ);
        targetDir = targetDir.normalize();


        turnTowards(targetDir);

        if (!orbit)
            moveForwards();
    }

    // Move flying sword towards owner if it is outside set range
    protected void moveToOwner()
    {
        // Get coordinates of owner
        double oPosX = owner.getX();
        double oPosY = owner.getY() + (owner.getBbHeight() / 2.0);
        double oPosZ = owner.getZ();

        target = new Vec3(oPosX, oPosY, oPosZ);

        // Check how far away the item is to the owner
        double distX = getX() - oPosX;
        double distY = getY() - oPosY;
        double distZ = getZ() - oPosZ;
        double distance = Math.abs(distX) + Math.abs(distY) + Math.abs(distZ);

        // If item is set distance from owner, turn towards them
        // Ignore distance limit if entity is recalling
        if (distance > idleDistance || recall)
        {
            Vec3 ownerDir = new Vec3(-distX, -distY, -distZ);
            ownerDir = ownerDir.normalize();

            turnTowards(ownerDir);

            if (!orbit)
                moveForwards();
        }

    }

    protected void updateMotion()
    {
        // For the moment, motion is solely based on movement, will add other external forces later
        this.setDeltaMovement(movement);
    }

    // Deal with any collisions made by this entity
    protected void handleCollisions()
    {
        handleEntityCollisions();

        if (Misc.enableHarvest)
            handleBlockCollisions();
    }

    protected void handleEntityCollisions()
    {
        // Get a list of entities within this entities bounding box
        List<Entity> collisionEntities = this.level.getEntities(this, getBoundingBox());

        // If there are entities within this entities bounding box
        if (!collisionEntities.isEmpty())
        {
            boolean realHit = false;

            // Handle collision with each entity within bounding box
            for (Entity collisionsEntity : collisionEntities)
                if (collideWithEntity(collisionsEntity))
                    realHit = true;
                    //bumpBackwards(0.5, collisionsEntity.getPositionVec());


            // If hit an appropriate entity, bump this entity backwards
            if (realHit)
                bumpBackwards(0.5);

        }
    }

    // Handle collision with specified entity
    protected boolean collideWithEntity(Entity collisionEntity)
    {
        // Do nothing if colliding with owner
        if (collisionEntity == this.owner)
            return false;

        // If colliding with a living entity
        if (collisionEntity instanceof LivingEntity)
        {
            attackTargetEntity(collisionEntity);

            return true;
        }

        return false;
    }

    protected void handleBlockCollisions()
    {
        BlockPos blockPosition = new BlockPos(position());
        BlockState collisionBlock = owner.getCommandSenderWorld().getBlockState(blockPosition);

        if (Misc.blockExists(collisionBlock.getBlock()))
        {
            tryHarvestBlock(blockPosition);
            bumpBackwards(0.4, Misc.getVec3FromBlockPos(blockPosition));
        }
    }

    // 'Bump' this entity backwards slightly
    protected void bumpBackwards(double power, Vec3 fromPos)
    {
        // Check how far away the item is to the target
        double distX = getX() - fromPos.x;
        double distY = getY() - fromPos.y;
        double distZ = getZ() - fromPos.z;

        Vec3 targetDir = new Vec3(distX, distY, distZ);
        targetDir = targetDir.normalize();

        this.movement = targetDir.scale(getMaxSpeed() * power);
    }

    // 'Bump' this entity backwards slightly
    protected void bumpBackwards(double power)
    {
        this.movement = direction.scale(getMaxSpeed() * -power);
    }

    // Return true if Flying Sword is in control range of owner
    public boolean isInRange()
    {
        if (owner != null && owner.isAlive() && distanceTo(owner) < getControlRange())
            return true;

        return false;
    }

    // Return true if Flying Sword is in control range of supplied vector
    protected boolean isInRange(Vec3 pos)
    {
        if (position().distanceTo(pos) < getControlRange())
            return true;

        return false;
    }

    // Make the flying Sword fall to the ground
    protected void fall()
    {
        canPickup = true;
        direction = new Vec3(0, -1, 0);

        movement = movement.add(direction.scale(0.2));
    }

    public boolean canControl()
    {
        return (stats != null && formation != null && isInRange());
    }

    @Override
    public void tick()
    {
        if (getItem().onEntityItemUpdate(this))
            return;

        if (this.getItem().isEmpty())
            this.remove(RemovalReason.DISCARDED);
        else
        {
            updateOwner();

            retrieveMovement();

            moveDecay();

            // If the flying sword is in range of it's owner then do normal movement, otherwise fall to the ground
            if (canControl() && CultivatorStats.getCultivatorStats(owner).getCultivation().consumeQi(owner, formation.getTechniqueStat(DefaultTechniqueStatIDs.qiCost, owner) / 20f))
            {
                canPickup = false;

                this.baseTick();

                this.xOld = getX();
                this.yOld = getY();
                this.zOld = getZ();

                // Set entity to be recalled if set formation is not longer active
                if (!formation.isActive())
                    recall = true;

                // Move towards target if it exists, otherwise move towards owner
                if (formation.getTarget() != null && isInRange(formation.getTarget().position()) && !recall)
                    moveToTarget();
                else
                    moveToOwner();

                if (orbit)
                    moveForwards();

                // Handle any collisions made by this entity
                handleCollisions();

                // Set this entity to radiate Qi for clients
                if (this.level.isClientSide)
                    QiGlowRenderer.setQiVisible(this, Elements.getElement(formation.getElement()));
                else
                    Technique.tickTechniqueModifiers(owner, position(), formation.getElement());

                if (owner instanceof ServerPlayer serverPlayer)
                    CultivationAdvancements.HAS_FLYING_SWORD.trigger(serverPlayer, true);
            }
            else
                fall();

            // Calculate the item's motion based on it's movement vectors
            updateMotion();

            // Calculate items Pitch and Yaw for rendering purposes, only on client
            if (this.level.isClientSide())
                calculatePitchAndYaw();

            this.move(MoverType.SELF, this.getDeltaMovement());

            // Increase the age, VERY important for rendering stuff (Stupid ItemEntity age being protected ;( )
            if (this.age != -32768)
                ++this.age;

            if (this.age >= 400)
                this.age = 200;

            if (this.getItem().isEmpty()) {
                this.remove(RemovalReason.DISCARDED);
            }

            storeMovement();
        }
    }

    // Called when entity collides with the ground
    @Override
    public void resetFallDistance()
    {
        super.resetFallDistance();

        // Stop sliding when a falling sword lodges into the ground
        if (!canControl())
            movement = new Vec3(0, movement.y, 0);
    }

    // Store the movement vector in the data manager
    protected void storeMovement()
    {
        getEntityData().set(movementX, (float)movement.x);
        getEntityData().set(movementY, (float)movement.y);
        getEntityData().set(movementZ, (float)movement.z);
    }

    // Updated the movement vector to match the values in the data manager
    protected void retrieveMovement()
    {
        movement = new Vec3(getEntityData().get(movementX).floatValue(), getEntityData().get(movementY).floatValue(), getEntityData().get(movementZ).floatValue());
    }

    @Override
    public void defineSynchedData()
    {
        getEntityData().define(ITEM, ItemStack.EMPTY);
        getEntityData().define(decaySpeed, 0.5f);

        getEntityData().define(movementX, 0f);
        getEntityData().define(movementY, 0f);
        getEntityData().define(movementZ, 0f);
    }

    @Override
    public void setItem(ItemStack stack)
    {
        getEntityData().set(ITEM, stack);
    }

    protected float getDecaySpeed()
    {
        return getEntityData().get(decaySpeed).floatValue();
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public ItemStack getItem() {
        return getEntityData().get(ITEM);
    }

    @Override
    public EntityType<?> getType()
    {
        return Register.FLYINGSWORD.get();
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    // Ripped almost word for word from ItemEntity.playerTouch, otherwise ItemPickupParticle is created and crashes everything~
    @Override
    public void playerTouch(Player entityIn)
    {
        // Only allow item to be picked up if it's collided with it's owner and is recalling
        // Or when it is not being controlled by anyone
        if (!(((getAge() > 20) && canPickup) || (entityIn == owner && recall))) return;

        if (!this.level.isClientSide)
        {
            ItemStack itemstack = this.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(this, entityIn);
            if (hook < 0) return;

            ItemStack copy = itemstack.copy();
            if (hook == 1 || i <= 0 || entityIn.getInventory().add(itemstack))
            {
                copy.setCount(copy.getCount() - getItem().getCount());
                net.minecraftforge.event.ForgeEventFactory.firePlayerItemPickupEvent(entityIn, this, copy);

                if (itemstack.isEmpty())
                {
                    this.remove(RemovalReason.DISCARDED);
                    itemstack.setCount(i);
                }

                entityIn.awardStat(Stats.ITEM_PICKED_UP.get(item), i);
                entityIn.onItemPickup(this);
            }

        }
    }

    // Ripped almost word for word from PlayerInteractionManager.tryHarvestBlock. Stupid minecraft
    public boolean tryHarvestBlock(BlockPos pos)
    {
        // TODO: This is bugged as hell, fix it later maybe

        /*
        // Only attempt a harvest on the server
        if (!this.level.isClientSide)
        {
            ServerPlayer serverOwner = ((ServerPlayer)owner);
            GameType gameType = serverOwner.interactionManager.getGameType();

            BlockState blockstate = this.world.getBlockState(pos);
            int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(world, gameType, serverOwner, pos);
            if (exp == -1) {
                return false;
            } else {
                BlockEntity BlockEntity = this.world.getBlockEntity(pos);
                Block block = blockstate.getBlock();


                if ((block instanceof CommandBlockBlock || block instanceof StructureBlock || block instanceof JigsawBlock) && !owner.canUseCommandBlock()) {
                    this.world.notifyBlockUpdate(pos, blockstate, blockstate, 3);
                    return false;
                } else if (getItem().onBlockStartBreak(pos, owner)) {
                    return false;
                } else if (owner.blockActionRestricted(this.world, pos, gameType)) {
                    return false;
                } else {


                    boolean flag1 = blockstate.canHarvestBlock(this.world, pos, owner);

                    // Don't damage the item on use
                    //itemstack.onBlockDestroyed(this.world, blockstate, pos, this.player);

                    boolean flag = removeBlock(pos, flag1);

                    if (flag && flag1) {
                        block.harvestBlock(this.world, owner, pos, blockstate, BlockEntity, getItem());
                    }

                    if (flag && exp > 0)
                        blockstate.getBlock().dropXpOnBlockBreak(serverOwner.getServerWorld(), pos, exp);

                    return true;
                }
            }
        }
        */
        return false;
    }

    // Ripped almost word for word from Player.attack. Stupid minecraft
    // Removed cooldown and some crit stuff, edited to attack from flying sword instead of player
    public void attackTargetEntity(Entity targetEntity)
    {
        //if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(owner, targetEntity)) return;

        if (targetEntity.isAttackable())
        {
            if (!targetEntity.skipAttackInteraction(this))
            {
                float entityHealth = 0;

                if (targetEntity instanceof LivingEntity)
                    entityHealth = ((LivingEntity) targetEntity).getHealth();

                // Get the amount of damage to deal
                float f = 1;
                if (getItem().getItem() instanceof SwordItem)
                    f = ((SwordItem)getItem().getItem()).getDamage();

                f *= getDamageModifier();

                float f1;

                if (targetEntity instanceof LivingEntity)
                    f1 = EnchantmentHelper.getDamageBonus(this.getItem(), ((LivingEntity)targetEntity).getMobType());
                else
                    f1 = EnchantmentHelper.getDamageBonus(this.getItem(), MobType.UNDEFINED);

                f = f + f1;


                for (TechniqueModifier mod : CultivatorStats.getCultivatorStats(owner).getCultivation().getModifiers())
                    mod.onHitEntity(owner, targetEntity, f, formation.getElement(), position());

                // Knockback
                int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, getItem());
                owner.level.playSound((Player)null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, owner.getSoundSource(), 1.0F, 1.0F);
                ++i;

                // Knockback target
                Vec3 Vec3 = targetEntity.getDeltaMovement();
                boolean flag5 = targetEntity.hurt(new QiDamageSource("player", this, formation.getElement(), true), f);

                formation.levelUp(owner, f);

                if (flag5)
                {
                    if (targetEntity instanceof LivingEntity)
                        ((LivingEntity) targetEntity).knockback((float) i * 0.5F, (double) Mth.sin(getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(getYRot() * ((float) Math.PI / 180F))));
                    else
                        targetEntity.push((-Mth.sin(getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(Mth.cos(getYRot() * ((float)Math.PI / 180F)) * (float)i * 0.5F));

                    // Tell client player has been knocked back if knocking back a player
                    if (targetEntity instanceof ServerPlayer && targetEntity.hurtMarked)
                    {
                        ((ServerPlayer)targetEntity).connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
                        targetEntity.hurtMarked = false;
                        targetEntity.setDeltaMovement(Vec3);
                    }

                    owner.level.playSound((Player)null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_STRONG, owner.getSoundSource(), 1.0F, 1.0F);


                    if (f1 > 0.0F)
                        owner.magicCrit(targetEntity);

                    owner.setLastHurtMob(targetEntity);

                    // Ignore enchantments for now
                    /*
                    if (targetEntity instanceof LivingEntity) {
                         EnchantmentHelper.applyThornEnchantments((LivingEntity)targetEntity, owner);
                    }

                    EnchantmentHelper.applyArthropodEnchantments(owner, targetEntity);*/

                    if (targetEntity instanceof EnderDragonPart)
                        targetEntity = ((EnderDragonPart)targetEntity).parentMob;

                    if (targetEntity instanceof LivingEntity)
                    {
                        float rawDamage = entityHealth - ((LivingEntity)targetEntity).getHealth();
                        owner.awardStat(Stats.DAMAGE_DEALT, Math.round(rawDamage * 10.0F));

                        if (owner instanceof ServerPlayer)
                            CultivationAdvancements.FLYING_SWORD_ATTACk.trigger((ServerPlayer) owner, ((LivingEntity) targetEntity).getHealth() <= 0);

                        if (owner.level instanceof ServerLevel && rawDamage > 2.0F)
                        {
                            int k = (int)((double)rawDamage * 0.5D);
                            ((ServerLevel)owner.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY(0.5D), targetEntity.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                        }
                    }
                }
            }
        }
    }
}
