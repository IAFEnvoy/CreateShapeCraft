package com.iafenvoy.create.shape.item.block.entity;

import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlock;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class ProcessMachineBlockEntity extends BrassTunnelBlockEntity {
    protected static final int PROCESS_INTERVAL = 4, MAX_STACK_COUNT = 4;
    protected ItemStack inputStack = ItemStack.EMPTY, outputStack = ItemStack.EMPTY;
    protected Direction insertDirection = Direction.UP;
    private int processTimer;

    public ProcessMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(new LinkedList<>());//Ignore all sub behaviours
    }

    @Override
    public void addBehavioursDeferred(List<BlockEntityBehaviour> behaviours) {
        super.addBehavioursDeferred(new LinkedList<>());//Ignore all sub behaviours
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.inputStack = ItemStack.parseOptional(registries, tag.getCompound("inputStack"));
        this.outputStack = ItemStack.parseOptional(registries, tag.getCompound("outputStack"));
        this.insertDirection = Objects.requireNonNullElse(Direction.byName(tag.getString("insertDirection")), Direction.UP);
        this.processTimer = tag.getInt("processTimer");
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.put("inputStack", this.inputStack.saveOptional(registries));
        tag.put("outputStack", this.outputStack.saveOptional(registries));
        tag.putString("insertDirection", this.insertDirection.getName());
        tag.putInt("processTimer", this.processTimer);
    }

    @Override
    public void updateTunnelConnections() {
        //Process flaps only
        this.flaps.clear();
        if (this.level == null) return;
        for (Direction direction : Iterate.horizontalDirections) {
            BlockState nextState = this.level.getBlockState(this.worldPosition.relative(direction));
            if (nextState.getBlock() instanceof BeltTunnelBlock) continue;
            if (nextState.getBlock() instanceof BeltFunnelBlock && nextState.getValue(BeltFunnelBlock.SHAPE) == BeltFunnelBlock.Shape.EXTENDED && nextState.getValue(BeltFunnelBlock.HORIZONTAL_FACING) == direction.getOpposite())
                continue;
            this.flaps.put(direction, this.createChasingFlap());
        }
        this.sendData();
    }

    private LerpedFloat createChasingFlap() {
        return LerpedFloat.linear()
                .startWithValue(.25f)
                .chase(0, .05f, LerpedFloat.Chaser.EXP);
    }

    @Override
    public void setStackToDistribute(ItemStack stack, @Nullable Direction enteredFrom) {
        //Do nothing, everything has done in #canInsert
        this.sendData();
        this.setChanged();
    }

    @Override
    public boolean hasDistributionBehaviour() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.processTimer++;
        if (this.processTimer >= PROCESS_INTERVAL) {
            this.processTimer = 0;
            this.process();
        }
        this.outputStack = this.insertIntoTunnel(this, this.insertDirection.getOpposite(), this.outputStack, false);
    }

    public abstract void process();

    @Override
    public boolean canInsert(Direction side, ItemStack stack) {
        this.insertDirection = side;
        if (this.inputStack.isEmpty()) this.inputStack = stack.split(MAX_STACK_COUNT);
        else if (ItemStack.isSameItemSameComponents(this.inputStack, stack)) {
            stack.shrink(MAX_STACK_COUNT - this.inputStack.getCount());
            this.inputStack.setCount(MAX_STACK_COUNT);
        }
        return stack.isEmpty();
    }

    public boolean isSide(Direction dir) {
        return dir.get2DDataValue() != -1 && dir.getAxis() != this.getBlockState().getValue(BeltTunnelBlock.HORIZONTAL_AXIS);
    }
}
