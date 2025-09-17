package com.iafenvoy.create.shape;

import com.iafenvoy.create.shape.registry.*;
import com.mojang.logging.LogUtils;
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
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SHAPE_GENERATOR.get(), (be, context) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CSCBlockEntities.SHAPE_DESTROYER.get(), (be, context) -> be.getItemHandler());
    }
}
