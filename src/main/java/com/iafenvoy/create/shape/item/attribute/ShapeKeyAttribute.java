package com.iafenvoy.create.shape.item.attribute;

import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.data.ShapeInfo;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ShapeKeyAttribute(ShapeInfo info) implements ItemAttribute {
    public static final MapCodec<ShapeKeyAttribute> CODEC = ShapeInfo.CODEC.xmap(ShapeKeyAttribute::new, ShapeKeyAttribute::info).fieldOf("value");
    public static final StreamCodec<ByteBuf, ShapeKeyAttribute> STREAM_CODEC = ShapeInfo.STREAM_CODEC.map(ShapeKeyAttribute::new, ShapeKeyAttribute::info);

    @Override
    public boolean appliesTo(ItemStack stack, Level world) {
        ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
        return info != null && info.equals(this.info);
    }

    @Override
    public ItemAttributeType getType() {
        return Type.INSTANCE;
    }

    @Override
    public String getTranslationKey() {
        return "shape_key";
    }

    @Override
    public Object[] getTranslationParameters() {
        return new String[]{this.info.toString()};
    }

    public enum Type implements ItemAttributeType {
        INSTANCE;

        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new ShapeKeyAttribute(ShapeInfo.DEFAULT);
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
            return info == null ? List.of() : List.of(new ShapeKeyAttribute(info));
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
