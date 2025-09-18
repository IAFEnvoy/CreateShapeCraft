package com.iafenvoy.create.shape.item.block;

import com.iafenvoy.create.shape.item.block.entity.SingleProcessMachineBlockEntity;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import com.iafenvoy.create.shape.shape.SingleProcessorProvider;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

public class SingleProcessMachineBlock extends BrassTunnelBlock implements SingleProcessorProvider {
    private final Function<ShapeInfo, ShapeInfo> processor;

    public SingleProcessMachineBlock(Function<ShapeInfo, ShapeInfo> processor) {
        super(Properties.of().mapColor(MapColor.STONE));
        this.processor = processor;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return this.isValidPositionForPlacement(state, worldIn, pos);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        super.onPlace(state, world, pos, p_220082_4_, p_220082_5_);
        BlockState below = world.getBlockState(pos);
        if (below.hasProperty(BeltBlock.CASING))
            world.setBlock(pos, below.setValue(BeltBlock.CASING, true), Block.UPDATE_NONE);
    }

    @Override
    public BlockEntityType<? extends SingleProcessMachineBlockEntity> getBlockEntityType() {
        return CSCBlockEntities.SINGLE_PROCESS_MACHINE.get();
    }

    @Override
    public Function<ShapeInfo, ShapeInfo> getProcessor() {
        return this.processor;
    }
}
