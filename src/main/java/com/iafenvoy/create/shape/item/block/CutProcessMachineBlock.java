package com.iafenvoy.create.shape.item.block;

import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.function.Function;

public class CutProcessMachineBlock extends ProcessMachineBlock {
    private final Function<ShapeInfo, List<ShapeInfo>> processor;

    public CutProcessMachineBlock(Function<ShapeInfo, List<ShapeInfo>> processor) {
        this.processor = processor;
    }

    @Override
    public BlockEntityType<? extends BeltTunnelBlockEntity> getBlockEntityType() {
        return CSCBlockEntities.CUT_PROCESS_MACHINE.get();
    }

    public Function<ShapeInfo, List<ShapeInfo>> getProcessor() {
        return this.processor;
    }
}
