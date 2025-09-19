package com.iafenvoy.create.shape.item.block;

import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class ProcessMachineBlock extends BrassTunnelBlock {
    public ProcessMachineBlock() {
        super(Properties.of().mapColor(MapColor.STONE));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return this.isValidPositionForPlacement(state, worldIn, pos);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        super.onPlace(state, world, pos, p_220082_4_, p_220082_5_);
        BlockPos belowPos = pos.below();
        BlockState below = world.getBlockState(belowPos);
        if (below.hasProperty(BeltBlock.CASING))
            world.setBlock(belowPos, below.setValue(BeltBlock.CASING, true), Block.UPDATE_CLIENTS);
        if (world.getBlockEntity(belowPos) instanceof BeltBlockEntity belt)
            belt.setCasingType(BeltBlockEntity.CasingType.ANDESITE);
    }

    @Override
    public void destroy(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
        super.destroy(level, pos, state);
        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        if (below.hasProperty(BeltBlock.CASING))
            level.setBlock(belowPos, below.setValue(BeltBlock.CASING, false), Block.UPDATE_CLIENTS);
        if (level.getBlockEntity(belowPos) instanceof BeltBlockEntity belt)
            belt.setCasingType(BeltBlockEntity.CasingType.NONE);
    }
}
