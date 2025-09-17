package com.iafenvoy.create.shape;

import com.iafenvoy.create.shape.item.ShapeItem;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@EventBusSubscriber
public class ShapeCommand {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(literal("shape")
                .requires(CommandSourceStack::isPlayer)
                .then(argument("key", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            ctx.getSource().getPlayerOrException().getInventory().add(ShapeItem.fromKey(StringArgumentType.getString(ctx, "key")));
                            return 1;
                        })
                ));
    }
}
