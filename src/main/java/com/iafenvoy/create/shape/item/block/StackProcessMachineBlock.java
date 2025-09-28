package com.iafenvoy.create.shape.item.block;

import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.data.ShapeInfo;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiFunction;

public class StackProcessMachineBlock extends ProcessMachineBlock {
    private final BiFunction<ShapeInfo, ShapeInfo, ShapeInfo> processor;

    public StackProcessMachineBlock(BiFunction<ShapeInfo, ShapeInfo, ShapeInfo> processor) {
        this.processor = processor;
    }

    @Override
    public BlockEntityType<? extends BeltTunnelBlockEntity> getBlockEntityType() {
        return CSCBlockEntities.STACK_PROCESS_MACHINE.get();
    }

    public BiFunction<ShapeInfo, ShapeInfo, ShapeInfo> getProcessor() {
        return this.processor;
    }
}
