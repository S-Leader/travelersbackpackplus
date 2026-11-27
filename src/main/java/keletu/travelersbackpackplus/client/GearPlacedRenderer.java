package keletu.travelersbackpackplus.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tiviacz.travelersbackpack.block.TravelersBackpackBlock;
import com.tiviacz.travelersbackpack.blockentity.BackpackBlockEntity;
import keletu.travelersbackpackplus.gear.GearItem;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class GearPlacedRenderer implements BlockEntityRenderer<BackpackBlockEntity, GearPlacedState> {
    public GearPlacedRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public GearPlacedState createRenderState() {
        return new GearPlacedState();
    }

    @Override
    public void extractRenderState(BackpackBlockEntity entity, GearPlacedState state, float partialTick, Vec3 camera, ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTick, camera, overlay);
        state.type = entity.getWrapper().getBackpackStack().getItem() instanceof GearItem item ? item.type() : null;
        state.fluid = GearTankRender.fluid(entity.getWrapper().getBackpackStack());
        state.rotation = entity.getBlockState().getValue(TravelersBackpackBlock.FACING).toYRot();
    }

    @Override
    public void submit(GearPlacedState state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.type == null) return;
        poses.pushPose();
        poses.translate(0.5, 0.75, 0.5);
        poses.mulPose(Axis.ZP.rotationDegrees(180));
        poses.mulPose(Axis.YP.rotationDegrees(state.rotation));
        poses.translate(0, 0, -0.3);
        GearRender.placed(state.type, state.fluid, poses, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poses.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(BackpackBlockEntity entity) {
        return new AABB(entity.getBlockPos()).inflate(2);
    }
}
