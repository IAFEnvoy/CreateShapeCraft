package com.iafenvoy.create.shape.item.block;

import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiFunction;

public class DyeProcessMachineBlock extends ProcessMachineBlock {
    private final BiFunction<ShapeInfo, ShapeInfo.Color, ShapeInfo> processor;

    public DyeProcessMachineBlock(BiFunction<ShapeInfo, ShapeInfo.Color, ShapeInfo> processor) {
        this.processor = processor;
    }

    @Override
    public BlockEntityType<? extends BeltTunnelBlockEntity> getBlockEntityType() {
        return CSCBlockEntities.DYE_PROCESS_MACHINE.get();
    }

    public BiFunction<ShapeInfo, ShapeInfo.Color, ShapeInfo> getProcessor() {
        return this.processor;
    }
}
