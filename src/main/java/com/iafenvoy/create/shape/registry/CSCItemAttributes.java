package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.item.attribute.ShapeKeyAttribute;
import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class CSCItemAttributes {
    public static final DeferredRegister<ItemAttributeType> REGISTRY = DeferredRegister.create(CreateRegistries.ITEM_ATTRIBUTE_TYPE, CreateShapeCraft.MOD_ID);

    public static final DeferredHolder<ItemAttributeType, ItemAttributeType> SHAPE_KEY = register("shape_key", () -> ShapeKeyAttribute.Type.INSTANCE);

    public static <T extends ItemAttributeType> DeferredHolder<ItemAttributeType, T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
