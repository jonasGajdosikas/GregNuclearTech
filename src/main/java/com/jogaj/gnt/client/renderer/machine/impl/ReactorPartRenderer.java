package com.jogaj.gnt.client.renderer.machine.impl;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.client.model.machine.IControllerModelRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("NullableProblems")
public class ReactorPartRenderer extends DynamicRender<MultiblockControllerMachine, ReactorPartRenderer>
                                 implements IControllerModelRenderer {

    // spotless:off
    public static final Codec<ReactorPartRenderer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("").forGetter(ReactorPartRenderer::getCasing),
            BlockState.CODEC.fieldOf("").forGetter(ReactorPartRenderer::getTurbineCasing)
    ).apply(instance, ReactorPartRenderer::new));
    public static final DynamicRenderType<MultiblockControllerMachine, ReactorPartRenderer> TYPE = new DynamicRenderType<>(ReactorPartRenderer.CODEC);
    // spotless:on

    private final @Getter BlockState casing, turbineCasing;

    private BakedModel casingModel, turbineCasingModel;

    public ReactorPartRenderer(Supplier<? extends Block> casing, Supplier<? extends Block> turbineCasing){
        this(casing.get().defaultBlockState(), turbineCasing.get().defaultBlockState());
    }
    public ReactorPartRenderer(BlockState casing, BlockState turbineCasing){
        this.casing = casing;
        this.turbineCasing = turbineCasing;
    }

    @Override
    public DynamicRenderType<MultiblockControllerMachine, ReactorPartRenderer> getType() {
        return TYPE;
    }
    @Override
    public void render(MultiblockControllerMachine machine, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderPartModel(List<BakedQuad> quads, IMultiController machine, IMultiPart part, Direction frontFacing, @Nullable Direction side, RandomSource rand, @NotNull ModelData modelData, @Nullable RenderType renderType) {
        if (casingModel == null) casingModel = getModelForState(casing);
        if (turbineCasingModel == null) turbineCasingModel = getModelForState(turbineCasing);
        BakedModel model = shouldRenderAsTurbineCasing(part) ? turbineCasingModel : casingModel;
        BlockState state = shouldRenderAsTurbineCasing(part) ? turbineCasing : casing;
        emitQuads(quads, model, machine.self().getLevel(), part.self().getPos(), state, side, rand, modelData, renderType);
    }

    boolean shouldRenderAsTurbineCasing(IMultiPart part){
        if (part instanceof RotorHolderPartMachine) return true;
        if (!(part instanceof EnergyHatchPartMachine energyHatch)) return false;
        return energyHatch.energyContainer.getHandlerIO().equals(IO.OUT);
    }

    private BakedModel getModelForState(BlockState state){
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
    }

    private static void emitQuads(List<BakedQuad> quads, @Nullable BakedModel model,
                                  BlockAndTintGetter level, BlockPos pos, BlockState state,
                                  @Nullable Direction side, RandomSource rand,
                                  ModelData modelData, @Nullable RenderType renderType){
        if (model == null) return;
        modelData = model.getModelData(level, pos, state, modelData);
        quads.addAll(model.getQuads(state, side, rand, modelData, renderType));
    }
}
