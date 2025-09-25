package com.iafenvoy.create.shape.util;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.client.gui.Font;
import net.minecraft.util.Mth;

public final class TextUtil {
    public static int getIndents(Font font, int defaultIndents) {
        int spaceWidth = font.width(" ");
        return LangBuilder.DEFAULT_SPACE_WIDTH == spaceWidth ? defaultIndents : Mth.ceil(LangBuilder.DEFAULT_SPACE_WIDTH * defaultIndents / spaceWidth);
    }
}
