package com.iafenvoy.create.shape.data;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.stream.Stream;

public enum ShapeQuarter implements StringRepresentable {
    TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT;
    public static final Codec<ShapeQuarter> CODEC = StringRepresentable.fromEnum(ShapeQuarter::values);
    public static final StreamCodec<ByteBuf, ShapeQuarter> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public static Stream<ShapeQuarter> stream() {
        return Stream.of(values());
    }

    public ShapeQuarter cycle(boolean clockwise) {
        ShapeQuarter[] types = values();
        return types[(this.ordinal() + (clockwise ? 1 : types.length - 1)) % types.length];
    }

    public String getTranslateKey() {
        return "quarter.%s.%s".formatted(CreateShapeCraft.MOD_ID, this.getSerializedName());
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
