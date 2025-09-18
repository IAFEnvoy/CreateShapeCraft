package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.item.ShapeItem;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import com.iafenvoy.create.shape.shape.SingleProcessorProvider;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@EventBusSubscriber
public class SingleProcessMachineBlockEntity extends BrassTunnelBlockEntity {
    private static final int PROCESS_INTERVAL = 4, MAX_STACK_COUNT = 4;
    protected final Function<ShapeInfo, ShapeInfo> processor;
    protected ItemStack inputStack = ItemStack.EMPTY, outputStack = ItemStack.EMPTY;
    protected Direction insertDirection = Direction.UP;
    private int processTimer;

    public SingleProcessMachineBlockEntity(BlockPos pos, BlockState state) {
        super(CSCBlockEntities.SINGLE_PROCESS_MACHINE.get(), pos, state);
        if (state.getBlock() instanceof SingleProcessorProvider provider)
            this.processor = provider.getProcessor();
        else
            throw new IllegalArgumentException("SingleProcessMachineBlockEntity need a block with SingleProcessorProvider");
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(new LinkedList<>());//Ignore all sub behaviours
    }

    @Override
    public void addBehavioursDeferred(List<BlockEntityBehaviour> behaviours) {
        super.addBehavioursDeferred(new LinkedList<>());//Ignore all sub behaviours
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.inputStack = ItemStack.parseOptional(registries, tag.getCompound("inputStack"));
        this.outputStack = ItemStack.parseOptional(registries, tag.getCompound("outputStack"));
        this.insertDirection = Objects.requireNonNullElse(Direction.byName(tag.getString("insertDirection")), Direction.UP);
        this.processTimer = tag.getInt("processTimer");
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.put("inputStack", this.inputStack.saveOptional(registries));
        tag.put("outputStack", this.outputStack.saveOptional(registries));
        tag.putString("insertDirection", this.insertDirection.getName());
        tag.putInt("processTimer", this.processTimer);
    }

    @Override
    public void updateTunnelConnections() {
        //Don't connect
    }

    @Override
    public void setStackToDistribute(ItemStack stack, @Nullable Direction enteredFrom) {
        //Do nothing, everything has done in #canInsert
        this.sendData();
        this.setChanged();
    }

    @Override
    public boolean hasDistributionBehaviour() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.processTimer++;
        if (this.processTimer >= PROCESS_INTERVAL) {
            this.processTimer = 0;
            ShapeInfo info = this.inputStack.get(CSCDataComponents.SHAPE);
            if (this.inputStack.isEmpty() || info == null) return;
            ItemStack result = ShapeItem.fromInfo(this.processor.apply(info));
            if (this.outputStack.isEmpty()) {
                this.outputStack = result;
                this.inputStack.shrink(1);
            } else if (ItemStack.isSameItemSameComponents(this.outputStack, result)) {
                this.outputStack.grow(1);
                this.inputStack.shrink(1);
            }
        }
        this.outputStack = this.insertIntoTunnel(this, this.insertDirection.getOpposite(), this.outputStack, false);
    }

    @Override
    public boolean canInsert(Direction side, ItemStack stack) {
        this.insertDirection = side;
        if (this.inputStack.isEmpty()) this.inputStack = stack.split(MAX_STACK_COUNT);
        else if (ItemStack.isSameItemSameComponents(this.inputStack, stack)) {
            stack.shrink(MAX_STACK_COUNT - this.inputStack.getCount());
            this.inputStack.setCount(MAX_STACK_COUNT);
        }
        return stack.isEmpty();
    }

    public IItemHandler getItemHandler() {
        return new StorageWrapper(this);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SINGLE_PROCESS_MACHINE.get(), (be, context) -> be.getItemHandler());
    }

    protected record StorageWrapper(SingleProcessMachineBlockEntity blockEntity) implements IItemHandler {
        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int i) {
            return this.blockEntity.outputStack;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) return ItemStack.EMPTY;
            else if (!this.isItemValid(slot, stack)) return stack;
            else {
                ItemStack existing = this.blockEntity.inputStack;
                int limit = this.getSlotLimit(slot);
                if (!existing.isEmpty()) {
                    if (!ItemStack.isSameItemSameComponents(stack, existing)) return stack;
                    limit -= existing.getCount();
                }
                if (limit <= 0) return stack;
                else {
                    boolean reachedLimit = stack.getCount() > limit;
                    if (!simulate) {
                        if (existing.isEmpty())
                            this.blockEntity.inputStack = reachedLimit ? stack.copyWithCount(limit) : stack;
                        else
                            existing.grow(reachedLimit ? limit : stack.getCount());
                        this.blockEntity.setChanged();
                    }
                    return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
                }
            }
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount == 0) {
                return ItemStack.EMPTY;
            } else {
                ItemStack existing = this.blockEntity.outputStack;
                if (existing.isEmpty()) {
                    return ItemStack.EMPTY;
                } else {
                    int toExtract = Math.min(amount, existing.getMaxStackSize());
                    if (existing.getCount() <= toExtract) {
                        if (!simulate) {
                            this.blockEntity.outputStack = ItemStack.EMPTY;
                            this.blockEntity.setChanged();
                            return existing;
                        } else return existing.copy();

                    } else {
                        if (!simulate) {
                            this.blockEntity.outputStack = existing.copyWithCount(existing.getCount() - toExtract);
                            this.blockEntity.setChanged();
                        }
                        return existing.copyWithCount(toExtract);
                    }
                }
            }
        }

        @Override
        public int getSlotLimit(int i) {
            return MAX_STACK_COUNT;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ItemStack.isSameItemSameComponents(this.blockEntity.inputStack, stack);
        }
    }
}
