package com.iafenvoy.create.shape.item.block.handler;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class DrainOnlyFluidTankWrapper implements IFluidHandler {
    private final FluidTank tank;

    public DrainOnlyFluidTankWrapper(FluidTank tank) {
        this.tank = tank;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int slot) {
        return this.tank.getFluidInTank(slot);
    }

    @Override
    public int getTankCapacity(int slot) {
        return this.tank.getTankCapacity(slot);
    }

    @Override
    public boolean isFluidValid(int slot, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(@NotNull FluidStack stack, @NotNull FluidAction action) {
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(@NotNull FluidStack stack, @NotNull FluidAction action) {
        return this.tank.drain(stack, action);
    }

    @Override
    public @NotNull FluidStack drain(int slot, @NotNull FluidAction action) {
        return this.tank.drain(slot, action);
    }
}
