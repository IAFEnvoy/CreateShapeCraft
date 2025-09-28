package com.iafenvoy.create.shape.item.block;

import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.data.ShapeInfo;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

public class SingleProcessMachineBlock extends ProcessMachineBlock {
    private final Function<ShapeInfo, ShapeInfo> processor;

    public SingleProcessMachineBlock(Function<ShapeInfo, ShapeInfo> processor) {
        this.processor = processor;
    }

    @Override
    public BlockEntityType<? extends BeltTunnelBlockEntity> getBlockEntityType() {
        return CSCBlockEntities.SINGLE_PROCESS_MACHINE.get();
    }

    public Function<ShapeInfo, ShapeInfo> getProcessor() {
        return this.processor;
    }
}
