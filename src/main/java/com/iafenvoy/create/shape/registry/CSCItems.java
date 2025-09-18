package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.item.ShapeDyeItem;
import com.iafenvoy.create.shape.item.ShapeItem;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class CSCItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(CreateShapeCraft.MOD_ID);

    public static final DeferredItem<ShapeItem> SHAPE = register("shape", ShapeItem::new);
    //Dye
    public static final DeferredItem<ShapeDyeItem> RED_SHAPE_DYE = register("red_shape_dye", () -> new ShapeDyeItem(ShapeInfo.Color.RED));
    public static final DeferredItem<ShapeDyeItem> GREEN_SHAPE_DYE = register("green_shape_dye", () -> new ShapeDyeItem(ShapeInfo.Color.GREEN));
    public static final DeferredItem<ShapeDyeItem> BLUE_SHAPE_DYE = register("blue_shape_dye", () -> new ShapeDyeItem(ShapeInfo.Color.BLUE));
    public static final DeferredItem<ShapeDyeItem> YELLOW_SHAPE_DYE = register("yellow_shape_dye", () -> new ShapeDyeItem(ShapeInfo.Color.YELLOW));
    public static final DeferredItem<ShapeDyeItem> PURPLE_SHAPE_DYE = register("purple_shape_dye", () -> new ShapeDyeItem(ShapeInfo.Color.PURPLE));
    public static final DeferredItem<ShapeDyeItem> CYAN_SHAPE_DYE = register("cyan_shape_dye", () -> new ShapeDyeItem(ShapeInfo.Color.CYAN));
    public static final DeferredItem<ShapeDyeItem> UNCOLORED_SHAPE_DYE = register("uncolored_shape_dye", () -> new ShapeDyeItem(ShapeInfo.Color.UNCOLORED));
    public static final DeferredItem<ShapeDyeItem> WHITE_SHAPE_DYE = register("white_shape_dye", () -> new ShapeDyeItem(ShapeInfo.Color.WHITE));

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        DeferredItem<T> r = REGISTRY.register(id, obj);
        CSCCreativeModeTabs.ITEMS.add(r);
        return r;
    }
}
