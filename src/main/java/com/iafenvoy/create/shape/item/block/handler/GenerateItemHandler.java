package com.iafenvoy.create.shape.item.block.handler;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class GenerateItemHandler implements IItemHandler, INBTSerializable<CompoundTag> {
    private final Predicate<ItemStack> allowed;
    private ItemStack provided = ItemStack.EMPTY;

    public GenerateItemHandler(Predicate<ItemStack> allowed) {
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
    public @NotNull ItemStack getStackInSlot(int slot) {
        return this.provided;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int count, boolean simulate) {
        return this.getStackInSlot(slot).copyWithCount(Math.min(count, this.getSlotLimit(slot)));
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.provided.getCount();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

    @Override
    public CompoundTag serializeNBT(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.put("provided", this.getProvided().saveOptional(registries));
        return tag;
    }

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider registries, @NotNull CompoundTag tag) {
        this.setProvided(ItemStack.parseOptional(registries, tag.getCompound("provided")));
    }
}
