package com.iafenvoy.create.shape.item.block;

import com.iafenvoy.create.shape.item.block.entity.ShapeStorageBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShapeStorageBlock extends BaseEntityBlock {
    public static final MapCodec<ShapeStorageBlock> CODEC = simpleCodec(p -> new ShapeStorageBlock());

    public ShapeStorageBlock() {
        super(Properties.ofFullCopy(Blocks.SMOOTH_STONE));
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ShapeStorageBlockEntity(blockPos, blockState);
    }
}
