package com.iafenvoy.create.shape.data;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ColorMixData {
    private static final List<MixHolder> MIXES = new LinkedList<>();

    public static void register(ShapeColor input1, ShapeColor input2, ShapeColor output) {
        MIXES.add(new MixHolder(input1, input2, output));
    }

    @Nullable
    public static ShapeColor getOutput(ShapeColor input1, ShapeColor input2) {
        return MIXES.stream().filter(x -> x.match(input1, input2)).findAny().map(MixHolder::output).orElse(null);
    }

    static {
        register(ShapeColor.RED, ShapeColor.GREEN, ShapeColor.YELLOW);
        register(ShapeColor.RED, ShapeColor.BLUE, ShapeColor.PURPLE);
        register(ShapeColor.GREEN, ShapeColor.BLUE, ShapeColor.CYAN);

        register(ShapeColor.BLUE, ShapeColor.YELLOW, ShapeColor.WHITE);
        register(ShapeColor.GREEN, ShapeColor.PURPLE, ShapeColor.WHITE);
        register(ShapeColor.RED, ShapeColor.CYAN, ShapeColor.WHITE);
    }

    private record MixHolder(ShapeColor input1, ShapeColor input2, ShapeColor output) {
        public boolean match(ShapeColor input1, ShapeColor input2) {
            return this.input1 == input1 && this.input2 == input2 || this.input1 == input2 && this.input2 == input1;
        }
    }
}
