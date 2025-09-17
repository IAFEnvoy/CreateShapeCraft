package com.iafenvoy.create.shape.registry;

import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class CSCRenderers {
    public static void registerBlockEntityRenderers() {
        BlockEntityRenderers.register(CSCBlockEntities.SHAPE_GENERATOR.get(), SmartBlockEntityRenderer::new);
    }
}
