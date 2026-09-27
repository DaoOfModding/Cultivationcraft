package DaoOfModding.Cultivationcraft.Common.Blocks.entity;

import DaoOfModding.Cultivationcraft.Common.Blocks.BlockRegister;
import DaoOfModding.Cultivationcraft.Common.Blocks.custom.AlchemyCauldronBlock;
import DaoOfModding.Cultivationcraft.Common.Containers.AlchemyCauldronMenu;
import DaoOfModding.Cultivationcraft.Common.Alchemy.AlchemyFailureExplosion;
import DaoOfModding.Cultivationcraft.Common.Items.ItemRegister;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AlchemyCauldronBlockEntity extends BaseContainerBlockEntity {
    public static final int SLOT_COUNT = 9;
    public static final int INVENTORY_SIZE = SLOT_COUNT + 3;
    private final NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
    private int storedQi;
    private long receivingUntil;
    private long nextQiDecayTick = -1;
    private UUID lastRefiner;

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemyCauldronBlockEntity cauldron) {
        cauldron.depleteIdleQi();
        if (cauldron.isReceivingQi() && level.getGameTime() % 20 == 0) cauldron.refineBatch();
    }

    private void refineBatch() {
        if (!(level instanceof ServerLevel server)) return;
        int output = -1;
        int wasteOutput = -1;
        for (int i = SLOT_COUNT; i < INVENTORY_SIZE; i++) {
            if (items.get(i).isEmpty()) {
                if (output < 0) output = i;
                else { wasteOutput = i; break; }
            }
        }
        if (output < 0) return;
        var batch = DaoOfModding.Cultivationcraft.Common.Alchemy.AlchemyBatch.inspect(this, server);
        if (batch == null || storedQi < batch.definition().qi()) return;
        int waste = batch.wasteCount();
        // Reserve both possible outputs before rolling, so a full output cannot discard
        // waste or repeatedly reroll the batch without consuming its ingredients.
        if (batch.valid() && waste > 0 && wasteOutput < 0) return;
        int tier = ((AlchemyCauldronBlock) getBlockState().getBlock()).getAlchemyTier(getBlockState());
        ItemStack result = batch.refine(server, tier);
        // Validate capacity before rolling or consuming anything. Commit on the server thread.
        for (int i = 0; i < SLOT_COUNT; i++) items.set(i, ItemStack.EMPTY);
        items.set(output, result);
        if (result.is(ItemRegister.ALCHEMY_PILL.get()) && waste > 0)
            items.set(wasteOutput, new ItemStack(ItemRegister.ALCHEMY_REMNANTS.get(), waste));
        storedQi -= batch.definition().qi();
        setChanged();
        // Commit the failed batch before applying damage; no second roll or block explosion.
        if (result.is(ItemRegister.ALCHEMY_REMNANTS.get())) {
            Player refiner = lastRefiner == null ? null : server.getPlayerByUUID(lastRefiner);
            AlchemyFailureExplosion.burst(server, worldPosition, batch.definition().complexity(), refiner);
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) { return slot < SLOT_COUNT; }

    private int qiDecayInterval() {
        return Math.max(1, ((AlchemyCauldronBlock) getBlockState().getBlock()).getQiDecayIntervalTicks(getBlockState()));
    }

    private void depleteIdleQi() {
        if (!(level instanceof ServerLevel) || storedQi == 0) return;
        long now = level.getGameTime();
        int interval = qiDecayInterval();
        if (nextQiDecayTick < 0) {
            // Existing saves receive a full grace period when first loaded.
            nextQiDecayTick = now + interval;
            setChanged();
        } else if (now >= nextQiDecayTick) {
            // Use world time so unloading a chunk does not reset the decay timer.
            int lost = (int) Math.min((long) storedQi, 1 + (now - nextQiDecayTick) / interval);
            storedQi -= lost;
            nextQiDecayTick = storedQi == 0 ? -1 : nextQiDecayTick + (long) lost * interval;
            setChanged();
        }
    }

    public int getStoredQi() { return storedQi; }

    public boolean isReceivingQi() {
        return level != null && level.getGameTime() < receivingUntil;
    }

    public int receiveQi(int amount) {
        return receiveQi(amount, null);
    }

    public int receiveQi(int amount, @Nullable Player refiner) {
        if (!(level instanceof ServerLevel) || amount <= 0) return 0;
        depleteIdleQi();
        int accepted = Math.min(amount, Integer.MAX_VALUE - storedQi);
        if (accepted > 0) {
            storedQi += accepted;
            lastRefiner = refiner != null && refiner.level == level ? refiner.getUUID() : null;
            receivingUntil = level.getGameTime() + 40;
            nextQiDecayTick = level.getGameTime() + qiDecayInterval();
            setChanged();
        }
        return accepted;
    }

    public AlchemyCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.ALCHEMY_CAULDRON_ENTITY.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.cultivationcraft.alchemy_cauldron");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new AlchemyCauldronMenu(id, inventory, this, ContainerLevelAccess.create(level, worldPosition));
    }

    @Override
    public int getContainerSize() {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack removed = ContainerHelper.removeItem(items, slot, amount);
        if (!removed.isEmpty()) setChanged();
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack removed = ContainerHelper.takeItem(items, slot);
        if (!removed.isEmpty()) setChanged();
        return removed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                                       worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
        tag.putInt("StoredQi", storedQi);
        tag.putLong("NextQiDecayTick", nextQiDecayTick);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items.clear();
        ContainerHelper.loadAllItems(tag, items);
        storedQi = Math.max(0, tag.getInt("StoredQi"));
        nextQiDecayTick = tag.contains("NextQiDecayTick") ? tag.getLong("NextQiDecayTick") : -1;
        // A reloaded cauldron must receive fresh Qi before refining another batch.
        receivingUntil = 0;
        lastRefiner = null;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level instanceof ServerLevel server && !isRemoved()) {
            server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        if (packet.getTag() != null) load(packet.getTag());
    }
}
