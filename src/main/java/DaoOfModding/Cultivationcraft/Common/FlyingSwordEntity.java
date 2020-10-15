package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import DaoOfModding.Cultivationcraft.Register;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FlyingSwordEntity extends ItemEntity
{
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(FlyingSwordEntity.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<Float> decaySpeed = EntityDataManager.createKey(FlyingSwordEntity.class, DataSerializers.FLOAT);

    private final double idleDistance = 3;

    private int age = 0;
    public Vector3d direction = new Vector3d(1, 0, 0);
    public Vector3d movement = new Vector3d(0, 0 ,0);
    private PlayerEntity owner = null;
    private ICultivatorStats stats = null;

    private boolean recall = false;

    // Testing thing
    private boolean orbit = false;

    public FlyingSwordEntity(EntityType<? extends ItemEntity> type, World worldIn)
    {
        super(type, worldIn);
        init();
    }

    public FlyingSwordEntity(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
        init();
    }

    public FlyingSwordEntity(World worldIn, double x, double y, double z, ItemStack stack)
    {
        super(worldIn, x, y, z, stack);
        init();
    }

    private void init()
    {
        this.setInfinitePickupDelay();

        if (Misc.enableHarvest)
            this.noClip = true;

        generateDecay();
    }

    private void generateDecay()
    {
        if (!world.isRemote)
        {
            Random test = new Random();
            test.setSeed(getItem().hashCode());

            getDataManager().set(decaySpeed, test.nextFloat() * 0.1f);
        }
    }

    // Check if owner is null, try to update if it is
    private void updateOwner()
    {
        if (owner == null)
        {
            UUID ownerID = getItem().getTag().getUniqueId("Owner");

            if (ownerID != null)
            {
                owner = this.world.getPlayerByUuid(ownerID);

                // if owner exists, grab this cultivator stats
                if (owner != null)
                {
                    movement = owner.getLookVec().normalize();
                    direction = owner.getLookVec().normalize();
                    stats = CultivatorStats.getCultivatorStats(owner);
                }
            }
        }
    }

    private void calculatePitchAndYaw()
    {
        // Store the previous pitch and yaw
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        // Calculate pitch and yaw based on direction

        // Subtracting quarter of PI to align yaw in the right direction
        rotationPitch = (float) Math.asin(direction.y) - (float)(Math.PI / 4.0);

        // Subtracting half of PI to align yaw in the right direction
        rotationYaw = (float) Math.atan2(direction.x, direction.z) - (float)(Math.PI / 2.0);
    }

    // Turn item towards specified direction
    private void turnTowards(Vector3d newDirection)
    {
        Double angle = Math.asin(direction.crossProduct(newDirection).length());

        // NEEDS TO BE LOOKED AT, causing desyncs with server (I think it's fixed now)
        if (angle != 0) {

            Double theta = stats.getFlyingItemTurnSpeed();
            if (theta > Math.abs(angle))
                theta = angle;

            Vector3d u = direction.crossProduct(newDirection).normalize();

            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);

            Vector3d rotatedDirection = u.scale(u.dotProduct(direction));
            rotatedDirection = rotatedDirection.add(u.crossProduct(direction).crossProduct(u).scale(cosTheta));
            rotatedDirection = rotatedDirection.add(u.crossProduct(direction).scale(sinTheta));

            if (rotatedDirection.length() != 0)
                direction = rotatedDirection;
        }
    }

    // Lower velocity by a specified amount
    private void moveDecay()
    {
        movement = movement.scale(0.85 + getDecaySpeed());
    }


    // Move flying sword forwards in specified direction
    private void moveForwards()
    {
        Vector3d toMove = movement.add(direction.mul(stats.getFlyingItemSpeed(), stats.getFlyingItemSpeed(), stats.getFlyingItemSpeed()));

        // If the movement vector is going faster than the item's max speed, lower it to the max speed
        if (toMove.length() > stats.getFlyingItemMaxSpeed())
            toMove = toMove.normalize().scale(stats.getFlyingItemMaxSpeed());

        movement = toMove;
    }

    private void moveToTarget()
    {
        Vector3d targetPos = stats.getTarget();

        // Check how far away the item is to the target
        double distX = getPosX() - targetPos.x;
        double distY = getPosY() - targetPos.y;
        double distZ = getPosZ() - targetPos.z;

        Vector3d targetDir = new Vector3d(-distX, -distY, -distZ);
        targetDir = targetDir.normalize();


        turnTowards(targetDir);

        if (!orbit)
            moveForwards();
    }

    // Move flying sword towards owner if it is outside set range
    private void moveToOwner()
    {
        // Get coordinates of owner
        double oPosX = owner.getPosX();
        double oPosY = owner.getPosY() + (owner.getHeight() / 2.0);
        double oPosZ = owner.getPosZ();

        // Check how far away the item is to the owner
        double distX = getPosX() - oPosX;
        double distY = getPosY() - oPosY;
        double distZ = getPosZ() - oPosZ;
        double distance = Math.abs(distX) + Math.abs(distY) + Math.abs(distZ);

        // If item is set distance from owner, turn towards them
        // Ignore distance limit if entity is recalling
        if (distance > idleDistance || recall)
        {
            Vector3d ownerDir = new Vector3d(-distX, -distY, -distZ);
            ownerDir = ownerDir.normalize();

            turnTowards(ownerDir);

            if (!orbit)
                moveForwards();
        }

    }

    private void updateMotion()
    {
        // For the moment, motion is solely based on movement, will add other external forces later
        this.setMotion(movement);
    }

    // Deal with any collisions made by this entity
    private void handleCollisions()
    {
        handleEntityCollisions();

        if (Misc.enableHarvest)
            handleBlockCollisions();
    }

    private void handleEntityCollisions()
    {
        // Get a list of entities within this entities bounding box
        List<Entity> collisionEntities = this.world.getEntitiesInAABBexcluding(this, getBoundingBox(), null);

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
    private boolean collideWithEntity(Entity collisionEntity)
    {
        // Do nothing if colliding with owner
        if (collisionEntity == this.owner)
            return false;

        // If colliding with a living entity
        if (collisionEntity.isLiving())
        {
            attackTargetEntity(collisionEntity);

            return true;
        }

        return false;
    }

    private void handleBlockCollisions()
    {
        BlockPos blockPosition = new BlockPos(getPositionVec());
        BlockState collisionBlock = owner.getEntityWorld().getBlockState(blockPosition);

        if (Misc.blockExists(collisionBlock.getBlock()))
        {
            tryHarvestBlock(blockPosition);
            bumpBackwards(0.4, Misc.getVector3dFromBlockPos(blockPosition));
        }
    }

    // 'Bump' this entity backwards slightly
    private void bumpBackwards(double power, Vector3d fromPos)
    {
        // Check how far away the item is to the target
        double distX = getPosX() - fromPos.x;
        double distY = getPosY() - fromPos.y;
        double distZ = getPosZ() - fromPos.z;

        Vector3d targetDir = new Vector3d(distX, distY, distZ);
        targetDir = targetDir.normalize();

        this.movement = targetDir.scale(stats.getFlyingItemMaxSpeed() * power);
    }

    // 'Bump' this entity backwards slightly
    private void bumpBackwards(double power)
    {
        this.movement = direction.scale(stats.getFlyingItemMaxSpeed() * -power);
    }

    @Override
    public void tick() {

        if (getItem().onEntityItemUpdate((ItemEntity)this)) return;
        if (this.getItem().isEmpty()) {
            this.remove();
        } else
        {
            updateOwner();

            if (owner != null && stats != null)
            {
                if (!this.world.isRemote) {
                    this.setFlag(6, this.isGlowing());
                }

                this.baseTick();

                this.prevPosX = this.getPosX();
                this.prevPosY = this.getPosY();
                this.prevPosZ = this.getPosZ();

                moveDecay();

                // Set entity to be recalled if recall has been called
                if (stats.getRecall())
                    recall = true;

                // Move towards target if it exists, otherwise move towards owner
                if (stats.hasTarget(owner.getEntityWorld()) && !recall)
                    moveToTarget();
                else
                    moveToOwner();

                if (orbit)
                    moveForwards();

                // Handle any collisions made by this entity
                handleCollisions();

                // Calculate the item's motion based on it's movement vectors
                updateMotion();
            }

            // Calculate items Pitch and Yaw for rendering purposes, only on client
            if (this.world.isRemote)
                calculatePitchAndYaw();

            this.move(MoverType.SELF, this.getMotion());

            // Increase the age, VERY important for rendering stuff (Stupid ItemEntity age being private ;( )
            if (this.age != -32768) {
                ++this.age;
            }

            if (this.getItem().isEmpty()) {
                this.remove();
            }
        }
    }

    @Override
    public void registerData()
    {
        getDataManager().register(ITEM, ItemStack.EMPTY);
        getDataManager().register(decaySpeed, 0.5f);
    }

    @Override
    public void setItem(ItemStack stack)
    {
        getDataManager().set(ITEM, stack);
    }

    private float getDecaySpeed()
    {
        return getDataManager().get(decaySpeed).floatValue();
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public ItemStack getItem() {
        return getDataManager().get(ITEM);
    }

    @Override
    public EntityType<?> getType()
    {
        return Register.FLYINGSWORD.get();
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    // Ripped almost word for word from ItemEntity.onCollideWithPlayer. Stupid minecraft
    @Override
    public void onCollideWithPlayer(PlayerEntity entityIn)
    {
        if (!this.world.isRemote)
        {
            // Only allow item to be picked up if it's collided with it's owner and is recalling
            if (entityIn != owner || !recall) return;

            ItemStack itemstack = this.getItem();
            Item item = itemstack.getItem();
            int i = itemstack.getCount();

            int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(this, entityIn);
            if (hook < 0) return;

            ItemStack copy = itemstack.copy();
            if (hook == 1 || i <= 0 || entityIn.inventory.addItemStackToInventory(itemstack))
            {
                copy.setCount(copy.getCount() - getItem().getCount());
                net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerItemPickupEvent(entityIn, this, copy);

                // This crashes everything, why, dunno. Seems to work without it, I'll LOOK AT THIS later
               // entityIn.onItemPickup(this, i);

                if (itemstack.isEmpty())
                {
                    this.remove();
                    itemstack.setCount(i);
                }

                entityIn.addStat(Stats.ITEM_PICKED_UP.get(item), i);
                entityIn.func_233630_a_(this);
            }

        }
    }

    // Ripped almost word for word from PlayerInteractionManager.tryHarvestBlock. Stupid minecraft
    public boolean tryHarvestBlock(BlockPos pos)
    {
        // Only attempt a harvest on the server
        if (!this.world.isRemote)
        {
            ServerPlayerEntity serverOwner = ((ServerPlayerEntity)owner);
            GameType gameType = serverOwner.interactionManager.getGameType();

            BlockState blockstate = this.world.getBlockState(pos);
            int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(world, gameType, serverOwner, pos);
            if (exp == -1) {
                return false;
            } else {
                TileEntity tileentity = this.world.getTileEntity(pos);
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
                        block.harvestBlock(this.world, owner, pos, blockstate, tileentity, getItem());
                    }

                    if (flag && exp > 0)
                        blockstate.getBlock().dropXpOnBlockBreak(serverOwner.getServerWorld(), pos, exp);

                    return true;
                }
            }
        }

        return false;
    }

    // Ripped almost word for word from PlayerInteractionManager.removeBlock. Stupid minecraft
    private boolean removeBlock(BlockPos p_180235_1_, boolean canHarvest) {
        BlockState state = this.world.getBlockState(p_180235_1_);
        boolean removed = state.removedByPlayer(this.world, p_180235_1_, owner, canHarvest, this.world.getFluidState(p_180235_1_));
        if (removed)
            state.getBlock().onPlayerDestroy(this.world, p_180235_1_, state);
        return removed;
    }

    // Ripped almost word for word from PlayerEntity.attackEntityWithCurrentItem. Stupid minecraft
    // Removed cooldown and some crit stuff
    public void attackTargetEntity(Entity targetEntity) {
        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(owner, targetEntity)) return;
        if (targetEntity.canBeAttackedWithItem()) {
            if (!targetEntity.hitByEntity(owner)) {
                float f = (float)owner.func_233637_b_(Attributes.field_233823_f_);
                float f1;
                if (targetEntity instanceof LivingEntity) {
                    f1 = EnchantmentHelper.getModifierForCreature(this.getItem(), ((LivingEntity)targetEntity).getCreatureAttribute());
                } else {
                    f1 = EnchantmentHelper.getModifierForCreature(this.getItem(), CreatureAttribute.UNDEFINED);
                }


                    boolean flag1 = false;
                    int i = 0;
                    i = i + EnchantmentHelper.getKnockbackModifier(owner);
                    if (owner.isSprinting()) {
                        owner.world.playSound((PlayerEntity)null, owner.getPosX(), owner.getPosY(), owner.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, owner.getSoundCategory(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    /*boolean flag2 = flag && owner.fallDistance > 0.0F && !owner.onGround && !owner.isOnLadder() && !owner.isInWater() && !owner.isPotionActive(Effects.BLINDNESS) && !owner.isPassenger() && targetEntity instanceof LivingEntity;
                    flag2 = flag2 && !owner.isSprinting();
                    net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(owner, targetEntity, flag2, flag2 ? 1.5F : 1.0F);
                    flag2 = hitResult != null;
                    if (flag2) {
                        f *= hitResult.getDamageModifier();
                    }*/

                    f = f + f1;
                    boolean flag3 = false;
                    double d0 = (double)(owner.distanceWalkedModified - owner.prevDistanceWalkedModified);
                    if (!flag1 && d0 < (double)owner.getAIMoveSpeed()) {
                        ItemStack itemstack = owner.getHeldItem(Hand.MAIN_HAND);
                        if (itemstack.getItem() instanceof SwordItem) {
                            flag3 = true;
                        }
                    }

                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(owner);
                    if (targetEntity instanceof LivingEntity) {
                        f4 = ((LivingEntity)targetEntity).getHealth();
                        if (j > 0 && !targetEntity.isBurning()) {
                            flag4 = true;
                            targetEntity.setFire(1);
                        }
                    }

                    Vector3d vector3d = targetEntity.getMotion();
                    boolean flag5 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(owner), f);
                    if (flag5) {
                        if (i > 0) {
                            if (targetEntity instanceof LivingEntity) {
                                ((LivingEntity)targetEntity).func_233627_a_((float)i * 0.5F, (double) MathHelper.sin(owner.rotationYaw * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(owner.rotationYaw * ((float)Math.PI / 180F))));
                            } else {
                                targetEntity.addVelocity((double)(-MathHelper.sin(owner.rotationYaw * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(owner.rotationYaw * ((float)Math.PI / 180F)) * (float)i * 0.5F));
                            }

                            owner.setMotion(owner.getMotion().mul(0.6D, 1.0D, 0.6D));
                            owner.setSprinting(false);
                        }

                        if (flag3) {
                            float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(owner) * f;

                            for(LivingEntity livingentity : owner.world.getEntitiesWithinAABB(LivingEntity.class, targetEntity.getBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
                                if (livingentity != owner && livingentity != targetEntity && !owner.isOnSameTeam(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingentity).hasMarker()) && owner.getDistanceSq(livingentity) < 9.0D) {
                                    livingentity.func_233627_a_(0.4F, (double)MathHelper.sin(owner.rotationYaw * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(owner.rotationYaw * ((float)Math.PI / 180F))));
                                    livingentity.attackEntityFrom(DamageSource.causePlayerDamage(owner), f3);
                                }
                            }

                            owner.world.playSound((PlayerEntity)null, owner.getPosX(), owner.getPosY(), owner.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, owner.getSoundCategory(), 1.0F, 1.0F);
                            owner.spawnSweepParticles();
                        }

                        if (targetEntity instanceof ServerPlayerEntity && targetEntity.velocityChanged) {
                            ((ServerPlayerEntity)targetEntity).connection.sendPacket(new SEntityVelocityPacket(targetEntity));
                            targetEntity.velocityChanged = false;
                            targetEntity.setMotion(vector3d);
                        }

                        if (!flag3)
                            owner.world.playSound((PlayerEntity)null, owner.getPosX(), owner.getPosY(), owner.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, owner.getSoundCategory(), 1.0F, 1.0F);


                        if (f1 > 0.0F)
                            owner.onEnchantmentCritical(targetEntity);

                        owner.setLastAttackedEntity(targetEntity);
                        if (targetEntity instanceof LivingEntity) {
                            EnchantmentHelper.applyThornEnchantments((LivingEntity)targetEntity, owner);
                        }

                        EnchantmentHelper.applyArthropodEnchantments(owner, targetEntity);
                        ItemStack itemstack1 = this.getItem();
                        Entity entity = targetEntity;
                        if (targetEntity instanceof EnderDragonPartEntity) {
                            entity = ((EnderDragonPartEntity)targetEntity).dragon;
                        }

                        if (!owner.world.isRemote && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
                            ItemStack copy = itemstack1.copy();
                            itemstack1.hitEntity((LivingEntity)entity, owner);
                            if (itemstack1.isEmpty()) {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(owner, copy, Hand.MAIN_HAND);
                                owner.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (targetEntity instanceof LivingEntity) {
                            float f5 = f4 - ((LivingEntity)targetEntity).getHealth();
                            owner.addStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                targetEntity.setFire(j * 4);
                            }

                            if (owner.world instanceof ServerWorld && f5 > 2.0F) {
                                int k = (int)((double)f5 * 0.5D);
                                ((ServerWorld)owner.world).spawnParticle(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getPosX(), targetEntity.getPosYHeight(0.5D), targetEntity.getPosZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        owner.addExhaustion(0.1F);
                    } else {
                        owner.world.playSound((PlayerEntity)null, owner.getPosX(), owner.getPosY(), owner.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, owner.getSoundCategory(), 1.0F, 1.0F);
                        if (flag4) {
                            targetEntity.extinguish();
                        }
                    }

            }
        }
    }
}
