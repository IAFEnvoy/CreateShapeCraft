package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.item.ShapeItem;
import com.iafenvoy.create.shape.item.block.StackProcessMachineBlock;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.BiFunction;

public class StackProcessMachineBlockEntity extends ProcessMachineBlockEntity {
    private final BiFunction<ShapeInfo, ShapeInfo, ShapeInfo> processor;
    protected ItemStack upperStack = ItemStack.EMPTY;

    public StackProcessMachineBlockEntity(BlockPos pos, BlockState state) {
        super(CSCBlockEntities.STACK_PROCESS_MACHINE.get(), pos, state);
        if (state.getBlock() instanceof StackProcessMachineBlock provider)
            this.processor = provider.getProcessor();
        else
            throw new IllegalArgumentException("StackProcessMachineBlockEntity need a block with StackProcessMachineBlock");
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(new DirectBeltInputBehaviour(this)
                .onlyInsertWhen(this::isSide)
                .allowingBeltFunnels()
                .setInsertionHandler(this::insertShape));
    }

    public ItemStack insertShape(TransportedItemStack stack, Direction side, boolean simulate) {
        ItemStack input = stack.stack.copy();
        if (this.isSide(side) && (this.upperStack.isEmpty() || ItemStack.isSameItem(this.upperStack, input)) && input.has(CSCDataComponents.SHAPE)) {
            int remain = MAX_STACK_COUNT - this.upperStack.getCount();
            int inserted = Math.min(input.getCount(), remain);
            if (!simulate) {
                if (this.upperStack.isEmpty()) this.upperStack = input.copyWithCount(inserted);
                else this.upperStack.grow(inserted);
            }
            input.shrink(inserted);
        }
        return input.isEmpty() ? ItemStack.EMPTY : input;
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.upperStack = ItemStack.parseOptional(registries, tag.getCompound("upperStack"));
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.put("upperStack", this.upperStack.saveOptional(registries));
    }

    @Override
    protected void process() {
        ShapeInfo info = this.inputStack.get(CSCDataComponents.SHAPE), upperInfo = this.upperStack.get(CSCDataComponents.SHAPE);
        if (this.inputStack.isEmpty() || info == null || this.upperStack.isEmpty() || upperInfo == null) return;
        ItemStack result = ShapeItem.fromInfo(this.processor.apply(info, upperInfo));
        boolean success = false;
        if (this.outputStack.isEmpty()) {
            this.outputStack = result;
            success = true;
        } else if (ItemStack.isSameItemSameComponents(this.outputStack, result) && this.outputStack.getCount() < MAX_STACK_COUNT) {
            this.outputStack.grow(1);
            success = true;
        }
        if (success) {
            this.inputStack.shrink(1);
            this.upperStack.shrink(1);
            this.setChanged();
        }
    }

    @Override
    protected List<ItemStack> grabInputs() {
        return List.of(this.inputStack, this.upperStack);
    }
}
