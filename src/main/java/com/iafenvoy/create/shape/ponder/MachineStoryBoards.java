package com.iafenvoy.create.shape.ponder;

import com.iafenvoy.create.shape.data.BuiltinFilters;
import com.iafenvoy.create.shape.data.ShapeColor;
import com.iafenvoy.create.shape.data.ShapeInfo;
import com.iafenvoy.create.shape.data.ShapeProcessors;
import com.iafenvoy.create.shape.item.ShapeItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.SidedFilteringBehaviour;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.catnip.math.VecHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import plus.dragons.createdragonsplus.common.registry.CDPFluids;

import java.util.List;

public class MachineStoryBoards {
    public static void rotator(SceneBuilder builder, SceneBuildingUtil util) {
        ShapeInfo example = ShapeInfo.parse("Su------");
        CreateSceneBuilder sb = new CreateSceneBuilder(builder);
        //Start
        sb.title("rotator", "Use rotators to rotate shapes");
        sb.configureBasePlate(0, 0, 7);
        //Initialize
        sb.world().showSection(util.select().layer(0), Direction.UP);
        sb.idle(10);
        sb.world().showSection(util.select().fromTo(0, 1, 3, 6, 1, 4), Direction.DOWN);
        sb.idle(10);
        sb.world().showSection(util.select().position(3, 2, 3), Direction.DOWN);
        sb.idle(10);
        sb.world().cycleBlockProperty(new BlockPos(3, 1, 3), BeltBlock.CASING);
        //Introduce
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("The rotator rotates shapes according to the direction drawn on the top.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 2, 3), Direction.WEST));
        sb.idle(60);
        //Example
        ItemStack stack1 = ShapeItem.fromInfo(example);
        sb.world().createItemOnBelt(util.grid().at(5, 1, 3), Direction.EAST, stack1);
        sb.overlay().showControls(new Vec3(5.5, 2, 3.5), Pointing.DOWN, 50).withItem(stack1);
        sb.idle(20);
        sb.overlay().showControls(new Vec3(1.5, 2, 3.5), Pointing.DOWN, 30).withItem(ShapeItem.fromInfo(ShapeProcessors.rotateClockwise(example)));
        sb.idle(40);
        //Display all variants
        sb.world().showSection(util.select().fromTo(1, 1, 5, 5, 2, 5), Direction.DOWN);
        sb.world().cycleBlockProperty(new BlockPos(3, 1, 5), BeltBlock.CASING);
        sb.idle(5);
        sb.world().showSection(util.select().fromTo(1, 1, 1, 5, 2, 2), Direction.DOWN);
        sb.world().cycleBlockProperty(new BlockPos(3, 1, 1), BeltBlock.CASING);
        sb.idle(10);
        sb.overlay().showText(70)
                .attachKeyFrame()
                .text("There are 3 variants of rotators.");
        sb.overlay().showText(70)
                .text("Clockwise")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 2, 3), Direction.WEST));
        sb.overlay().showText(70)
                .text("Counterclockwise")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 2, 5), Direction.WEST));
        sb.overlay().showText(70)
                .text("180 Degree")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 2, 1), Direction.WEST));
        sb.idle(80);
        ItemStack stack2 = ShapeItem.fromInfo(example);
        sb.world().createItemOnBelt(util.grid().at(5, 1, 1), Direction.EAST, stack2);
        sb.world().createItemOnBelt(util.grid().at(5, 1, 3), Direction.EAST, stack2);
        sb.world().createItemOnBelt(util.grid().at(5, 1, 5), Direction.EAST, stack2);
        sb.overlay().showControls(new Vec3(5.5, 2, 1.5), Pointing.DOWN, 70).withItem(stack2);
        sb.overlay().showControls(new Vec3(5.5, 2, 3.5), Pointing.DOWN, 70).withItem(stack2);
        sb.overlay().showControls(new Vec3(5.5, 2, 5.5), Pointing.DOWN, 70).withItem(stack2);
        sb.idle(20);
        sb.overlay().showControls(new Vec3(1.5, 2, 1.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(ShapeProcessors.rotate180(example)));
        sb.overlay().showControls(new Vec3(1.5, 2, 3.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(ShapeProcessors.rotateClockwise(example)));
        sb.overlay().showControls(new Vec3(1.5, 2, 5.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(ShapeProcessors.rotateCounterclockwise(example)));
        sb.idle(50);
    }

    public static void dyer(SceneBuilder builder, SceneBuildingUtil util) {
        ShapeInfo example = ShapeInfo.parse("CuCuCuCu");
        CreateSceneBuilder sb = new CreateSceneBuilder(builder);
        //Start
        sb.title("dyer", "Use dyers to dye shapes");
        sb.configureBasePlate(0, 0, 5);
        //Initialize
        sb.world().showSection(util.select().layer(0), Direction.UP);
        sb.idle(10);
        sb.world().showSection(util.select().fromTo(0, 1, 2, 4, 1, 2), Direction.DOWN);
        sb.world().showSection(util.select().fromTo(4, 1, 3, 4, 1, 5), Direction.DOWN);
        sb.idle(10);
        sb.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);
        sb.idle(10);
        sb.world().cycleBlockProperty(new BlockPos(2, 1, 2), BeltBlock.CASING);
        //Introduce
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("The dyer dyes the whole shape with dye fluid.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(2, 2, 2), Direction.WEST));
        sb.idle(60);
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("Base shapes insert from sides of the machine.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 1, 2), Direction.UP));
        sb.idle(60);
        sb.world().showSection(util.select().fromTo(0, 1, 3, 2, 2, 4), Direction.NORTH);
        sb.idle(10);
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("Dye Fluid can insert from other 2 sides.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(0, 2, 4), Direction.UP));
        sb.idle(60);
        sb.world().setBlock(new BlockPos(2, 1, 3), AllBlocks.COGWHEEL.getDefaultState().setValue(CogWheelBlock.AXIS, Direction.Axis.Z), false);
        sb.world().showIndependentSection(util.select().position(3, 1, 2), Direction.NORTH);
        //Example
        ItemStack shapeStack = ShapeItem.fromInfo(example);
        sb.world().createItemOnBelt(util.grid().at(4, 1, 2), Direction.EAST, shapeStack);
        sb.overlay().showControls(new Vec3(4.5, 2, 2.5), Pointing.DOWN, 50).withItem(shapeStack);
        sb.overlay().showControls(new Vec3(0.5, 3, 4.5), Pointing.DOWN, 50).withItem(getDyeFluidBucket(DyeColor.RED));
        sb.idle(20);
        sb.overlay().showControls(new Vec3(0.5, 2, 2.5), Pointing.DOWN, 30).withItem(ShapeItem.fromInfo(ShapeProcessors.color(example, ShapeColor.RED)));
        sb.idle(40);
    }

    public static void stacker(SceneBuilder builder, SceneBuildingUtil util) {
        ShapeInfo example = ShapeInfo.parse("CgCgCgCg"), exampleUpper = ShapeInfo.parse("RrRrRrRr");
        CreateSceneBuilder sb = new CreateSceneBuilder(builder);
        //Start
        sb.title("stacker", "Use stackers to stack shapes");
        sb.configureBasePlate(0, 0, 5);
        //Initialize
        sb.world().showSection(util.select().layer(0), Direction.UP);
        sb.idle(10);
        sb.world().showSection(util.select().fromTo(0, 1, 2, 4, 1, 2), Direction.DOWN);
        sb.world().showSection(util.select().fromTo(4, 1, 3, 4, 1, 5), Direction.DOWN);
        sb.idle(10);
        sb.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);
        sb.idle(10);
        sb.world().cycleBlockProperty(new BlockPos(2, 1, 2), BeltBlock.CASING);
        //Introduce
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("The stacker stacks 2 shapes together.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(2, 2, 2), Direction.WEST));
        sb.idle(60);
        sb.world().showSection(util.select().fromTo(0, 1, 3, 2, 2, 4), Direction.NORTH);
        sb.idle(10);
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("Base shapes insert from sides of the machine.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 1, 2), Direction.UP));
        sb.idle(60);
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("Upper shapes can only insert into the top of the machine.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(2, 2, 2), Direction.UP));
        sb.idle(60);
        //Example
        ItemStack stack = ShapeItem.fromInfo(example), upperStack = ShapeItem.fromInfo(exampleUpper);
        sb.world().createItemOnBelt(util.grid().at(4, 1, 2), Direction.EAST, stack);
        sb.overlay().showControls(new Vec3(4.5, 2, 2.5), Pointing.DOWN, 50).withItem(stack);
        sb.world().createItemOnBelt(util.grid().at(2, 2, 4), Direction.SOUTH, upperStack);
        sb.overlay().showControls(new Vec3(2.5, 3, 4.5), Pointing.DOWN, 50).withItem(upperStack);
        sb.idle(20);
        sb.overlay().showControls(new Vec3(0.5, 2, 2.5), Pointing.DOWN, 30).withItem(ShapeItem.fromInfo(ShapeProcessors.stack(example, exampleUpper)));
        sb.idle(40);
    }

    public static void cutter(SceneBuilder builder, SceneBuildingUtil util) {
        ShapeInfo example = ShapeInfo.parse("CrCcCcCr");
        List<ShapeInfo> results = ShapeProcessors.cutHorizontal(example);
        CreateSceneBuilder sb = new CreateSceneBuilder(builder);
        //Start
        sb.title("cutter", "Use cutters to cut shapes");
        sb.configureBasePlate(0, 0, 6);
        //Initialize
        sb.world().showSection(util.select().layer(0), Direction.UP);
        sb.idle(10);
        sb.world().showSection(util.select().fromTo(0, 1, 3, 5, 1, 3), Direction.DOWN);
        sb.world().showSection(util.select().fromTo(5, 1, 4, 5, 1, 6), Direction.DOWN);
        sb.idle(10);
        sb.world().showSection(util.select().position(3, 2, 3), Direction.DOWN);
        sb.idle(10);
        sb.world().cycleBlockProperty(new BlockPos(3, 1, 3), BeltBlock.CASING);
        //Introduce
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("The cutter cut shapes into pieces according to the direction drawn on the top.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 2, 3), Direction.WEST));
        sb.idle(60);
        ItemStack stack1 = ShapeItem.fromInfo(example);
        sb.world().createItemOnBelt(util.grid().at(4, 1, 3), Direction.EAST, stack1);
        sb.overlay().showControls(new Vec3(5.5, 2, 3.5), Pointing.DOWN, 70).withItem(stack1);
        sb.idle(20);
        sb.overlay().showControls(new Vec3(0.5, 2, 3.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(results.getFirst()));
        sb.overlay().showControls(new Vec3(1.5, 2, 3.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(results.getLast()));
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("All results will output on the same belt.");
        sb.idle(60);
        sb.world().showSection(util.select().fromTo(0, 1, 2, 2, 1, 2), Direction.DOWN);
        sb.idle(10);
        //Brass Tunnel
        Selection tunnels = util.select().fromTo(2, 2, 2, 2, 2, 3);
        sb.world().setBlocks(tunnels, AllBlocks.BRASS_TUNNEL.getDefaultState(), false);
        sb.world().modifyBlockEntity(new BlockPos(2, 2, 2), BrassTunnelBlockEntity.class, be -> {
            be.initialize();
            be.getBehaviour(SidedFilteringBehaviour.TYPE).setFilter(Direction.WEST, BuiltinFilters.toFilterItem(BuiltinFilters.CORNERS.get(1)));
        });
        sb.world().modifyBlockEntity(new BlockPos(2, 2, 3), BrassTunnelBlockEntity.class, be -> {
            be.initialize();
            be.getBehaviour(SidedFilteringBehaviour.TYPE).setFilter(Direction.WEST, BuiltinFilters.toFilterItem(BuiltinFilters.CORNERS.get(3)));
        });
        sb.world().showSection(tunnels, Direction.DOWN);
        sb.idle(10);
        sb.world().cycleBlockProperty(new BlockPos(2, 2, 2), BeltBlock.CASING);
        sb.world().cycleBlockProperty(new BlockPos(2, 2, 3), BeltBlock.CASING);
        sb.overlay().showText(70)
                .attachKeyFrame()
                .text("We can use brass tunnels and attribute filters to split them out.");
        Vec3 filter1 = getTunnelFilterVec(new BlockPos(2, 2, 2));
        sb.overlay().showFilterSlotInput(filter1, Direction.WEST, 50);
        sb.overlay().showText(70)
                .attachKeyFrame()
                .text("Bottom right is not empty")
                .placeNearTarget()
                .pointAt(filter1);
        Vec3 filter2 = getTunnelFilterVec(new BlockPos(2, 2, 3));
        sb.overlay().showFilterSlotInput(filter2, Direction.WEST, 50);
        sb.overlay().showText(70)
                .attachKeyFrame()
                .text("Top left is not empty")
                .placeNearTarget()
                .pointAt(filter2);
        sb.idle(80);
        ItemStack stack2 = ShapeItem.fromInfo(example);
        sb.world().createItemOnBelt(util.grid().at(4, 1, 3), Direction.EAST, stack2);
        sb.overlay().showControls(new Vec3(5.5, 2, 3.5), Pointing.DOWN, 70).withItem(stack2);
        sb.idle(20);
        sb.overlay().showControls(new Vec3(0.5, 2, 3.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(results.getFirst()));
        sb.overlay().showControls(new Vec3(0.5, 2, 2.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(results.getLast()));
        sb.idle(60);
    }

    public static void cutterVariants(SceneBuilder builder, SceneBuildingUtil util) {
        ShapeInfo example = ShapeInfo.parse("CrCyCcCp");
        ItemStack stack = ShapeItem.fromInfo(example);
        List<ShapeInfo> horizontal = ShapeProcessors.cutHorizontal(example), vertical = ShapeProcessors.cutVertical(example), quarter = ShapeProcessors.cutQuarter(example);
        CreateSceneBuilder sb = new CreateSceneBuilder(builder);
        //Start
        sb.title("cutter_variants", "Cutter variants");
        sb.configureBasePlate(-2, 0, 11);
        //Initialize
        sb.world().showSection(util.select().layer(0), Direction.UP);
        sb.idle(10);
        sb.world().showSection(util.select().fromTo(0, 1, 6, 5, 2, 10), Direction.DOWN);
        sb.idle(10);
        sb.world().showSection(util.select().fromTo(0, 1, 3, 5, 2, 5), Direction.DOWN);
        sb.idle(10);
        sb.world().showSection(util.select().fromTo(0, 1, 0, 5, 2, 2), Direction.DOWN);
        sb.idle(10);
        //Introduce
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("There are 3 variants of cutters.");
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("Horizontal Cutter")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 2, 0), Direction.UP));
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("Vertical Cutter")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 2, 3), Direction.UP));
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("Quarter Cutter")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 2, 6), Direction.UP));
        sb.idle(60);
        sb.rotateCameraY(-45);
        sb.idle(20);
        sb.world().createItemOnBelt(util.grid().at(5, 1, 0), Direction.EAST, stack.copy());
        sb.overlay().showControls(new Vec3(5.5, 2, 0.5), Pointing.DOWN, 150).withItem(stack);
        sb.idle(20);
        sb.overlay().showControls(new Vec3(0.5, 2, 0.5), Pointing.DOWN, 130).withItem(ShapeItem.fromInfo(horizontal.getFirst()));
        sb.overlay().showControls(new Vec3(0.5, 2, 1.5), Pointing.DOWN, 130).withItem(ShapeItem.fromInfo(horizontal.getLast()));
        sb.idle(20);
        sb.world().createItemOnBelt(util.grid().at(5, 1, 3), Direction.EAST, stack.copy());
        sb.overlay().showControls(new Vec3(5.5, 2, 3.5), Pointing.DOWN, 110).withItem(stack);
        sb.idle(20);
        sb.overlay().showControls(new Vec3(0.5, 2, 3.5), Pointing.DOWN, 90).withItem(ShapeItem.fromInfo(vertical.getFirst()));
        sb.overlay().showControls(new Vec3(0.5, 2, 4.5), Pointing.DOWN, 90).withItem(ShapeItem.fromInfo(vertical.getLast()));
        sb.idle(20);
        sb.world().createItemOnBelt(util.grid().at(5, 1, 6), Direction.EAST, stack.copy());
        sb.overlay().showControls(new Vec3(5.5, 2, 6.5), Pointing.DOWN, 70).withItem(stack);
        sb.idle(20);
        sb.overlay().showControls(new Vec3(0.5, 2, 6.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(quarter.getFirst()));
        sb.overlay().showControls(new Vec3(0.5, 2, 7.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(quarter.get(1)));
        sb.overlay().showControls(new Vec3(0.5, 2, 8.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(quarter.get(2)));
        sb.overlay().showControls(new Vec3(0.5, 2, 9.5), Pointing.DOWN, 50).withItem(ShapeItem.fromInfo(quarter.get(3)));
        sb.idle(60);
        sb.rotateCameraY(45);
        sb.idle(20);
    }

    public static void colorMixer(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder sb = new CreateSceneBuilder(builder);
        //Start
        sb.title("color_mixer", "Use color mixers to mix dye liquid.");
        sb.configureBasePlate(0, 0, 7);
        //Initialize
        sb.world().showSection(util.select().layer(0), Direction.UP);
        sb.idle(10);
        sb.world().showSection(util.select().position(3, 1, 3), Direction.DOWN);
        sb.idle(10);
        //Introduce
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("The color mixer can mix two dye fluid into a new color.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 1, 3), Direction.UP));
        sb.idle(60);
        sb.world().showSection(util.select().position(3, 1, 1), Direction.DOWN);
        sb.idle(10);
        sb.overlay().showText(70)
                .attachKeyFrame()
                .text("There are 2 inputs and 1 output on the sides of color mixer.");
        sb.overlay().showText(70)
                .attachKeyFrame()
                .text("Input 1")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 1, 3), Direction.WEST));
        sb.overlay().showText(70)
                .attachKeyFrame()
                .text("Input 2")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 1, 1), Direction.WEST).add(0, 0.25, 0.25));
        sb.overlay().showText(70)
                .attachKeyFrame()
                .text("Output")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(new BlockPos(3, 1, 1), Direction.NORTH).subtract(0.25, 0.25, 0));
        sb.idle(80);
        sb.world().hideSection(util.select().position(3, 1, 1), Direction.UP);
        sb.idle(20);
        sb.world().showSection(util.select().fromTo(4, 1, 3, 6, 2, 7), Direction.WEST);
        sb.idle(5);
        sb.world().showSection(util.select().fromTo(3, 1, 4, 3, 2, 6), Direction.NORTH);
        sb.idle(5);
        sb.world().showSection(util.select().fromTo(2, 1, 3, 0, 2, 6), Direction.EAST);
        sb.idle(10);
        sb.overlay().showText(50)
                .attachKeyFrame()
                .text("Use pipes and pumps to interact.");
        sb.idle(60);
        //Example
        sb.world().modifyBlockEntity(new BlockPos(6, 1, 3), FluidTankBlockEntity.class, be -> be.getTankInventory().setFluid(new FluidStack(getDyeFluid(DyeColor.RED), 16000)));
        sb.overlay().showControls(new Vec3(6.5, 3, 3.5), Pointing.DOWN, 70).withItem(getDyeFluidBucket(DyeColor.RED));
        sb.world().modifyBlockEntity(new BlockPos(0, 1, 3), FluidTankBlockEntity.class, be -> be.getTankInventory().setFluid(new FluidStack(getDyeFluid(DyeColor.BLUE), 16000)));
        sb.overlay().showControls(new Vec3(0.5, 3, 3.5), Pointing.DOWN, 70).withItem(getDyeFluidBucket(DyeColor.BLUE));
        sb.idle(20);
        sb.overlay().showControls(new Vec3(3.5, 3, 6.5), Pointing.DOWN, 50).withItem(getDyeFluidBucket(DyeColor.PURPLE));
        sb.idle(60);
    }

    private static Fluid getDyeFluid(DyeColor color) {
        return CDPFluids.DYES_BY_COLOR.get(color).get();
    }

    private static ItemStack getDyeFluidBucket(DyeColor color) {
        return getDyeFluid(color).getBucket().getDefaultInstance();
    }

    private static Vec3 getTunnelFilterVec(BlockPos pos) {
        return VecHelper.getCenterOf(pos).add(Vec3.atLowerCornerOf(Direction.WEST.getNormal()).scale(.5)).add(0, 0.3, 0);
    }
}
