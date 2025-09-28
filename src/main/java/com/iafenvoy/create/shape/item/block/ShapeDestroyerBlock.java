package com.iafenvoy.create.shape.item.block;

import com.iafenvoy.create.shape.item.block.entity.ShapeDestroyerBlockEntity;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ShapeDestroyerBlock extends Block implements IBE<ShapeDestroyerBlockEntity> {
    public ShapeDestroyerBlock() {
        super(Properties.ofFullCopy(Blocks.SMOOTH_STONE));
    }

    @Override
    public Class<ShapeDestroyerBlockEntity> getBlockEntityClass() {
        return ShapeDestroyerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ShapeDestroyerBlockEntity> getBlockEntityType() {
        return CSCBlockEntities.SHAPE_DESTROYER.get();
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }
}
