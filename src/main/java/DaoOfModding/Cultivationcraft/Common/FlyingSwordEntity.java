package DaoOfModding.Cultivationcraft.Common;

import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.CultivatorStats;
import DaoOfModding.Cultivationcraft.Common.Capabilities.CultivatorStats.ICultivatorStats;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
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
    // TODO: Make not die to explosions

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(FlyingSwordEntity.class, DataSerializers.ITEMSTACK);
    private static final DataParameter<Float> decaySpeed = EntityDataManager.createKey(FlyingSwordEntity.class, DataSerializers.FLOAT);

    private static final DataParameter<Float> movementX = EntityDataManager.createKey(FlyingSwordEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> movementY = EntityDataManager.createKey(FlyingSwordEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> movementZ = EntityDataManager.createKey(FlyingSwordEntity.class, DataSerializers.FLOAT);

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
        // If owner has disconnected, clear the owner instance
        if (owner != null && stats != null)
            if (stats.isDisconnected() || !owner.isAlive())
                owner = null;

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

                    storeMovement();
                }
            }
        }
    }

    // Sets the owner to null in order to update it later
    public void clearOwner()
    {
        owner = null;
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
        Vector3d toMove = movement.add(direction.scale(stats.getFlyingItemSpeed()));

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
        if (collisionEntity instanceof LivingEntity)
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

    // Return true if Flying Sword is in control range of owner
    public boolean isInRange()
    {
        if (owner != null && owner.isAlive() && getDistance(owner) < stats.getFlyingControlRange())
            return true;

        return false;
    }

    // Return true if Flying Sword is in control range of supplied vector
    private boolean isInRange(Vector3d pos)
    {
        if (getPositionVec().distanceTo(pos) < stats.getFlyingControlRange())
            return true;

        return false;
    }

    // Make the flying Sword fall to the ground
    private void fall()
    {
        direction = new Vector3d(0, -1, 0);

        movement = movement.add(direction.scale(2));
    }

    @Override
    public void tick() {

        if (getItem().onEntityItemUpdate((ItemEntity)this)) return;
        if (this.getItem().isEmpty()) {
            this.remove();
        } else
        {
            updateOwner();

            retrieveMovement();

            moveDecay();

            // If the flying sword is in range of it's owner then do normal movement, otherwise fall to the ground
            if (stats != null && isInRange())
            {
                this.baseTick();

                this.prevPosX = this.getPosX();
                this.prevPosY = this.getPosY();
                this.prevPosZ = this.getPosZ();

                // Set entity to be recalled if recall has been called
                if (stats.getRecall())
                    recall = true;

                // Move towards target if it exists, otherwise move towards owner
                if (stats.hasTarget(owner.getEntityWorld()) && isInRange(stats.getTarget()) && !recall)
                    moveToTarget();
                else
                    moveToOwner();

                if (orbit)
                    moveForwards();

                // Handle any collisions made by this entity
                handleCollisions();
            }
            else
                fall();

            // Calculate the item's motion based on it's movement vectors
            updateMotion();

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

            storeMovement();
        }
    }

    // Store the movement vector in the data manager
    private void storeMovement()
    {
        getDataManager().set(movementX, (float)movement.x);
        getDataManager().set(movementY, (float)movement.y);
        getDataManager().set(movementZ, (float)movement.z);
    }

    // Updated the movement vector to match the values in the data manager
    private void retrieveMovement()
    {
        movement = new Vector3d(getDataManager().get(movementX).floatValue(), getDataManager().get(movementY).floatValue(), getDataManager().get(movementZ).floatValue());
    }

    @Override
    public void registerData()
    {
        getDataManager().register(ITEM, ItemStack.EMPTY);
        getDataManager().register(decaySpeed, 0.5f);

        getDataManager().register(movementX, 0f);
        getDataManager().register(movementY, 0f);
        getDataManager().register(movementZ, 0f);
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

                // This crashes everything, why, dunno. Seems to work without it TODO: LOOK AT THIS
               // entityIn.onItemPickup(this, i);

                if (itemstack.isEmpty())
                {
                    this.remove();
                    itemstack.setCount(i);
                }

                entityIn.addStat(Stats.ITEM_PICKED_UP.get(item), i);
                entityIn.triggerItemPickupTrigger(this);
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
    // Removed cooldown and some crit stuff, edited to attack from flying sword instead of player
    public void attackTargetEntity(Entity targetEntity)
    {
        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(owner, targetEntity)) return;

        if (targetEntity.canBeAttackedWithItem())
        {
            if (!targetEntity.hitByEntity(this))
            {
                // Get the amount of damage to deal
                float f = 1;
                if (getItem().getItem() instanceof SwordItem)
                    f = ((SwordItem)getItem().getItem()).getAttackDamage();

                float f1;

                if (targetEntity instanceof LivingEntity)
                    f1 = EnchantmentHelper.getModifierForCreature(this.getItem(), ((LivingEntity)targetEntity).getCreatureAttribute());
                else
                    f1 = EnchantmentHelper.getModifierForCreature(this.getItem(), CreatureAttribute.UNDEFINED);

                f = f + f1;

                // Knockback
                int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, getItem());
                owner.world.playSound((PlayerEntity)null, getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, owner.getSoundCategory(), 1.0F, 1.0F);
                ++i;


                // lets not set things on fire right now...
                /*
                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspectModifier(owner);
                    if (targetEntity instanceof LivingEntity) {
                        f4 = ((LivingEntity)targetEntity).getHealth();
                        if (j > 0 && !targetEntity.isBurning()) {
                            flag4 = true;
                            targetEntity.setFire(1);
                        }
                    }*/

                // Knockback target
                Vector3d vector3d = targetEntity.getMotion();
                boolean flag5 = targetEntity.attackEntityFrom(new EntityDamageSource("player", this), f);

                if (flag5)
                {
                    if (targetEntity instanceof LivingEntity)
                        ((LivingEntity) targetEntity).applyKnockback((float) i * 0.5F, (double) MathHelper.sin(rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(rotationYaw * ((float) Math.PI / 180F))));
                    else
                        targetEntity.addVelocity((double)(-MathHelper.sin(rotationYaw * ((float)Math.PI / 180F)) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(rotationYaw * ((float)Math.PI / 180F)) * (float)i * 0.5F));

                    // Tell client player has been knocked back if knocking back a player
                    if (targetEntity instanceof ServerPlayerEntity && targetEntity.velocityChanged)
                    {
                        ((ServerPlayerEntity)targetEntity).connection.sendPacket(new SEntityVelocityPacket(targetEntity));
                        targetEntity.velocityChanged = false;
                        targetEntity.setMotion(vector3d);
                    }

                    owner.world.playSound((PlayerEntity)null, getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, owner.getSoundCategory(), 1.0F, 1.0F);


                    if (f1 > 0.0F)
                        owner.onEnchantmentCritical(targetEntity);

                    owner.setLastAttackedEntity(targetEntity);

                    // Ignore enchantmnets for now
                    /*
                    if (targetEntity instanceof LivingEntity) {
                         EnchantmentHelper.applyThornEnchantments((LivingEntity)targetEntity, owner);
                    }

                    EnchantmentHelper.applyArthropodEnchantments(owner, targetEntity);*/

                    Entity entity = targetEntity;
                    if (targetEntity instanceof EnderDragonPartEntity)
                        entity = ((EnderDragonPartEntity)targetEntity).dragon;


                    // I think this is the code the adds damage to an item
                    /*ItemStack itemstack1 = this.getItem();

                    if (!owner.world.isRemote && entity instanceof LivingEntity)
                    {
                        ItemStack copy = itemstack1.copy();
                        itemstack1.hitEntity((LivingEntity)entity, owner);
                    }*/

                    if (targetEntity instanceof LivingEntity)
                    {
                        float f5 = 0 - ((LivingEntity)targetEntity).getHealth();
                        owner.addStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));

                        /*
                            if (j > 0) {
                                targetEntity.setFire(j * 4);
                            }*/

                        if (owner.world instanceof ServerWorld && f5 > 2.0F)
                        {
                            int k = (int)((double)f5 * 0.5D);
                            ((ServerWorld)owner.world).spawnParticle(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getPosX(), targetEntity.getPosYHeight(0.5D), targetEntity.getPosZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                        }
                    }
                }
                else
                {
                    owner.world.playSound((PlayerEntity)null, getPosX(), getPosY(), getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, owner.getSoundCategory(), 1.0F, 1.0F);
                }

            }
        }
    }
}
