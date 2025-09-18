package com.iafenvoy.create.shape.shape;

import com.google.common.collect.ImmutableList;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/*
 * Basic processing machines in ShapeZ
 * 1.Belt (Provided by Create)
 * 2.Combiner&Separator (Provided by Create)
 * 3.Tunnel (Useless in 3D game)
 * 4.Shape Generator (Implemented by block)
 * 5.Cutter (half&quarter)
 * 6.Rotator (90&180&270)
 * 7.Stacker
 * 8.Color mixer (Provided by vanilla)
 * 9.Dyer
 * 0.Shape Destroyer (Implemented by block)
 *
 * Functional machines
 * 1.Storage (Implemented by block)
 * 2.Counter (Implemented by block)
 * 3.Switch (Provided by Create)
 * 4.Filter (Provided by Create)
 * 5.Screen (Useless)
 * */
public final class ShapeProcessors {
    //Cutter
    public static PairResult cutVertical(ShapeInfo info) {
        return new PairResult(
                new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(ShapeInfo.Part.EMPTY, ShapeInfo.Part.EMPTY, x.bottomLeft(), x.topLeft())).toList()),
                new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(x.topRight(), x.bottomRight(), ShapeInfo.Part.EMPTY, ShapeInfo.Part.EMPTY)).toList())
        );
    }

    public static PairResult cutHorizontal(ShapeInfo info) {
        return new PairResult(
                new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(x.topRight(), ShapeInfo.Part.EMPTY, ShapeInfo.Part.EMPTY, x.topLeft())).toList()),
                new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(ShapeInfo.Part.EMPTY, x.bottomRight(), x.bottomLeft(), ShapeInfo.Part.EMPTY)).toList())
        );
    }

    public static QuaternionResult cutQuarter(ShapeInfo info) {
        return new QuaternionResult(
                new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(x.topRight(), ShapeInfo.Part.EMPTY, ShapeInfo.Part.EMPTY, ShapeInfo.Part.EMPTY)).toList()),
                new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(ShapeInfo.Part.EMPTY, x.bottomRight(), ShapeInfo.Part.EMPTY, ShapeInfo.Part.EMPTY)).toList()),
                new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(ShapeInfo.Part.EMPTY, ShapeInfo.Part.EMPTY, x.bottomLeft(), ShapeInfo.Part.EMPTY)).toList()),
                new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(ShapeInfo.Part.EMPTY, ShapeInfo.Part.EMPTY, ShapeInfo.Part.EMPTY, x.topLeft())).toList())
        );
    }

    //Rotator
    public static ShapeInfo rotateClockwise(ShapeInfo info) {
        return new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(x.topLeft(), x.topRight(), x.bottomRight(), x.bottomLeft())).toList());
    }

    public static ShapeInfo rotateCounterclockwise(ShapeInfo info) {
        return new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(x.bottomRight(), x.bottomLeft(), x.topLeft(), x.topRight())).toList());
    }

    public static ShapeInfo rotate180(ShapeInfo info) {
        return new ShapeInfo(info.layers().stream().map(x -> new ShapeInfo.Layer(x.bottomLeft(), x.topLeft(), x.topRight(), x.bottomRight())).toList());
    }

    //Stacker
    public static ShapeInfo stack(ShapeInfo base, ShapeInfo upper) {
        List<ShapeInfo.Layer> baseLayers = base.layers(), upperLayers = upper.layers();
        int maxCoincide = Math.min(baseLayers.size(), upperLayers.size());
        ShapeInfo result = new ShapeInfo(ImmutableList.<ShapeInfo.Layer>builder().addAll(baseLayers).addAll(upperLayers).build());
        for (int i = 1; i <= maxCoincide; i++) {
            int upperHeight = baseLayers.size() - i;
            List<ShapeInfo.Layer> layers = new LinkedList<>();
            for (int j = 0; j < upperHeight; j++) layers.add(baseLayers.get(j));
            boolean failed = false;
            for (int j = 0; j < i; j++) {
                Optional<ShapeInfo.Layer> optional = ShapeInfo.Layer.combine(baseLayers.get(j + upperHeight), upperLayers.get(j));
                if (optional.isEmpty()) {
                    failed = true;
                    break;
                }
                layers.add(optional.get());
            }
            if (failed) break;
            for (int j = i; j < upperLayers.size(); j++) layers.add(upperLayers.get(j));
            result = new ShapeInfo(List.copyOf(layers));
        }
        return result;
    }

    //Dyer
    public static ShapeInfo color(ShapeInfo info, ShapeInfo.Color color) {
        return new ShapeInfo(info.layers().stream().map(x -> x.withColor(color)).toList());
    }

    public static ShapeInfo color(ShapeInfo info, ShapeInfo.Color topRightColor, ShapeInfo.Color bottomRightColor, ShapeInfo.Color bottomLeftColor, ShapeInfo.Color topLeftColor) {
        return new ShapeInfo(info.layers().stream().map(x -> x.withColor(topRightColor, bottomRightColor, bottomLeftColor, topLeftColor)).toList());
    }

    //Data Structure
    public record PairResult(ShapeInfo first, ShapeInfo second) {
        public List<ShapeInfo> collect() {
            return List.of(this.first, this.second);
        }
    }

    public record QuaternionResult(ShapeInfo first, ShapeInfo second, ShapeInfo third, ShapeInfo forth) {
        public List<ShapeInfo> collect() {
            return List.of(this.first, this.second, this.third, this.forth);
        }
    }
}
