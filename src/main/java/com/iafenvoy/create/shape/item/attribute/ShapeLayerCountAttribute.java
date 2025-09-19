package com.iafenvoy.create.shape.item.attribute;

import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ShapeLayerCountAttribute(int layer) implements ItemAttribute {
    public static final MapCodec<ShapeLayerCountAttribute> CODEC = Codec.INT.xmap(ShapeLayerCountAttribute::new, ShapeLayerCountAttribute::layer).fieldOf("value");
    public static final StreamCodec<ByteBuf, ShapeLayerCountAttribute> STREAM_CODEC = ByteBufCodecs.INT.map(ShapeLayerCountAttribute::new, ShapeLayerCountAttribute::layer);

    @Override
    public boolean appliesTo(ItemStack stack, Level world) {
        ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
        return info != null && info.layers().size() == this.layer;
    }

    @Override
    public ItemAttributeType getType() {
        return Type.INSTANCE;
    }

    @Override
    public String getTranslationKey() {
        return "shape_layer_count";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new String[]{String.valueOf(this.layer)};
    }

    public enum Type implements ItemAttributeType {
        INSTANCE;

        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new ShapeLayerCountAttribute(0);
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
            return info == null ? List.of() : List.of(new ShapeLayerCountAttribute(info.layers().size()));
        }

        @Override
        public MapCodec<? extends ItemAttribute> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ? extends ItemAttribute> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
