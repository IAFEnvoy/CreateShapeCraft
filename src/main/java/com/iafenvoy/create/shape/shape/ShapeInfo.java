package com.iafenvoy.create.shape.shape;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

//https://viewer.shapez.io/
public record ShapeInfo(List<Layer> layers) {
    public static final int MAX_LAYERS = 4;
    public static final String EMPTY_SLUG = "-", LAYER_SEPARATOR = ":";
    public static final Codec<ShapeInfo> CODEC = Codec.STRING.xmap(ShapeInfo::parse, ShapeInfo::toString);
    public static final StreamCodec<ByteBuf, ShapeInfo> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(ShapeInfo::parse, ShapeInfo::toString);
    public static final ShapeInfo DEFAULT = parse("CuCuCuCu");

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

    public record Layer(EnumMap<Quarter, Part> parts) {
        public static final Layer EMPTY = new Layer(x -> Part.EMPTY);

        public Layer(Function<Quarter, Part> function) {
            this(Util.make(new EnumMap<>(Quarter.class), map -> Quarter.stream().forEach(x -> map.put(x, function.apply(x)))));
        }

        @Override
        @NotNull
        public String toString() {
            return Quarter.stream().map(this.parts::get).map(Part::toString).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append).toString();
        }

        @NotNull
        public static Layer parse(String key) {
            if (key.length() != 8) return EMPTY;
            return new Layer(x -> Part.parse(key.substring(x.ordinal() * 2, x.ordinal() * 2 + 2)));
        }

        public Layer collect(Quarter... quarters) {
            List<Quarter> list = List.of(quarters);
            return new Layer(x -> list.contains(x) ? this.parts.get(x) : Part.EMPTY);
        }

        public Layer withColor(Color color) {
            return this.withColor(Util.make(new EnumMap<>(Quarter.class), map -> Quarter.stream().forEach(x -> map.put(x, color))));
        }

        public Layer withColor(EnumMap<Quarter, Color> colors) {
            return new Layer(x -> this.parts.get(x).withColor(colors.get(x)));
        }

        public Layer rotate(Function<Quarter, Quarter> mapping) {
            return new Layer(x -> this.parts.get(mapping.apply(x)));
        }

        public static Optional<Layer> combine(Layer l1, Layer l2) {
            return l1 == null || l2 == null || Quarter.stream().anyMatch(x -> !l1.parts.get(x).isEmpty() && !l2.parts.get(x).isEmpty()) ?
                    Optional.empty() :
                    Optional.of(new Layer(x -> l1.parts.get(x).isEmpty() ? l2.parts.get(x) : l1.parts.get(x)));
        }
    }

    public record Part(Shape shape, Color color) {
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
            return key.length() != 2 ? EMPTY : new Part(Shape.fromSlug(key.substring(0, 1)), Color.fromSlug(key.substring(1, 2)));
        }

        public Part withColor(Color color) {
            return new Part(this.shape, color);
        }
    }

    public enum Shape {
        CIRCLE("circle", "C"),
        RECTANGLE("rectangle", "R"),
        WIND_MILL("wind_mill", "W"),
        STAR("star", "S");
        private final String texture, slug;

        Shape(String texture, String slug) {
            this.texture = texture;
            this.slug = slug;
        }

        public ResourceLocation getBorderTexture() {
            return ResourceLocation.tryBuild(CreateShapeCraft.MOD_ID, "textures/shape/%s_border.png".formatted(this.texture));
        }

        public ResourceLocation getInnerTexture() {
            return ResourceLocation.tryBuild(CreateShapeCraft.MOD_ID, "textures/shape/%s_inner.png".formatted(this.texture));
        }

        public String getSlug() {
            return this.slug;
        }

        @Nullable
        public static Shape fromSlug(String slug) {
            return Arrays.stream(values()).filter(x -> Objects.equals(x.getSlug(), slug)).findAny().orElse(null);
        }
    }

    public enum Color {
        RED(ChatFormatting.RED, "r"),
        GREEN(ChatFormatting.GREEN, "g"),
        BLUE(ChatFormatting.BLUE, "b"),
        YELLOW(ChatFormatting.YELLOW, "y"),
        PURPLE(ChatFormatting.LIGHT_PURPLE, "p"),
        CYAN(ChatFormatting.AQUA, "c"),
        UNCOLORED(ChatFormatting.GRAY, "u"),
        WHITE(ChatFormatting.WHITE, "w");
        private final ChatFormatting color;
        private final String slug;

        Color(ChatFormatting color, String slug) {
            this.color = color;
            this.slug = slug;
        }

        public ChatFormatting getFormatting() {
            return this.color;
        }

        public int getColor() {
            Integer i = this.color.getColor();
            return i == null ? -1 : i;
        }

        public String getSlug() {
            return this.slug;
        }

        @Nullable
        public static Color fromSlug(String slug) {
            return Arrays.stream(values()).filter(x -> Objects.equals(x.getSlug(), slug)).findAny().orElse(null);
        }
    }

    public enum Quarter {
        TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT;

        public static Stream<Quarter> stream() {
            return Stream.of(values());
        }

        public Quarter cycle(boolean clockwise) {
            Quarter[] types = values();
            return types[(this.ordinal() + (clockwise ? 1 : types.length - 1)) % types.length];
        }
    }
}
