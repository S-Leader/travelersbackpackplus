package keletu.travelersbackpackplus.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tiviacz.travelersbackpack.init.ModDataComponents;
import keletu.travelersbackpackplus.TravelersBackpackPlus;
import keletu.travelersbackpackplus.client.model.GearModel;
import keletu.travelersbackpackplus.client.model.GearPose;
import keletu.travelersbackpackplus.client.model.OriginalModels;
import keletu.travelersbackpackplus.gear.GearItem;
import keletu.travelersbackpackplus.gear.GearState;
import keletu.travelersbackpackplus.gear.GearType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public final class GearRender {
    private static final Map<GearType, GearModel> BODIES = new EnumMap<>(GearType.class);
    private static final Map<GearType, GearModel> HEADS = new EnumMap<>(GearType.class);

    public static void reload() {
        BODIES.clear();
        HEADS.clear();
        for (GearType type : GearType.values()) {
            var root = OriginalModels.bake(type);
            BODIES.put(type, new GearModel(root.getChild("body"), type, false));
            HEADS.put(type, new GearModel(root.getChild("head"), type, true));
        }
    }

    public static boolean worn(HumanoidModel<?> parent, PoseStack poses, SubmitNodeCollector collector, int light, HumanoidRenderState renderState, ItemStack stack) {
        if (!(stack.getItem() instanceof GearItem item)) return false;
        if (!stack.getOrDefault(ModDataComponents.IS_VISIBLE, true)) return true;
        ensureModels();
        GearState state = new GearState(stack);
        GearPose pose = new GearPose(renderState.ageInTicks * 0.5F, state.enabled());
        Identifier texture = texture(item.type());
        poses.pushPose();
        parent.body.translateAndRotate(poses);
        collector.submitModel(BODIES.get(item.type()), pose, poses, texture, light, OverlayTexture.NO_OVERLAY, renderState.outlineColor, null);
        poses.popPose();
        poses.pushPose();
        parent.head.translateAndRotate(poses);
        collector.submitModel(HEADS.get(item.type()), pose, poses, texture, light, OverlayTexture.NO_OVERLAY, renderState.outlineColor, null);
        poses.popPose();
        return true;
    }

    public static void placed(GearType type, PoseStack poses, SubmitNodeCollector collector, int light, int overlay, int outline) {
        ensureModels();
        collector.submitModel(BODIES.get(type), new GearPose(0, true), poses, texture(type), light, overlay, outline, null);
    }

    private static Identifier texture(GearType type) {
        return TravelersBackpackPlus.id("textures/models/" + type.name().toLowerCase(Locale.ROOT) + ".png");
    }

    private static void ensureModels() {
        if (BODIES.isEmpty()) reload();
    }
}
