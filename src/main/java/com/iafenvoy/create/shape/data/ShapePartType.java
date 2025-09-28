package com.iafenvoy.create.shape.data;

import com.iafenvoy.create.shape.CreateShapeCraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public enum ShapePartType {
    CIRCLE("circle", "C"),
    RECTANGLE("rectangle", "R"),
    WIND_MILL("wind_mill", "W"),
    STAR("star", "S");
    private final String texture, slug;

    ShapePartType(String texture, String slug) {
        this.texture = texture.toLowerCase(Locale.ROOT);
        this.slug = slug;
    }

    public ResourceLocation getBorderTexture() {
        return ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, "textures/item/shape/%s_border.png".formatted(this.texture));
    }

    public ResourceLocation getInnerTexture() {
        return ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, "textures/item/shape/%s_inner.png".formatted(this.texture));
    }

    public String getSlug() {
        return this.slug;
    }

    @Nullable
    public static ShapePartType fromSlug(String slug) {
        return Arrays.stream(values()).filter(x -> Objects.equals(x.getSlug(), slug)).findAny().orElse(null);
    }
}
