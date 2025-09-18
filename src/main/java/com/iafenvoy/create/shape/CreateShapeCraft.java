package com.iafenvoy.create.shape;

import com.iafenvoy.create.shape.registry.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CreateShapeCraft.MOD_ID)
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
}
