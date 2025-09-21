package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.screen.container.ShapeStorageContainer;
import com.simibubi.create.foundation.ICapabilityProvider;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

@EventBusSubscriber
public class ShapeStorageBlockEntity extends SmartBlockEntity {
    protected final ShapeStorageContainer container = new ShapeStorageContainer(65536);
    protected final ICapabilityProvider<IItemHandler> itemCapability = ICapabilityProvider.of(this.container);
    protected FilteringBehaviour filtering;

    public ShapeStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(CSCBlockEntities.SHAPE_STORAGE.get(), pos, blockState);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        //This method is called in super class constructor, so do not use method reference to call uninitialized fields.
//        this.filtering = new FilteringBehaviour(this, new ShapeGeneratorFilterSlot())
//                .showCount()
//                .withCallback(stack -> this.container.setProvided(stack.copyWithCount(this.filtering.getAmount())))
//                .withPredicate(Predicates.ALLOW_GENERATE);
//        behaviours.add(this.filtering);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.container.deserializeNBT(registries, tag.getCompound("container"));
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.put("container", this.container.serializeNBT(registries));
    }

    public IItemHandler getItemHandler() {
        return this.itemCapability.getCapability();
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SHAPE_STORAGE.get(), (be, context) -> be.getItemHandler());
    }
}
