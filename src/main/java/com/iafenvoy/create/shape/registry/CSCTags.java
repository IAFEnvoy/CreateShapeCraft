package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public final class CSCTags {
    public static final TagKey<Fluid> RED_DYES = fluid("red_dyes");
    public static final TagKey<Fluid> GREEN_DYES = fluid("green_dyes");
    public static final TagKey<Fluid> BLUE_DYES = fluid("blue_dyes");
    public static final TagKey<Fluid> YELLOW_DYES = fluid("yellow_dyes");
    public static final TagKey<Fluid> PURPLE_DYES = fluid("purple_dyes");
    public static final TagKey<Fluid> CYAN_DYES = fluid("cyan_dyes");
    public static final TagKey<Fluid> UNCOLORED_DYES = fluid("uncolored_dyes");
    public static final TagKey<Fluid> WHITE_DYES = fluid("white_dyes");

    public static final TagKey<Fluid> SHAPE_DYES = fluid("shape_dyes");

    public static TagKey<Fluid> fluid(String id) {
        return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, id));
    }
}
