package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.item.ShapeItem;
import com.iafenvoy.create.shape.item.block.CutProcessMachineBlock;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Function;

public class CutProcessMachineBlockEntity extends ProcessMachineBlockEntity {
    private final Function<ShapeInfo, List<ShapeInfo>> processor;
    protected final NonNullList<ItemStack> outputs = NonNullList.withSize(4, ItemStack.EMPTY);

    public CutProcessMachineBlockEntity(BlockPos pos, BlockState state) {
        super(CSCBlockEntities.CUT_PROCESS_MACHINE.get(), pos, state);
        if (state.getBlock() instanceof CutProcessMachineBlock provider)
            this.processor = provider.getProcessor();
        else
            throw new IllegalArgumentException("CutProcessMachineBlockEntity need a block with CutProcessMachineBlock");
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        ContainerHelper.loadAllItems(tag.getCompound("outputs"), this.outputs, registries);
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        CompoundTag compoundTag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundTag, this.outputs, registries);
        tag.put("outputs", compoundTag);
    }

    @Override
    protected int getOutputStackCount() {
        return 4;
    }

    @Override
    protected ItemStack getOutputStack(int index) {
        return this.outputs.get(index);
    }

    @Override
    protected void setOutputStack(int index, ItemStack stack) {
        this.outputs.set(index, stack);
    }

    @Override
    protected void process() {
        ShapeInfo info = this.inputStack.get(CSCDataComponents.SHAPE);
        if (this.inputStack.isEmpty() || info == null) return;
        List<ItemStack> results = this.processor.apply(info).stream().map(ShapeItem::fromInfo).toList();
        boolean success = true;
        for (int i = 0; i < results.size(); i++) {
            ItemStack stack = this.outputs.get(i);
            success &= stack.isEmpty() || ItemStack.isSameItemSameComponents(stack, results.get(i)) && stack.getCount() < MAX_STACK_COUNT;
        }
        if (success) {
            for (int i = 0; i < results.size(); i++) {
                ItemStack stack = this.outputs.get(i);
                if (stack.isEmpty()) this.outputs.set(i, results.get(i));
                else stack.grow(1);
            }
            this.inputStack.shrink(1);
            this.setChanged();
        }
    }
}
