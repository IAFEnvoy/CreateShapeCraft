package com.iafenvoy.create.shape.shape;

import java.util.List;
import java.util.stream.Stream;

public final class BuiltinShapes {
    public static final ShapeInfo LOGO = ShapeInfo.parse("RuCw--Cw:----Ru--");
    public static final ShapeInfo ROCKET = ShapeInfo.parse("CbCuCbCu:Sr------:--CrSrCr:CwCwCwCw");
    public static final List<ShapeInfo> BASE = Stream.of("CuCuCuCu", "RuRuRuRu", "WuWuWuWu", "SuSuSuSu").map(ShapeInfo::parse).toList();
}
