package com.iafenvoy.create.shape.item.block;

import com.iafenvoy.create.shape.item.block.entity.ShapeStorageBlockEntity;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ShapeStorageBlock extends Block implements IBE<ShapeStorageBlockEntity> {
    public ShapeStorageBlock() {
        super(Properties.ofFullCopy(Blocks.SMOOTH_STONE));
    }

    @Override
    public Class<ShapeStorageBlockEntity> getBlockEntityClass() {
        return ShapeStorageBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ShapeStorageBlockEntity> getBlockEntityType() {
        return CSCBlockEntities.SHAPE_STORAGE.get();
    }
}
