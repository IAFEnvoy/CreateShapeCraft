package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.item.ShapeItem;
import com.iafenvoy.create.shape.item.block.SingleProcessMachineBlock;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.data.ShapeInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class SingleProcessMachineBlockEntity extends ProcessMachineBlockEntity {
    private final Function<ShapeInfo, ShapeInfo> processor;

    public SingleProcessMachineBlockEntity(BlockPos pos, BlockState state) {
        super(CSCBlockEntities.SINGLE_PROCESS_MACHINE.get(), pos, state);
        if (state.getBlock() instanceof SingleProcessMachineBlock provider)
            this.processor = provider.getProcessor();
        else
            throw new IllegalArgumentException("SingleProcessMachineBlockEntity need a block with SingleProcessMachineBlock");
    }

    @Override
    protected void process() {
        ShapeInfo info = this.inputStack.get(CSCDataComponents.SHAPE);
        if (this.inputStack.isEmpty() || info == null) return;
        ItemStack result = ShapeItem.fromInfo(this.processor.apply(info));
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
            this.setChanged();
        }
    }
}
