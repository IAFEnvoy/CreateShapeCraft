package com.iafenvoy.create.shape.util;

import com.iafenvoy.create.shape.registry.CSCItems;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public final class Predicates {
    public static final Predicate<ItemStack> IS_SHAPE = stack -> stack.is(CSCItems.SHAPE);
}
