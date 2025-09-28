package com.iafenvoy.create.shape.item.block;

import com.iafenvoy.create.shape.item.block.entity.ColorMixerBlockEntity;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorMixerBlock extends Block implements IBE<ColorMixerBlockEntity>, IWrenchable {
    public static final Property<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ColorMixerBlock() {
        super(Properties.ofFullCopy(Blocks.SMOOTH_STONE));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public Class<ColorMixerBlockEntity> getBlockEntityClass() {
        return ColorMixerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ColorMixerBlockEntity> getBlockEntityType() {
        return CSCBlockEntities.COLOR_MIXER.get();
    }
}
