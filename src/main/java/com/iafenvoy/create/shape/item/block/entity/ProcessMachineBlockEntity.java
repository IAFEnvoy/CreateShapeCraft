package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.item.ShapeDyeItem;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
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
        behaviours.add(new DirectBeltInputBehaviour(this)
                .onlyInsertWhen(this::isRightForShape)
                .allowingBeltFunnels()
                .setInsertionHandler(this::insertShape));
    }

    private boolean isRightForShape(Direction dir) {
        return dir.get2DDataValue() != -1 && dir.getAxis() == this.getBlockState().getValue(BeltTunnelBlock.HORIZONTAL_AXIS);
    }

    private ItemStack insertShape(TransportedItemStack stack, Direction side, boolean simulate) {
        ItemStack input = stack.stack;
        if (this.isRightForShape(side) && (this.inputStack.isEmpty() || ItemStack.isSameItemSameComponents(this.inputStack, input)) && input.getItem() instanceof ShapeDyeItem) {
            int remain = MAX_STACK_COUNT - this.inputStack.getCount();
            int inserted = Math.min(input.getCount(), remain);
            if (!simulate) {
                if (this.inputStack.isEmpty()) this.inputStack = input.copyWithCount(inserted);
                else this.inputStack.grow(inserted);
            }
            input.shrink(inserted);
        }
        return input.isEmpty() ? ItemStack.EMPTY : input;
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
        assert this.level != null;
        for (int i = 0; i < this.getOutputStackCount(); i++) {
            ItemStack stack = this.getOutputStack(i);
            if (this.level.getBlockEntity(this.getBlockPos().offset(this.insertDirection.getOpposite().getNormal())) instanceof ProcessMachineBlockEntity process) {
                process.canInsert(this.insertDirection, stack);
                process.setStackToDistribute(stack, this.insertDirection);
            } else {
                ItemStack result = this.insertIntoTunnel(this, this.insertDirection.getOpposite(), stack, false);
                if (result == null) result = ItemStack.EMPTY;
                this.setOutputStack(i, result);
            }
        }
    }

    protected abstract void process();

    //FIXME::Use Container Later
    protected int getOutputStackCount() {
        return 1;
    }

    protected ItemStack getOutputStack(int index) {
        return this.outputStack;
    }

    protected void setOutputStack(int index, ItemStack stack) {
        this.outputStack = stack;
    }

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
        return dir.get2DDataValue() != -1;
    }
}
