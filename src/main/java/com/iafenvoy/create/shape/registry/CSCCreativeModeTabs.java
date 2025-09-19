package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.item.ShapeItem;
import com.iafenvoy.create.shape.shape.BuiltinFilters;
import com.iafenvoy.create.shape.shape.BuiltinShapes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class CSCCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateShapeCraft.MOD_ID);

    public static final List<DeferredItem<?>> ITEMS = new LinkedList<>();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = register("main", () -> CreativeModeTab.builder()
            .icon(() -> ShapeItem.fromInfo(BuiltinShapes.ROCKET))
            .title(Component.translatable("itemGroup.%s.main".formatted(CreateShapeCraft.MOD_ID)))
            .displayItems((params, output) -> {
                output.accept(ShapeItem.fromInfo(BuiltinShapes.LOGO));
                output.accept(ShapeItem.fromInfo(BuiltinShapes.ROCKET));
                BuiltinShapes.BASE.forEach(x -> output.accept(ShapeItem.fromInfo(x)));
                ITEMS.forEach(output::accept);
                BuiltinFilters.CORNERS.stream().map(BuiltinFilters::toFilterItem).forEach(output::accept);
            })
            .build());

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> register(String id, Supplier<CreativeModeTab> obj) {
        return REGISTRY.register(id, obj);
    }
}