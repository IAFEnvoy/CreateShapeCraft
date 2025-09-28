package com.iafenvoy.create.shape.data;

import com.iafenvoy.create.shape.registry.CSCTags;
import net.minecraft.ChatFormatting;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import plus.dragons.createdragonsplus.common.registry.CDPFluids;

import java.util.Arrays;
import java.util.Objects;

public enum ShapeColor {
    RED(ChatFormatting.RED, "r", CSCTags.RED_DYES, DyeColor.RED),
    GREEN(ChatFormatting.GREEN, "g", CSCTags.GREEN_DYES, DyeColor.GREEN),
    BLUE(ChatFormatting.BLUE, "b", CSCTags.BLUE_DYES, DyeColor.BLUE),
    YELLOW(ChatFormatting.YELLOW, "y", CSCTags.YELLOW_DYES, DyeColor.YELLOW),
    PURPLE(ChatFormatting.LIGHT_PURPLE, "p", CSCTags.PURPLE_DYES, DyeColor.PURPLE),
    CYAN(ChatFormatting.AQUA, "c", CSCTags.CYAN_DYES, DyeColor.CYAN),
    UNCOLORED(ChatFormatting.GRAY, "u", CSCTags.UNCOLORED_DYES, null),
    WHITE(ChatFormatting.WHITE, "w", CSCTags.WHITE_DYES, DyeColor.WHITE);
    private final ChatFormatting color;
    private final String slug;
    private final TagKey<Fluid> fluidTag;
    @Nullable
    private final DyeColor dyeColor;

    ShapeColor(ChatFormatting color, String slug, TagKey<Fluid> fluidTag, @Nullable DyeColor dyeColor) {
        this.color = color;
        this.slug = slug;
        this.fluidTag = fluidTag;
        this.dyeColor = dyeColor;
    }

    public ChatFormatting getFormatting() {
        return this.color;
    }

    public Fluid getFluid() {
        return this.dyeColor == null ? Fluids.WATER : CDPFluids.DYES_BY_COLOR.get(this.dyeColor).get();
    }

    public int getColor() {
        Integer i = this.color.getColor();
        return i == null ? -1 : i;
    }

    public String getSlug() {
        return this.slug;
    }

    @Nullable
    public static ShapeColor fromSlug(String slug) {
        return Arrays.stream(values()).filter(x -> Objects.equals(x.getSlug(), slug)).findAny().orElse(null);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    public static ShapeColor forFluid(Fluid fluid) {
        return Arrays.stream(values()).filter(x -> fluid.is(x.fluidTag)).findAny().orElse(null);
    }
}
