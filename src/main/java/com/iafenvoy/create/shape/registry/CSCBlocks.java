package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.item.block.*;
import com.iafenvoy.create.shape.shape.ShapeProcessors;
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
    public static final DeferredBlock<SingleProcessMachineBlock> ROTATOR_CW = register("rotator_cw", () -> new SingleProcessMachineBlock(ShapeProcessors::rotateClockwise));
    public static final DeferredBlock<SingleProcessMachineBlock> ROTATOR_CCW = register("rotator_ccw", () -> new SingleProcessMachineBlock(ShapeProcessors::rotateCounterclockwise));
    public static final DeferredBlock<SingleProcessMachineBlock> ROTATOR_180 = register("rotator_180", () -> new SingleProcessMachineBlock(ShapeProcessors::rotate180));
    public static final DeferredBlock<DyeProcessMachineBlock> DYE = register("dye", () -> new DyeProcessMachineBlock(ShapeProcessors::color));
    public static final DeferredBlock<StackProcessMachineBlock> STACK = register("stack", () -> new StackProcessMachineBlock(ShapeProcessors::stack));

    public static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj) {
        return register(id, obj, block -> new BlockItem(block, new Item.Properties()));
    }

    public static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj, Function<Block, Item> itemGetter) {
        DeferredBlock<T> r = REGISTRY.register(id, obj);
        CSCItems.register(id, () -> itemGetter.apply(r.get()));
        return r;
    }
}
