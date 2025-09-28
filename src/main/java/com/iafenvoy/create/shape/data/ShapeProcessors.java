package com.iafenvoy.create.shape.data;

import com.google.common.collect.ImmutableList;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class ShapeProcessors {
    //Cutter
    public static List<ShapeInfo> cutVertical(ShapeInfo info) {
        return Stream.of(
                new ShapeInfo(info.layers().stream().map(x -> x.collect(ShapeQuarter.BOTTOM_LEFT, ShapeQuarter.TOP_LEFT)).toList()),
                new ShapeInfo(info.layers().stream().map(x -> x.collect(ShapeQuarter.TOP_RIGHT, ShapeQuarter.BOTTOM_RIGHT)).toList())
        ).filter(x -> !x.isEmpty()).toList();
    }

    public static List<ShapeInfo> cutHorizontal(ShapeInfo info) {
        return Stream.of(
                new ShapeInfo(info.layers().stream().map(x -> x.collect(ShapeQuarter.TOP_RIGHT, ShapeQuarter.TOP_LEFT)).toList()),
                new ShapeInfo(info.layers().stream().map(x -> x.collect(ShapeQuarter.BOTTOM_RIGHT, ShapeQuarter.BOTTOM_LEFT)).toList())
        ).filter(x -> !x.isEmpty()).toList();
    }

    public static List<ShapeInfo> cutQuarter(ShapeInfo info) {
        return Stream.of(
                new ShapeInfo(info.layers().stream().map(x -> x.collect(ShapeQuarter.TOP_RIGHT)).toList()),
                new ShapeInfo(info.layers().stream().map(x -> x.collect(ShapeQuarter.BOTTOM_RIGHT)).toList()),
                new ShapeInfo(info.layers().stream().map(x -> x.collect(ShapeQuarter.BOTTOM_LEFT)).toList()),
                new ShapeInfo(info.layers().stream().map(x -> x.collect(ShapeQuarter.TOP_LEFT)).toList())
        ).filter(x -> !x.isEmpty()).toList();
    }

    //Rotator
    public static ShapeInfo rotateClockwise(ShapeInfo info) {
        return new ShapeInfo(info.layers().stream().map(x -> x.rotate(q -> q.cycle(false))).toList());
    }

    public static ShapeInfo rotateCounterclockwise(ShapeInfo info) {
        return new ShapeInfo(info.layers().stream().map(x -> x.rotate(q -> q.cycle(true))).toList());
    }

    public static ShapeInfo rotate180(ShapeInfo info) {
        return new ShapeInfo(info.layers().stream().map(x -> x.rotate(q -> q.cycle(false).cycle(false))).toList());
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
    public static ShapeInfo color(ShapeInfo info, ShapeColor color) {
        return new ShapeInfo(info.layers().stream().map(x -> x.withColor(color)).toList());
    }

    public static ShapeInfo color(ShapeInfo info, EnumMap<ShapeQuarter, ShapeColor> colors) {
        return new ShapeInfo(info.layers().stream().map(x -> x.withColor(colors)).toList());
    }
}
