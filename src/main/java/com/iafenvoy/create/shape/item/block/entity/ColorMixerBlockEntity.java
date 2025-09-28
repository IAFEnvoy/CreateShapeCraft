package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.data.ColorMixData;
import com.iafenvoy.create.shape.data.ShapeColor;
import com.iafenvoy.create.shape.item.block.ColorMixerBlock;
import com.iafenvoy.create.shape.item.block.handler.DrainOnlyFluidTankWrapper;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.simibubi.create.content.fluids.transfer.FluidDrainingBehaviour;
import com.simibubi.create.content.fluids.transfer.FluidFillingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.EmptyFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class ColorMixerBlockEntity extends SmartBlockEntity {
    private static final int AMOUNT_PER_TICK = 10, FLUID_CAPACITY = 1000;
    private final FluidTank input1 = new FluidTank(FLUID_CAPACITY), input2 = new FluidTank(FLUID_CAPACITY), output = new FluidTank(FLUID_CAPACITY);

    public ColorMixerBlockEntity(BlockPos pos, BlockState state) {
        super(CSCBlockEntities.COLOR_MIXER.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new FluidFillingBehaviour(this));
        behaviours.add(new FluidDrainingBehaviour(this));
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.input1.readFromNBT(registries, tag.getCompound("input1"));
        this.input2.readFromNBT(registries, tag.getCompound("input2"));
        this.output.readFromNBT(registries, tag.getCompound("output"));
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.put("input1", this.input1.writeToNBT(registries, new CompoundTag()));
        tag.put("input2", this.input2.writeToNBT(registries, new CompoundTag()));
        tag.put("output", this.output.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    public void tick() {
        super.tick();
        this.process();
    }

    protected void process() {
        if (this.output.getCapacity() - this.output.getFluidAmount() < AMOUNT_PER_TICK * 2 ||
                this.input1.getFluidAmount() < AMOUNT_PER_TICK ||
                this.input2.getFluidAmount() < AMOUNT_PER_TICK) return;
        ShapeColor color1 = ShapeColor.forFluid(this.input1.getFluid().getFluid()), color2 = ShapeColor.forFluid(this.input2.getFluid().getFluid());
        if (color1 == null || color2 == null) return;
        ShapeColor result = ColorMixData.getOutput(color1, color2);
        if (result == null) return;
        Fluid fluid = result.getFluid();
        FluidStack stack = this.output.getFluid();
        boolean success = false;
        if (stack.isEmpty()) {
            this.output.setFluid(new FluidStack(fluid, AMOUNT_PER_TICK * 2));
            success = true;
        } else if (stack.is(fluid)) {
            stack.grow(AMOUNT_PER_TICK * 2);
            success = true;
        }
        if (success) {
            this.input1.drain(AMOUNT_PER_TICK, IFluidHandler.FluidAction.EXECUTE);
            this.input2.drain(AMOUNT_PER_TICK, IFluidHandler.FluidAction.EXECUTE);
            this.setChanged();
        }
    }

    public IFluidHandler getFluidHandler(Direction dir) {
        if (dir == null) return new CombinedTankWrapper(this.input1, this.input2, this.output);
        if (dir.get2DDataValue() == -1) return EmptyFluidHandler.INSTANCE;
        int d = (4 + dir.get2DDataValue() - this.getBlockState().getValue(ColorMixerBlock.FACING).get2DDataValue()) % 4;
        return switch (d) {
            case 1 -> this.input1;
            case 3 -> this.input2;
            case 0 -> new DrainOnlyFluidTankWrapper(this.output);
            default -> EmptyFluidHandler.INSTANCE;
        };
    }
}
