package com.iafenvoy.create.shape.item;

import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.registry.CSCItems;
import com.iafenvoy.create.shape.render.item.ShapeItemRenderer;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ShapeItem extends Item {
    public ShapeItem() {
        super(new Properties().component(CSCDataComponents.SHAPE, ShapeInfo.DEFAULT));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, components, flag);
        ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
        if (info != null) components.add(Component.literal(info.toString()));
    }

    @SuppressWarnings("removal")
    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new ShapeItemRenderer();
            }
        });
    }

    public static ItemStack fromKey(String key) {
        return fromInfo(ShapeInfo.parse(key));
    }

    public static ItemStack fromInfo(ShapeInfo info) {
        ItemStack stack = CSCItems.SHAPE.toStack();
        stack.set(CSCDataComponents.SHAPE, info);
        return stack;
    }
}
