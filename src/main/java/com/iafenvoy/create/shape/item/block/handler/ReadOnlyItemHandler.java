package com.iafenvoy.create.shape.item.block.handler;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class ReadOnlyItemHandler implements IItemHandler {
    private final Supplier<List<ItemStack>> listSupplier;

    public ReadOnlyItemHandler(Supplier<List<ItemStack>> supplier) {
        this.listSupplier = supplier;
    }

    @Override
    public int getSlots() {
        return this.listSupplier.get().size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return this.listSupplier.get().get(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int count, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }
}
