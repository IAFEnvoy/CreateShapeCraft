package com.iafenvoy.create.shape;

import com.iafenvoy.create.shape.registry.CSCRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = CreateShapeCraft.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class CreateShapeCraftClient {
    public CreateShapeCraftClient(ModContainer container) {
    }

    @SubscribeEvent
    public static void onInit(FMLClientSetupEvent event) {
        CSCRenderers.registerRenderTypes();
        CSCRenderers.registerBlockEntityRenderers();
        //FlyWheel
        CSCRenderers.registerVisualizers();
    }
}