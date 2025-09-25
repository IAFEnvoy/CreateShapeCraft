package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.item.ShapeItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class CSCItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(CreateShapeCraft.MOD_ID);

    public static final DeferredItem<ShapeItem> SHAPE = register("shape", ShapeItem::new);

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        DeferredItem<T> r = REGISTRY.register(id, obj);
        CSCCreativeModeTabs.ITEMS.add(r);
        return r;
    }
}
