package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.item.block.ShapeDestroyerBlock;
import com.iafenvoy.create.shape.item.block.ShapeGeneratorBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public final class CSCBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(CreateShapeCraft.MOD_ID);

    public static final DeferredBlock<ShapeGeneratorBlock> SHAPE_GENERATOR = register("shape_generator", ShapeGeneratorBlock::new);
    public static final DeferredBlock<ShapeDestroyerBlock> SHAPE_DESTROYER = register("shape_destroyer", ShapeDestroyerBlock::new);

    public static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj) {
        return register(id, obj, block -> new BlockItem(block, new Item.Properties()));
    }

    public static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj, Function<Block, Item> itemGetter) {
        DeferredBlock<T> r = REGISTRY.register(id, obj);
        CSCItems.register(id, () -> itemGetter.apply(r.get()));
        return r;
    }
}
