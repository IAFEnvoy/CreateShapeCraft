package com.iafenvoy.create.shape.render.item;

import com.iafenvoy.create.shape.data.ShapeQuarter;
import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.render.model.LayerModel;
import com.iafenvoy.create.shape.data.ShapeInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShapeItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final LayerModel model = new LayerModel(LayerModel.createBodyLayer().bakeRoot());

    @SuppressWarnings("DataFlowIssue")
    public ShapeItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext transformType, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(1, 0, 1);
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
        if (info == null) return;
        List<ShapeInfo.Layer> layers = info.layers();
        for (int i = 0, layersSize = layers.size(); i < layersSize; i++) {
            ShapeInfo.Layer layer = layers.get(i);
            poseStack.pushPose();
            float scale = Math.max(0.1F, 0.9F - i * 0.22F);
            poseStack.scale(scale, scale, scale);
            ShapeQuarter.stream().map(x -> layer.parts().get(x)).forEach(x -> this.renderPart(poseStack, buffer, x, light, overlay));
            poseStack.popPose();
            poseStack.translate(0, 0.001, 0);
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
