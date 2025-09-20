package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.item.ShapeDyeItem;
import com.iafenvoy.create.shape.item.ShapeItem;
import com.iafenvoy.create.shape.item.block.DyeProcessMachineBlock;
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

public class DyeProcessMachineBlockEntity extends ProcessMachineBlockEntity {
    private final BiFunction<ShapeInfo, ShapeInfo.Color, ShapeInfo> processor;
    protected ItemStack dyeStack = ItemStack.EMPTY;

    public DyeProcessMachineBlockEntity(BlockPos pos, BlockState state) {
        super(CSCBlockEntities.DYE_PROCESS_MACHINE.get(), pos, state);
        if (state.getBlock() instanceof DyeProcessMachineBlock provider)
            this.processor = provider.getProcessor();
        else
            throw new IllegalArgumentException("DyeProcessMachineBlockEntity need a block with DyeProcessMachineBlock");
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(new DirectBeltInputBehaviour(this)
                .onlyInsertWhen(this::isSide)
                .allowingBeltFunnels()
                .setInsertionHandler(this::insertColor));
    }

    private ItemStack insertColor(TransportedItemStack stack, Direction side, boolean simulate) {
        ItemStack input = stack.stack.copy();
        if (this.isSide(side) && (this.dyeStack.isEmpty() || ItemStack.isSameItem(this.dyeStack, input)) && input.getItem() instanceof ShapeDyeItem) {
            int remain = MAX_STACK_COUNT - this.dyeStack.getCount();
            int inserted = Math.min(input.getCount(), remain);
            if (!simulate) {
                if (this.dyeStack.isEmpty()) this.dyeStack = input.copyWithCount(inserted);
                else this.dyeStack.grow(inserted);
            }
            input.shrink(inserted);
        }
        return input.isEmpty() ? ItemStack.EMPTY : input;
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.dyeStack = ItemStack.parseOptional(registries, tag.getCompound("dyeStack"));
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.put("dyeStack", this.dyeStack.saveOptional(registries));
    }

    @Override
    protected void process() {
        ShapeInfo info = this.inputStack.get(CSCDataComponents.SHAPE);
        if (this.inputStack.isEmpty() || info == null || !(this.dyeStack.getItem() instanceof ShapeDyeItem dye)) return;
        ItemStack result = ShapeItem.fromInfo(this.processor.apply(info, dye.getColor()));
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
            this.dyeStack.shrink(1);
            this.setChanged();
        }
    }
}
