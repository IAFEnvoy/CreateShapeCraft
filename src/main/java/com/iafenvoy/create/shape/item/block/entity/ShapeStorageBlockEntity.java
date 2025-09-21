package com.iafenvoy.create.shape.item.block.entity;

import com.iafenvoy.create.shape.registry.CSCBlockEntities;
import com.iafenvoy.create.shape.item.container.ShapeStorageContainer;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

@EventBusSubscriber
public class ShapeStorageBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    protected final ShapeStorageContainer container = new ShapeStorageContainer(this, 65536);

    public ShapeStorageBlockEntity(BlockPos pos, BlockState blockState) {
        super(CSCBlockEntities.SHAPE_STORAGE.get(), pos, blockState);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
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

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        ShapeInfo info = this.container.getInfo();
        tooltip.add(Component.empty());
        tooltip.add(Component.literal(info.isEmpty() || this.container.getCount() == 0 ? "Empty" : info.toString()));
        tooltip.add(Component.literal("%s/%s".formatted(this.container.getCount(), this.container.getMaxCount())));
        return true;
    }

    public IItemHandler getItemHandler() {
        return this.container;
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SHAPE_STORAGE.get(), (be, context) -> be.getItemHandler());
    }
}
