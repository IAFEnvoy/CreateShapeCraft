package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.shape.ShapeInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class CSCDataComponents {
    public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CreateShapeCraft.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ShapeInfo>> SHAPE = register("shape", () -> DataComponentType.<ShapeInfo>builder().persistent(ShapeInfo.CODEC).networkSynchronized(ShapeInfo.STREAM_CODEC).build());

    public static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String id, Supplier<DataComponentType<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}
