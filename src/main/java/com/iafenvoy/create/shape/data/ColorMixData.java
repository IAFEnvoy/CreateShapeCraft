package com.iafenvoy.create.shape.data;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ColorMixData {
    private static final List<MixHolder> MIXES = new LinkedList<>();

    public static void register(ShapeInfo.Color input1, ShapeInfo.Color input2, ShapeInfo.Color output) {
        MIXES.add(new MixHolder(input1, input2, output));
    }

    @Nullable
    public static ShapeInfo.Color getOutput(ShapeInfo.Color input1, ShapeInfo.Color input2) {
        return MIXES.stream().filter(x -> x.match(input1, input2)).findAny().map(MixHolder::output).orElse(null);
    }

    private record MixHolder(ShapeInfo.Color input1, ShapeInfo.Color input2, ShapeInfo.Color output) {
        public boolean match(ShapeInfo.Color input1, ShapeInfo.Color input2) {
            return this.input1 == input1 && this.input2 == input2;
        }
    }
}
