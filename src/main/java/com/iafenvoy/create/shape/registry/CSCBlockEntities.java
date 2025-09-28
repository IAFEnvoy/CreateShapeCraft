package com.iafenvoy.create.shape.registry;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.item.block.entity.*;
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
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShapeStorageBlockEntity>> SHAPE_STORAGE = register("shape_storge", () -> BlockEntityType.Builder.of(ShapeStorageBlockEntity::new, CSCBlocks.SHAPE_STORAGE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SingleProcessMachineBlockEntity>> SINGLE_PROCESS_MACHINE = register("single_process_machine", () -> BlockEntityType.Builder.of(SingleProcessMachineBlockEntity::new, CSCBlocks.ROTATOR_CW.get(), CSCBlocks.ROTATOR_CCW.get(), CSCBlocks.ROTATOR_180.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DyeProcessMachineBlockEntity>> DYE_PROCESS_MACHINE = register("dye_process_machine", () -> BlockEntityType.Builder.of(DyeProcessMachineBlockEntity::new, CSCBlocks.DYER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StackProcessMachineBlockEntity>> STACK_PROCESS_MACHINE = register("stack_process_machine", () -> BlockEntityType.Builder.of(StackProcessMachineBlockEntity::new, CSCBlocks.STACKER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CutProcessMachineBlockEntity>> CUT_PROCESS_MACHINE = register("cut_process_machine", () -> BlockEntityType.Builder.of(CutProcessMachineBlockEntity::new, CSCBlocks.CUTTER_HORIZONTAL.get(), CSCBlocks.CUTTER_VERTICAL.get(), CSCBlocks.CUTTER_QUARTER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ColorMixerBlockEntity>> COLOR_MIXER = register("color_mixer", () -> BlockEntityType.Builder.of(ColorMixerBlockEntity::new, CSCBlocks.COLOR_MIXER.get()).build(null));

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}
