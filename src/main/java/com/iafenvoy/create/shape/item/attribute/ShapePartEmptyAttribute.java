package com.iafenvoy.create.shape.item.attribute;

import com.iafenvoy.create.shape.data.ShapeQuarter;
import com.iafenvoy.create.shape.registry.CSCDataComponents;
import com.iafenvoy.create.shape.data.ShapeInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public record ShapePartEmptyAttribute(ShapeQuarter quarter, boolean empty) implements ItemAttribute {
    public static final MapCodec<ShapePartEmptyAttribute> CODEC = RecordCodecBuilder.<ShapePartEmptyAttribute>create(i -> i.group(
            ShapeQuarter.CODEC.fieldOf("quarter").forGetter(ShapePartEmptyAttribute::quarter),
            Codec.BOOL.fieldOf("empty").forGetter(ShapePartEmptyAttribute::empty)
    ).apply(i, ShapePartEmptyAttribute::new)).fieldOf("value");
    public static final StreamCodec<ByteBuf, ShapePartEmptyAttribute> STREAM_CODEC = StreamCodec.composite(
            ShapeQuarter.STREAM_CODEC, ShapePartEmptyAttribute::quarter,
            ByteBufCodecs.BOOL, ShapePartEmptyAttribute::empty,
            ShapePartEmptyAttribute::new
    );

    @Override
    public boolean appliesTo(ItemStack stack, Level world) {
        ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
        return info != null && info.layers().stream().allMatch(x -> x.parts().get(this.quarter).isEmpty()) == this.empty;
    }

    @Override
    public ItemAttributeType getType() {
        return Type.INSTANCE;
    }

    @Override
    public String getTranslationKey() {
        return "shape_part_" + (this.empty ? "empty" : "non_empty");
    }

    @Override
    public Object[] getTranslationParameters() {
        return new Component[]{Component.translatable(this.quarter.getTranslateKey())};
    }

    public enum Type implements ItemAttributeType {
        INSTANCE;

        @Override
        public @NotNull ItemAttribute createAttribute() {
            return new ShapePartEmptyAttribute(ShapeQuarter.TOP_RIGHT, true);
        }

        @Override
        public List<ItemAttribute> getAllAttributes(ItemStack stack, Level level) {
            ShapeInfo info = stack.get(CSCDataComponents.SHAPE);
            List<ItemAttribute> attributes = new LinkedList<>();
            if (info != null)
                ShapeQuarter.stream().map(x -> new ShapePartEmptyAttribute(x, info.layers().stream().allMatch(y -> y.parts().get(x).isEmpty()))).forEach(attributes::add);
            return attributes;
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
