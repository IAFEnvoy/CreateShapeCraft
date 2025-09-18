package com.iafenvoy.create.shape.registry;

import com.simibubi.create.content.logistics.tunnel.BeltTunnelRenderer;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelVisual;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public final class CSCRenderers {
    @SuppressWarnings("deprecation")
    public static void registerRenderTypes() {
        CSCBlockEntities.SINGLE_PROCESS_MACHINE.get().getValidBlocks().forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, RenderType.cutout()));
        CSCBlockEntities.DYE_PROCESS_MACHINE.get().getValidBlocks().forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, RenderType.cutout()));
        CSCBlockEntities.STACK_PROCESS_MACHINE.get().getValidBlocks().forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, RenderType.cutout()));
    }

    public static void registerBlockEntityRenderers() {
        BlockEntityRenderers.register(CSCBlockEntities.SHAPE_GENERATOR.get(), SmartBlockEntityRenderer::new);
        BlockEntityRenderers.register(CSCBlockEntities.SINGLE_PROCESS_MACHINE.get(), BeltTunnelRenderer::new);
        BlockEntityRenderers.register(CSCBlockEntities.DYE_PROCESS_MACHINE.get(), BeltTunnelRenderer::new);
        BlockEntityRenderers.register(CSCBlockEntities.STACK_PROCESS_MACHINE.get(), BeltTunnelRenderer::new);
    }

    public static void registerVisualizers() {
        SimpleBlockEntityVisualizer.builder(CSCBlockEntities.SINGLE_PROCESS_MACHINE.get()).factory(BeltTunnelVisual::new).neverSkipVanillaRender().apply();
        SimpleBlockEntityVisualizer.builder(CSCBlockEntities.DYE_PROCESS_MACHINE.get()).factory(BeltTunnelVisual::new).neverSkipVanillaRender().apply();
        SimpleBlockEntityVisualizer.builder(CSCBlockEntities.STACK_PROCESS_MACHINE.get()).factory(BeltTunnelVisual::new).neverSkipVanillaRender().apply();
    }
}
