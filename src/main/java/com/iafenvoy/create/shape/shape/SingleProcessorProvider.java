package com.iafenvoy.create.shape.shape;

import java.util.function.Function;

public interface SingleProcessorProvider {
    Function<ShapeInfo,ShapeInfo> getProcessor();
}
