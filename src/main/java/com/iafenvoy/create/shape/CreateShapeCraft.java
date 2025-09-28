package com.iafenvoy.create.shape;

import com.iafenvoy.create.shape.item.block.entity.ColorMixerBlockEntity;
import com.iafenvoy.create.shape.item.block.entity.DyeProcessMachineBlockEntity;
import com.iafenvoy.create.shape.ponder.CSCPonderPlugin;
import com.iafenvoy.create.shape.registry.*;
import com.mojang.logging.LogUtils;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

@Mod(CreateShapeCraft.MOD_ID)
@EventBusSubscriber
public class CreateShapeCraft {
    public static final String MOD_ID = "create_shape_craft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateShapeCraft(IEventBus bus, ModContainer container) {
        CSCBlocks.REGISTRY.register(bus);
        CSCBlockEntities.REGISTRY.register(bus);
        CSCCreativeModeTabs.REGISTRY.register(bus);
        CSCDataComponents.REGISTRY.register(bus);
        CSCItems.REGISTRY.register(bus);
        //Create Registry
        CSCItemAttributes.REGISTRY.register(bus);
        PonderIndex.addPlugin(CSCPonderPlugin.INSTANCE);
    }

    @SuppressWarnings("DataFlowIssue")
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        //Item
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SHAPE_GENERATOR.get(), (be, dir) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SHAPE_DESTROYER.get(), (be, dir) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SHAPE_STORAGE.get(), (be, dir) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SINGLE_PROCESS_MACHINE.get(), (be, dir) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.DYE_PROCESS_MACHINE.get(), (be, dir) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.STACK_PROCESS_MACHINE.get(), (be, dir) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.CUT_PROCESS_MACHINE.get(), (be, dir) -> be.getItemHandler());
        //Fluid
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, CSCBlockEntities.DYE_PROCESS_MACHINE.get(), DyeProcessMachineBlockEntity::getFluidHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, CSCBlockEntities.COLOR_MIXER.get(), ColorMixerBlockEntity::getFluidHandler);
    }
}
