package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.item.block.entity.ShapeDestroyerBlockEntity;
import com.iafenvoy.create.shape.item.block.entity.ShapeGeneratorBlockEntity;
import com.iafenvoy.create.shape.item.block.entity.SingleProcessMachineBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("DataFlowIssue")
public final class CSCBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateShapeCraft.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShapeGeneratorBlockEntity>> SHAPE_GENERATOR = register("shape_generator", () -> BlockEntityType.Builder.of(ShapeGeneratorBlockEntity::new, CSCBlocks.SHAPE_GENERATOR.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShapeDestroyerBlockEntity>> SHAPE_DESTROYER = register("shape_destroyer", () -> BlockEntityType.Builder.of(ShapeDestroyerBlockEntity::new, CSCBlocks.SHAPE_DESTROYER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SingleProcessMachineBlockEntity>> SINGLE_PROCESS_MACHINE = register("single_process_machine", () -> BlockEntityType.Builder.of(SingleProcessMachineBlockEntity::new, CSCBlocks.ROTATOR_CW.get(), CSCBlocks.ROTATOR_CCW.get(), CSCBlocks.ROTATOR_180.get()).build(null));

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}
