package com.iafenvoy.create.shape.render.item;

import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.render.model.LayerModel;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShapeItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final LayerModel model = new LayerModel(LayerModel.createBodyLayer().bakeRoot());

    @SuppressWarnings("DataFlowIssue")
    public ShapeItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext transformType, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(1, 0, 1);
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
        if (info == null) return;
        for (ShapeInfo.Layer layer : info.layers()) {
            this.renderPart(poseStack, buffer, layer.topRight(), light, overlay);
            this.renderPart(poseStack, buffer, layer.bottomRight(), light, overlay);
            this.renderPart(poseStack, buffer, layer.bottomLeft(), light, overlay);
            this.renderPart(poseStack, buffer, layer.topLeft(), light, overlay);
            poseStack.translate(0, 0.001, 0);
            poseStack.scale(0.75f, 0.75f, 0.75f);
        }
        poseStack.popPose();
    }

    private void renderPart(PoseStack poseStack, MultiBufferSource buffer, ShapeInfo.Part part, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(-0.035, 0, -0.035);
        if (!part.isEmpty()) {
            this.model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(part.shape().getBorderTexture())), light, overlay, -1);
            this.model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(part.shape().getInnerTexture())), light, overlay, part.color().getColor());
        }
        poseStack.popPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
    }
}
