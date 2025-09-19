package com.iafenvoy.create.shape.item.block.slot;

import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.world.phys.Vec3;

public class ShapeGeneratorFilterSlot extends ValueBoxTransform.Sided {
    @Override
    protected Vec3 getSouthLocation() {
        return VecHelper.voxelSpace(8, 10, 15.5);
    }
}
