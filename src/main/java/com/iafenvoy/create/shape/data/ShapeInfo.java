package com.iafenvoy.create.shape.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.Util;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

//https://viewer.shapez.io/
public record ShapeInfo(List<Layer> layers) {
    public static final int MAX_LAYERS = 4;
    public static final String EMPTY_SLUG = "-", LAYER_SEPARATOR = ":";
    public static final Codec<ShapeInfo> CODEC = Codec.STRING.xmap(ShapeInfo::parse, ShapeInfo::toString);
    public static final StreamCodec<ByteBuf, ShapeInfo> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(ShapeInfo::parse, ShapeInfo::toString);
    public static final ShapeInfo EMPTY = parse(""), DEFAULT = parse("CuCuCuCu");

    public ShapeInfo(List<Layer> layers) {
        this.layers = layers.stream().filter(x -> !x.isEmpty()).toList();
    }

    @Override
    @NotNull
    public String toString() {
        return String.join(LAYER_SEPARATOR, this.layers.stream().map(Layer::toString).toList());
    }

    @NotNull
    public static ShapeInfo parse(String key) {
        return new ShapeInfo(Arrays.stream(key.split(LAYER_SEPARATOR)).map(Layer::parse).toList());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ShapeInfo(List<Layer> layers1))) return false;
        if (this.layers.size() != layers1.size()) return false;
        for (int i = 0; i < this.layers.size(); i++)
            if (!Objects.equals(this.layers.get(i), layers1.get(i)))
                return false;
        return true;
    }

    public boolean isEmpty() {
        return this == EMPTY || this.layers.isEmpty();
    }

    public record Layer(EnumMap<ShapeQuarter, Part> parts) {
        public static final Layer EMPTY = new Layer(x -> Part.EMPTY);

        public Layer(Function<ShapeQuarter, Part> function) {
            this(Util.make(new EnumMap<>(ShapeQuarter.class), map -> ShapeQuarter.stream().forEach(x -> map.put(x, function.apply(x)))));
        }

        public boolean isEmpty() {
            return this == EMPTY || this.parts.values().stream().allMatch(Part::isEmpty);
        }

        @Override
        @NotNull
        public String toString() {
            return ShapeQuarter.stream().map(this.parts::get).map(Part::toString).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append).toString();
        }

        @NotNull
        public static Layer parse(String key) {
            if (key.length() != 8) return EMPTY;
            return new Layer(x -> Part.parse(key.substring(x.ordinal() * 2, x.ordinal() * 2 + 2)));
        }

        public Layer collect(ShapeQuarter... quarters) {
            List<ShapeQuarter> list = List.of(quarters);
            return new Layer(x -> list.contains(x) ? this.parts.get(x) : Part.EMPTY);
        }

        public Layer withColor(ShapeColor color) {
            return this.withColor(Util.make(new EnumMap<>(ShapeQuarter.class), map -> ShapeQuarter.stream().forEach(x -> map.put(x, color))));
        }

        public Layer withColor(EnumMap<ShapeQuarter, ShapeColor> colors) {
            return new Layer(x -> this.parts.get(x).withColor(colors.get(x)));
        }

        public Layer rotate(Function<ShapeQuarter, ShapeQuarter> mapping) {
            return new Layer(x -> this.parts.get(mapping.apply(x)));
        }

        public static Optional<Layer> combine(Layer l1, Layer l2) {
            return l1 == null || l2 == null || ShapeQuarter.stream().anyMatch(x -> !l1.parts.get(x).isEmpty() && !l2.parts.get(x).isEmpty()) ?
                    Optional.empty() :
                    Optional.of(new Layer(x -> l1.parts.get(x).isEmpty() ? l2.parts.get(x) : l1.parts.get(x)));
        }
    }

    public record Part(ShapePartType shape, ShapeColor color) {
        public static final Part EMPTY = new Part(null, null);

        public boolean isEmpty() {
            return this == EMPTY || this.shape == null || this.color == null;
        }

        @Override
        @NotNull
        public String toString() {
            return (this.shape == null ? EMPTY_SLUG : this.shape.getSlug()) + (this.color == null ? EMPTY_SLUG : this.color.getSlug());
        }

        @NotNull
        public static Part parse(String key) {
            return key.length() != 2 ? EMPTY : new Part(ShapePartType.fromSlug(key.substring(0, 1)), ShapeColor.fromSlug(key.substring(1, 2)));
        }

        public Part withColor(ShapeColor color) {
            return new Part(this.shape, color);
        }
    }
}
