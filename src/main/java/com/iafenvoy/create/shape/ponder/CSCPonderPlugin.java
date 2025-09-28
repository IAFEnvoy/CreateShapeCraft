package com.iafenvoy.create.shape.ponder;

import com.iafenvoy.create.shape.CreateShapeCraft;
import com.iafenvoy.create.shape.registry.CSCBlocks;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public enum CSCPonderPlugin implements PonderPlugin {
    INSTANCE;
    private static final ResourceLocation MACHINE_TAG_ID = ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, "machine");

    @Override
    public @NotNull String getModId() {
        return CreateShapeCraft.MOD_ID;
    }

    @Override
    public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(CSCBlocks.ROTATOR_CW.getId(), CSCBlocks.ROTATOR_CCW.getId(), CSCBlocks.ROTATOR_180.getId())
                .addStoryBoard(ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, "rotator"), MachineStoryBoards::rotator);
        helper.forComponents(CSCBlocks.DYER.getId())
                .addStoryBoard(ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, "dyer"), MachineStoryBoards::dyer);
        helper.forComponents(CSCBlocks.STACKER.getId())
                .addStoryBoard(ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, "stacker"), MachineStoryBoards::stacker);
        helper.forComponents(CSCBlocks.CUTTER_HORIZONTAL.getId(), CSCBlocks.CUTTER_VERTICAL.getId(), CSCBlocks.CUTTER_QUARTER.getId())
                .addStoryBoard(ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, "cutter"), MachineStoryBoards::cutter)
                .addStoryBoard(ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, "cutter_variants"), MachineStoryBoards::cutterVariants);
        helper.forComponents(CSCBlocks.COLOR_MIXER.getId())
                .addStoryBoard(ResourceLocation.fromNamespaceAndPath(CreateShapeCraft.MOD_ID, "color_mixer"), MachineStoryBoards::colorMixer);
    }

    @Override
    public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
        helper.registerTag(MACHINE_TAG_ID)
                .addToIndex()
                .item(CSCBlocks.ROTATOR_CW.get(), true, false)
                .title("Shape Processing Machine")
                .description("These machine can process shapes.")
                .register();
        helper.addToTag(MACHINE_TAG_ID)
                .add(CSCBlocks.ROTATOR_CW.getId())
                .add(CSCBlocks.ROTATOR_CCW.getId())
                .add(CSCBlocks.ROTATOR_180.getId())
                .add(CSCBlocks.DYER.getId())
                .add(CSCBlocks.STACKER.getId())
                .add(CSCBlocks.CUTTER_HORIZONTAL.getId())
                .add(CSCBlocks.CUTTER_VERTICAL.getId())
                .add(CSCBlocks.CUTTER_QUARTER.getId())
                .add(CSCBlocks.COLOR_MIXER.getId());
    }
}
