package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.item.ShapeItem;
import com.iafenvoy.create.shape.item.block.DyeProcessMachineBlock;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.registry.CSCTags;
import com.iafenvoy.create.shape.data.ShapeInfo;
import com.simibubi.create.content.fluids.transfer.FluidDrainingBehaviour;
import com.simibubi.create.content.fluids.transfer.FluidFillingBehaviour;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.EmptyFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;
import java.util.function.BiFunction;

public class DyeProcessMachineBlockEntity extends ProcessMachineBlockEntity {
    private static final int DYE_PER_SHAPE = 50;
    private final BiFunction<ShapeInfo, ShapeInfo.Color, ShapeInfo> processor;
    protected final FluidTank dyeHandler = new FluidTank(1000).setValidator(stack -> stack.is(CSCTags.SHAPE_DYES));

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
        behaviours.add(new FluidFillingBehaviour(this));
        behaviours.add(new FluidDrainingBehaviour(this));
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.dyeHandler.readFromNBT(registries, tag.getCompound("fluidTank"));
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.put("fluidTank", this.dyeHandler.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void process() {
        ShapeInfo info = this.inputStack.get(CSCDataComponents.SHAPE);
        if (this.inputStack.isEmpty() || info == null || this.dyeHandler.getFluidAmount() < DYE_PER_SHAPE) return;
        ItemStack result = ShapeItem.fromInfo(this.processor.apply(info, ShapeInfo.Color.forFluid(this.dyeHandler.getFluid().getFluid())));
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
            this.dyeHandler.drain(50, IFluidHandler.FluidAction.EXECUTE);
            this.setChanged();
        }
    }

    public IFluidHandler getFluidHandler(Direction dir) {
        if (dir == null) return this.dyeHandler;
        return dir.getAxis().isHorizontal() && dir.getAxis() != this.getBlockState().getValue(BeltTunnelBlock.HORIZONTAL_AXIS) ? this.dyeHandler : EmptyFluidHandler.INSTANCE;
    }
}
