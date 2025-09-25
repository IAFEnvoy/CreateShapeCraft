package com.iafenvoy.create.shape.item.block.container;

import com.iafenvoy.create.shape.item.ShapeItem;
import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

public class ShapeStorageItemHandler implements IItemHandler, INBTSerializable<CompoundTag> {
    private final SmartBlockEntity blockEntity;
    private final int maxCount;
    @NotNull
    private ShapeInfo info = ShapeInfo.EMPTY;
    private int count = 0;

    public ShapeStorageItemHandler(SmartBlockEntity blockEntity, int maxCount) {
        this.blockEntity = blockEntity;
        this.maxCount = maxCount;
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public @NotNull ShapeInfo getInfo() {
        return this.info;
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return ShapeItem.fromInfo(this.info).copyWithCount(Math.min(this.count, 64));
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (!this.isItemValid(slot, stack)) return stack;
        if (this.info.isEmpty() || this.count == 0)
            this.info = Objects.requireNonNullElse(stack.get(CSCDataComponents.SHAPE), ShapeInfo.EMPTY);
        int transferred = Math.min(this.maxCount - this.count, stack.getCount());
        if (!simulate) {
            this.count += transferred;
            this.blockEntity.notifyUpdate();
        }
        return stack.copyWithCount(stack.getCount() - transferred);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int count, boolean simulate) {
        int transferred = Math.min(this.count, count);
        if (!simulate) {
            this.count -= transferred;
            this.blockEntity.notifyUpdate();
        }
        return ShapeItem.fromInfo(this.info).copyWithCount(transferred);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
        return info != null && (this.info.isEmpty() || this.count == 0 || Objects.equals(this.info, info));
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putString("info", this.info.toString());
        tag.putInt("count", this.count);
        return tag;
    }

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider registries, @NotNull CompoundTag tag) {
        this.info = ShapeInfo.parse(tag.getString("info"));
        this.count = tag.getInt("count");
    }
}
