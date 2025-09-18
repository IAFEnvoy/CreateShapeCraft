package com.iafenvoy.create.shape.item;

import com.iafenvoy.create.shape.shape.ShapeInfo;
import net.minecraft.world.item.Item;

public class ShapeDyeItem extends Item {
    private final ShapeInfo.Color color;

    public ShapeDyeItem(ShapeInfo.Color color) {
        super(new Properties());
        this.color = color;
    }

    public ShapeInfo.Color getColor() {
        return this.color;
    }
}
