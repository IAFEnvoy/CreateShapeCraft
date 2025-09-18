package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.item.block.slot.ShapeGeneratorFilterSlot;
import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.screen.container.InfiniteProvideContainer;
import com.iafenvoy.create.shape.util.Predicates;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.ICapabilityProvider;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

@EventBusSubscriber
public class ShapeGeneratorBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IHaveHoveringInformation {
    protected final InfiniteProvideContainer container = new InfiniteProvideContainer(1, Predicates.IS_SHAPE);
    protected final ICapabilityProvider<IItemHandler> itemCapability = ICapabilityProvider.of(this.container);
    protected FilteringBehaviour filtering;

    public ShapeGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(CSCBlockEntities.SHAPE_GENERATOR.get(), pos, blockState);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        //This method is called in super class constructor, so do not use method reference to call uninitialized fields.
        this.filtering = new FilteringBehaviour(this, new ShapeGeneratorFilterSlot())
                .showCount()
                .withCallback(stack -> this.container.setProvided(stack.copyWithCount(this.filtering.getAmount())))
                .withPredicate(Predicates.IS_SHAPE);
        behaviours.add(this.filtering);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.setProvided(ItemStack.parseOptional(registries, tag.getCompound("provided")));
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.put("provided", this.getProvided().saveOptional(registries));
    }

    public ItemStack getProvided() {
        return this.container.getProvided();
    }

    public void setProvided(ItemStack provided) {
        this.container.setProvided(provided);
    }

    public IItemHandler getItemHandler() {
        return this.itemCapability.getCapability();
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SHAPE_GENERATOR.get(), (be, context) -> be.getItemHandler());
    }
}
