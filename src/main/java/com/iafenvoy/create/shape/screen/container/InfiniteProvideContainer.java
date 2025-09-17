package com.iafenvoy.create.shape.screen.container;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class InfiniteProvideContainer implements IItemHandler {
    private final int maxCountPerTime;
    private final Predicate<ItemStack> allowed;
    private ItemStack provided = ItemStack.EMPTY;

    public InfiniteProvideContainer(int maxCountPerTime, Predicate<ItemStack> allowed) {
        this.maxCountPerTime = maxCountPerTime;
        this.allowed = allowed;
    }

    public ItemStack getProvided() {
        return this.provided;
    }

    public void setProvided(ItemStack provided) {
        if (this.allowed.test(provided)) this.provided = provided;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        return this.provided.copyWithCount(this.getSlotLimit(i));
    }

    @Override
    public @NotNull ItemStack insertItem(int i, @NotNull ItemStack itemStack, boolean b) {
        return itemStack;
    }

    @Override
    public @NotNull ItemStack extractItem(int i, int i1, boolean b) {
        return this.getStackInSlot(i);
    }

    @Override
    public int getSlotLimit(int i) {
        return this.maxCountPerTime;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack itemStack) {
        return false;
    }
}
