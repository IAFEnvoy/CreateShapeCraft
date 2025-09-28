package com.iafenvoy.create.shape.data;

import com.iafenvoy.create.shape.item.attribute.ShapePartEmptyAttribute;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.logistics.filter.AttributeFilterWhitelistMode;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class BuiltinFilters {
    public static final List<List<ItemAttribute>> CORNERS = ShapeInfo.Quarter.stream()
            .map(x -> new ShapePartEmptyAttribute(x, false))
            .map(ItemAttribute.class::cast)
            .map(List::of)
            .toList();

    public static ItemStack toFilterItem(List<ItemAttribute> attributes) {
        ItemStack stack = AllItems.ATTRIBUTE_FILTER.asStack();
        stack.set(AllDataComponents.ATTRIBUTE_FILTER_WHITELIST_MODE, AttributeFilterWhitelistMode.WHITELIST_DISJ);
        List<ItemAttribute.ItemAttributeEntry> entries = new ArrayList<>();
        attributes.forEach(attribute -> entries.add(new ItemAttribute.ItemAttributeEntry(attribute, false)));
        stack.set(AllDataComponents.ATTRIBUTE_FILTER_MATCHED_ATTRIBUTES, entries);
        return stack;
    }
}
