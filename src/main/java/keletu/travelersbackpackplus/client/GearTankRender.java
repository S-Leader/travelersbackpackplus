package keletu.travelersbackpackplus.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tiviacz.travelersbackpack.component.Fluids;
import com.tiviacz.travelersbackpack.component.RenderInfo;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import keletu.travelersbackpackplus.client.model.GearModel;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;

public final class GearTankRender {
    public static FluidStack fluid(ItemStack stack) {
        if (!(stack.getItem() instanceof GearItem item) || item.type().tankCount() != 1) return FluidStack.EMPTY;
        Fluids fluids = stack.get(ModDataComponents.FLUIDS);
        return (fluids == null ? stack.getOrDefault(ModDataComponents.RENDER_INFO, RenderInfo.EMPTY).getLeftFluidStack()
                : fluids.leftFluidStack()).copy();
    }

    public static void submit(GearType type, GearModel model, FluidStack fluid, PoseStack poses, SubmitNodeCollector collector, int light, int overlay) {
        AABB bounds = bounds(type, fluid.getAmount());
        if (fluid.isEmpty() || bounds == null) return;
        var fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.getFluid().defaultFluidState());
        TextureAtlasSprite sprite = fluidModel.stillMaterial().sprite();
        int color = fluidModel.fluidTintSource() == null ? -1 : fluidModel.fluidTintSource().colorAsStack(fluid) | 0xFF000000;
        int emission = Math.clamp(fluid.getFluidType().getLightLevel(fluid), 0, 15) << 4;
        int fluidLight = light & 0xFFFF0000 | Math.max(light & 0xFFFF, emission);
        poses.pushPose();
        model.translateTank(poses);
        collector.submitCustomGeometry(poses, RenderTypes.entityTranslucentCullItemTarget(sprite.atlasLocation()),
                (pose, buffer) -> render(pose, buffer, bounds, sprite, color, fluidLight, overlay));
        poses.popPose();
    }

    static AABB bounds(GearType type, int amount) {
        if (type.tankCount() != 1 || amount <= 0) return null;
        double fill = Math.clamp((double) amount / type.tankCapacity(), 0, 1);
        double inset = type == GearType.COPTER ? 0.5 : 0.25;
        double width = type == GearType.COPTER ? 5 : 3;
        double bottom = type == GearType.COPTER ? 4.99 : 8.99;
        double top = bottom - (bottom - 1.01) * fill;
        return new AABB(inset / 16, top / 16, inset / 16, (width - inset) / 16, bottom / 16, (width - inset) / 16);
    }

    static void render(PoseStack.Pose pose, VertexConsumer buffer, AABB bounds, TextureAtlasSprite sprite, int color, int light, int overlay) {
        float x0 = (float) bounds.minX;
        float x1 = (float) bounds.maxX;
        float y0 = (float) bounds.minY;
        float y1 = (float) bounds.maxY;
        float z0 = (float) bounds.minZ;
        float z1 = (float) bounds.maxZ;
        float u0 = sprite.getU0();
        float ux = sprite.getU(x1 - x0);
        float uz = sprite.getU(z1 - z0);
        float v0 = sprite.getV0();
        float vz = sprite.getV(z1 - z0);
        float vTop = sprite.getV(1 - (y1 - y0));
        float vBottom = sprite.getV1();
        vertex(pose, buffer, x0, y0, z0, u0, v0, color, light, overlay, 0, -1, 0);
        vertex(pose, buffer, x1, y0, z0, ux, v0, color, light, overlay, 0, -1, 0);
        vertex(pose, buffer, x1, y0, z1, ux, vz, color, light, overlay, 0, -1, 0);
        vertex(pose, buffer, x0, y0, z1, u0, vz, color, light, overlay, 0, -1, 0);
        vertex(pose, buffer, x0, y1, z1, u0, vz, color, light, overlay, 0, 1, 0);
        vertex(pose, buffer, x1, y1, z1, ux, vz, color, light, overlay, 0, 1, 0);
        vertex(pose, buffer, x1, y1, z0, ux, v0, color, light, overlay, 0, 1, 0);
        vertex(pose, buffer, x0, y1, z0, u0, v0, color, light, overlay, 0, 1, 0);
        vertex(pose, buffer, x1, y0, z0, ux, vTop, color, light, overlay, 0, 0, -1);
        vertex(pose, buffer, x0, y0, z0, u0, vTop, color, light, overlay, 0, 0, -1);
        vertex(pose, buffer, x0, y1, z0, u0, vBottom, color, light, overlay, 0, 0, -1);
        vertex(pose, buffer, x1, y1, z0, ux, vBottom, color, light, overlay, 0, 0, -1);
        vertex(pose, buffer, x0, y0, z1, u0, vTop, color, light, overlay, 0, 0, 1);
        vertex(pose, buffer, x1, y0, z1, ux, vTop, color, light, overlay, 0, 0, 1);
        vertex(pose, buffer, x1, y1, z1, ux, vBottom, color, light, overlay, 0, 0, 1);
        vertex(pose, buffer, x0, y1, z1, u0, vBottom, color, light, overlay, 0, 0, 1);
        vertex(pose, buffer, x0, y0, z0, u0, vTop, color, light, overlay, -1, 0, 0);
        vertex(pose, buffer, x0, y0, z1, uz, vTop, color, light, overlay, -1, 0, 0);
        vertex(pose, buffer, x0, y1, z1, uz, vBottom, color, light, overlay, -1, 0, 0);
        vertex(pose, buffer, x0, y1, z0, u0, vBottom, color, light, overlay, -1, 0, 0);
        vertex(pose, buffer, x1, y0, z1, uz, vTop, color, light, overlay, 1, 0, 0);
        vertex(pose, buffer, x1, y0, z0, u0, vTop, color, light, overlay, 1, 0, 0);
        vertex(pose, buffer, x1, y1, z0, u0, vBottom, color, light, overlay, 1, 0, 0);
        vertex(pose, buffer, x1, y1, z1, uz, vBottom, color, light, overlay, 1, 0, 0);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v, int color, int light, int overlay, float nx, float ny, float nz) {
        buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(pose, nx, ny, nz);
    }
}
