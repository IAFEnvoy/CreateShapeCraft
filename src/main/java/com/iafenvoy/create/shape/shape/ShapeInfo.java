package com.iafenvoy.create.shape.shape;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//https://viewer.shapez.io/
public record ShapeInfo(List<Layer> layers) {
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

    public record Layer(Part topRight, Part bottomRight, Part bottomLeft, Part topLeft) {
        public static final Layer EMPTY = new Layer(Part.EMPTY, Part.EMPTY, Part.EMPTY, Part.EMPTY);

        @Override
        @NotNull
        public String toString() {
            return this.topRight.toString() + this.bottomRight.toString() + this.bottomLeft.toString() + this.topLeft.toString();
        }

        @NotNull
        public static Layer parse(String key) {
            if (key.length() != 8) return EMPTY;
            return new Layer(Part.parse(key.substring(0, 2)), Part.parse(key.substring(2, 4)), Part.parse(key.substring(4, 6)), Part.parse(key.substring(6, 8)));
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
}
